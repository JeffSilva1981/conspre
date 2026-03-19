package com.jeferson.conspre.services;

import com.jeferson.conspre.dto.CategoryDTO;
import com.jeferson.conspre.entity.Category;
import com.jeferson.conspre.repositories.CategoryRepository;
import com.jeferson.conspre.services.exceptions.DatabaseException;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository repository;

    private Long existingId, nonExistingId;

    private Category category;
    private CategoryDTO dto;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;

        category = new Category();
        category.setId(existingId);
        category.setName("Solda");
        category.setDescription("Materiais de solda");
        category.setAtivo(true);

        dto = new CategoryDTO();
        dto.setName("Solda");
        dto.setDescription("Materiais de solda");

        // =============================
        // FIND BY ID
        // =============================
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(category));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // =============================
        // INSERT / UPDATE
        // =============================
        Mockito.when(repository.existsByNameIgnoreCaseAndAtivoTrue(dto.getName())).thenReturn(false);
        Mockito.when(repository.existsByNameIgnoreCaseAndAtivoTrueAndIdNot(dto.getName(), existingId)).thenReturn(false);

        Mockito.when(repository.save(Mockito.any())).thenReturn(category);

        // =============================
        // UPDATE getReference
        // =============================
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(category);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    // ===============================
    // ✅ FIND ALL
    // ===============================
    @Test
    public void findAllShouldReturnPage() {

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Category> page = new PageImpl<>(List.of(category));

        Mockito.when(repository.findAll(pageRequest)).thenReturn(page);

        Page<CategoryDTO> result = service.findAll(pageRequest);

        Assertions.assertFalse(result.isEmpty());
    }

    // ===============================
    // ✅ FIND BY ID
    // ===============================
    @Test
    public void findByIdShouldReturnDTOWhenIdExists() {

        CategoryDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Solda", result.getName());
    }

    @Test
    public void findByIdShouldThrowExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    // ===============================
    // ✅ INSERT
    // ===============================
    @Test
    public void insertShouldReturnDTOWhenValidData() {

        CategoryDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Solda", result.getName());
    }

    @Test
    public void insertShouldThrowExceptionWhenNameAlreadyExists() {

        Mockito.when(repository.existsByNameIgnoreCaseAndAtivoTrue(dto.getName()))
                .thenReturn(true);

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.insert(dto);
        });
    }

    // ===============================
    // ✅ UPDATE
    // ===============================
    @Test
    public void updateShouldReturnDTOWhenIdExists() {

        CategoryDTO result = service.update(existingId, dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Solda", result.getName());
    }

    @Test
    public void updateShouldThrowExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, dto);
        });
    }

    @Test
    public void updateShouldThrowExceptionWhenNameAlreadyExists() {

        Mockito.when(repository.existsByNameIgnoreCaseAndAtivoTrueAndIdNot(dto.getName(), existingId))
                .thenReturn(true);

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.update(existingId, dto);
        });
    }

    // ===============================
    // ✅ DELETE (SOFT DELETE)
    // ===============================
    @Test
    public void deleteShouldSetAtivoFalseWhenIdExists() {

        service.delete(existingId);

        Assertions.assertFalse(category.isAtivo());
        Mockito.verify(repository, Mockito.times(1)).save(category);
    }

    @Test
    public void deleteShouldThrowExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }
}
