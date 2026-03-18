package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.Employee;
import com.jeferson.conspre.entity.MaterialRequest;
import com.jeferson.conspre.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@DataJpaTest
public class MaterialRequestRepositoryTests {

    @Autowired
    private MaterialRequestRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Long existingId;
    private Long nonExistingId;

    private User user;
    private Employee employee;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;

        user = userRepository.findById(1L).get();
        employee = employeeRepository.findById(1L).get();
    }

    // =========================
    // FIND ALL
    // =========================

    @Test
    public void findAllShouldReturnPage() {

        User user = userRepository.save(Factory.createdUser());
        Employee employee = employeeRepository.save(Factory.createdEmployee());

        MaterialRequest materialRequest = Factory.createdMaterialRequest(user, employee);
        materialRequest = repository.save(materialRequest);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MaterialRequest> result = repository.search(employee.getId(), true,
                null, null, "Teste de requisição", pageRequest);

        Assertions.assertFalse(result.isEmpty());
    }

    // =========================
    // FIND BY ID
    // =========================

    @Test
    public void findByIdShouldReturnMaterialRequestWhenIdExists() {

        User user = userRepository.save(Factory.createdUser());
        Employee employee = employeeRepository.save(Factory.createdEmployee());
        MaterialRequest materialRequest = Factory.createdMaterialRequest(user, employee);
        materialRequest = repository.save(materialRequest);
        Optional<MaterialRequest> result = repository.findById(materialRequest.getId());
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyWhenIdDoesNotExist() {

        Optional<MaterialRequest> result = repository.findById(nonExistingId);

        Assertions.assertTrue(result.isEmpty());
    }

    // =========================
    // SAVE
    // =========================

    @Test
    public void saveShouldPersistMaterialRequest() {

        User user = userRepository.save(Factory.createdUser());
        Employee employee = employeeRepository.save(Factory.createdEmployee());
        MaterialRequest request = Factory.createdMaterialRequest(user, employee);
        request = repository.save(request);

        Assertions.assertNotNull(request.getId());
        Assertions.assertNotNull(request.getMoment()); // 🔥 testa o @PrePersist
    }

    // =========================
    // DELETE
    // =========================

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        repository.deleteById(existingId);
        Optional<MaterialRequest> result = repository.findById(existingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void deleteShouldThrowExceptionWhenIdDoesNotExist() {

        Assertions.assertDoesNotThrow(
                () -> {
                    repository.deleteById(nonExistingId);
                });
    }

}
