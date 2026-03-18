package com.jeferson.conspre.repositories;

import com.jeferson.conspre.dto.MaterialMinDTO;
import com.jeferson.conspre.entity.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@DataJpaTest
public class MaterialRepositoryTests {

    @Autowired
    private MaterialRepository repository;

    private Long existingId, nonExistingId;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 200L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        repository.deleteById(existingId);
        Optional<Material> result = repository.findById(existingId);
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

        Material material = Factory.createNewMaterial();
        material.setId(null);
        material = repository.save(material);
        Assertions.assertNotNull(material.getId());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {

        Optional<Material> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyWhenIdDoesNotExist() {

        Optional<Material> result = repository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldUpdateObjectWhenIdExists() {

        Material material = repository.findById(existingId).get();
        material.setName("Material Atualizado");
        material = repository.save(material);
        Assertions.assertEquals("Material Atualizado", material.getName());
    }

    @Test
    public void searchShouldReturnPageWhenMaterialExists() {

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MaterialMinDTO> result = repository.search(null, null, null, null, pageRequest);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

    }

}
