package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class StockMovementRepositoryTests {

    @Autowired
    private StockMovementRepository repository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MaterialRequestRepository materialRequestRepository;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
    }

    // ===============================
    // ✅ SAVE
    // ===============================
    @Test
    public void saveShouldPersistObject() {

        Material material = materialRepository.save(Factory.createNewMaterial());
        User user = userRepository.save(Factory.createdUser());
        Employee employee = employeeRepository.save(Factory.createdEmployee());
        MaterialRequest materialRequest = materialRequestRepository.save(Factory.createdMaterialRequest(user, employee));
        StockMovement movement = Factory.createdStockMovement(material, user, employee, materialRequest);
        movement = repository.save(movement);

        Assertions.assertNotNull(movement.getId());
        Assertions.assertNotNull(movement.getMoment()); // preenchido automaticamente
    }

    // ===============================
    // ✅ FIND BY ID
    // ===============================
    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {

        Material material = materialRepository.save(Factory.createNewMaterial());
        User user = userRepository.save(Factory.createdUser());
        Employee employee = employeeRepository.save(Factory.createdEmployee());
        MaterialRequest materialRequest = materialRequestRepository.save(Factory.createdMaterialRequest(user, employee));
        StockMovement movement = Factory.createdStockMovement(material, user, employee, materialRequest);
        repository.save(movement);

        Optional<StockMovement> result = repository.findById(movement.getId());

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyWhenIdDoesNotExist() {

        Optional<StockMovement> result = repository.findById(nonExistingId);

        Assertions.assertTrue(result.isEmpty());
    }

    // ===============================
    // ✅ DELETE
    // ===============================
    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        Material material = materialRepository.save(Factory.createNewMaterial());
        User user = userRepository.save(Factory.createdUser());
        Employee employee = employeeRepository.save(Factory.createdEmployee());
        MaterialRequest materialRequest = materialRequestRepository.save(Factory.createdMaterialRequest(user, employee));
        StockMovement movement = Factory.createdStockMovement(material, user, employee, materialRequest);
        repository.save(movement);
        repository.deleteById(movement.getId());

        Optional<StockMovement> result = repository.findById(movement.getId());

        Assertions.assertTrue(result.isEmpty());
    }

    // ===============================
    // ✅ FIND BY MATERIAL
    // ===============================
    @Test
    public void findByMaterialIdShouldReturnList() {

        Material material = materialRepository.save(Factory.createNewMaterial());
        User user = userRepository.save(Factory.createdUser());
        Employee employee = employeeRepository.save(Factory.createdEmployee());
        MaterialRequest materialRequest = materialRequestRepository.save(Factory.createdMaterialRequest(user, employee));
        StockMovement movement = Factory.createdStockMovement(material, user, employee, materialRequest);
        repository.save(movement);

        List<StockMovement> result = repository.findByMaterialId(material.getId());

        Assertions.assertFalse(result.isEmpty());
    }

    // ===============================
    // ✅ FIND BY MATERIAL REQUEST
    // ===============================
    @Test
    public void findByMaterialRequestIdShouldReturnList() {

        Material material = materialRepository.save(Factory.createNewMaterial());
        User user = userRepository.save(Factory.createdUser());
        Employee employee = employeeRepository.save(Factory.createdEmployee());
        MaterialRequest materialRequest = materialRequestRepository.save(Factory.createdMaterialRequest(user, employee));
        StockMovement movement = Factory.createdStockMovement(material, user, employee, materialRequest);
        repository.save(movement);

        List<StockMovement> result =
                repository.findByMaterialRequestId(materialRequest.getId());

        Assertions.assertFalse(result.isEmpty());
    }

    // ===============================
    // ✅ SEARCH (SEM FILTRO)
    // ===============================
    @Test
    public void searchShouldReturnPageWhenNoFilters() {

        Material material = materialRepository.save(Factory.createNewMaterial());
        User user = userRepository.save(Factory.createdUser());
        Employee employee = employeeRepository.save(Factory.createdEmployee());
        MaterialRequest materialRequest = materialRequestRepository.save(Factory.createdMaterialRequest(user, employee));
        StockMovement movement = Factory.createdStockMovement(material, user, employee, materialRequest);
        repository.save(movement);

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<StockMovement> result = repository.search(
                null,
                null,
                pageRequest
        );

        Assertions.assertFalse(result.isEmpty());
    }

    // ===============================
    // ✅ SEARCH POR NOME DO MATERIAL
    // ===============================
    @Test
    public void searchShouldReturnPageWhenMaterialNameMatches() {

        Material material = materialRepository.save(Factory.createNewMaterial());
        material.setName("Cimento");
        material = materialRepository.save(material);
        User user = userRepository.save(Factory.createdUser());
        Employee employee = employeeRepository.save(Factory.createdEmployee());
        MaterialRequest materialRequest = materialRequestRepository.save(Factory.createdMaterialRequest(user, employee));
        StockMovement movement = Factory.createdStockMovement(material, user, employee, materialRequest);
        repository.save(movement);
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<StockMovement> result = repository.search(
                "cim", // busca parcial
                null,
                pageRequest
        );

        Assertions.assertFalse(result.isEmpty());
    }

    // ===============================
    // ❌ SEARCH NÃO DEVE RETORNAR
    // ===============================
    @Test
    public void searchShouldReturnEmptyWhenNoMatch() {

        Material material = materialRepository.save(Factory.createNewMaterial());
        material.setName("Areia");
        material = materialRepository.save(material);
        User user = userRepository.save(Factory.createdUser());
        Employee employee = employeeRepository.save(Factory.createdEmployee());
        MaterialRequest materialRequest = materialRequestRepository.save(Factory.createdMaterialRequest(user, employee));
        StockMovement movement = Factory.createdStockMovement(material, user, employee, materialRequest);
        repository.save(movement);

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<StockMovement> result = repository.search(
                "cim", // busca parcial
                null,
                pageRequest
        );


        Assertions.assertTrue(result.isEmpty());
    }
}
