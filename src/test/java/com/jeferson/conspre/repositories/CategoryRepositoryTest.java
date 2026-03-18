package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    private Long existingId, nonExistingId;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 200L;
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {

        Optional<Category> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyWhenIdDoesNotExists() {

        Optional<Category> result = repository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        repository.deleteById(existingId);
        Optional<Category> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowsExceptionWhenIdDoesNotExist() {

        Assertions.assertDoesNotThrow(() -> {
            repository.deleteById(nonExistingId);
        });
    }

    @Test
    public void saveShouldPersistObjectWhenIdIsNull() {

        Category category = Factory.createdCategory();
        category.setId(null);
        category = repository.save(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertTrue(category.isAtivo());
        Assertions.assertEquals("Especial", category.getName());
    }

    @Test
    public void saveShouldUpdateObjectWhenIdExists() {

        Category category = repository.findById(existingId).get();
        category.setName("Categoria atualizada");
        repository.save(category);

        Assertions.assertEquals("Categoria atualizada", category.getName());
    }

    @Test
    public void pageShouldReturnPageWhenCategoryExists() {

        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<Category> result = repository.findAll(pageRequest);
        Assertions.assertFalse(result.isEmpty());
    }
}
