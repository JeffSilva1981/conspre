package com.jeferson.conspre.services;

import com.jeferson.conspre.dto.MaterialDTO;
import com.jeferson.conspre.entity.Material;
import com.jeferson.conspre.repositories.CategoryRepository;
import com.jeferson.conspre.repositories.MaterialRepository;
import com.jeferson.conspre.services.exceptions.DatabaseException;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class MaterialServiceTests {

    @InjectMocks
    private MaterialService service;

    @Mock
    private MaterialRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId, nonExistingId;

    private Material material;
    private MaterialDTO dto;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;

        material = new Material();
        material.setId(existingId);
        material.setName("Eletrodo");
        material.setAtivo(true);

        dto = new MaterialDTO();
        dto.setName("Eletrodo");
        dto.setAtivo(true);

        // =============================
        // FIND BY ID
        // =============================
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(material));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // =============================
        // INSERT / UPDATE
        // =============================
        Mockito.when(repository.existsByNameIgnoreCaseAndAtivoTrue(dto.getName())).thenReturn(false);
        Mockito.when(repository.existsByNameIgnoreCaseAndAtivoTrueAndIdNot(dto.getName(), existingId)).thenReturn(false);

        Mockito.when(repository.save(Mockito.any())).thenReturn(material);

        // =============================
        // UPDATE getReference
        // =============================
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(material);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        // =============================
        // DELETE
        // =============================
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(material));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
    }

    // ===============================
    // ✅ FIND BY ID
    // ===============================
    @Test
    public void findByIdShouldReturnDTOWhenIdExists() {

        MaterialDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Eletrodo", result.getName());
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

        MaterialDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Eletrodo", result.getName());
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

        MaterialDTO result = service.update(existingId, dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Eletrodo", result.getName());
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

        Assertions.assertFalse(material.isAtivo());
        Mockito.verify(repository, Mockito.times(1)).save(material);
    }

    @Test
    public void deleteShouldThrowExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }
}