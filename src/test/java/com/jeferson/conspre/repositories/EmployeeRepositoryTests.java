package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalEmployees;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;
        countTotalEmployees = 4L;

    }

    @Test
    public void findAllShouldReturnPage() {

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Employee> result = repository.findAll(pageRequest);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

    }

    @Test
    public void findByIdShouldReturnEmployeeWhenIdExists() {

        Optional<Employee> result = repository.findById(existingId);

        Assertions.assertTrue(result.isPresent());

    }

    @Test
    public void findByIdShouldReturnEmptyWhenIdDoesNotExist() {

        Optional<Employee> result = repository.findById(nonExistingId);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {

        Employee employee = Factory.createdEmployee();
        employee.setId(null);
        employee = repository.save(employee);
        Assertions.assertNotNull(employee.getId());

    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        repository.deleteById(existingId);
        Optional<Employee> result = repository.findById(existingId);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    public void deleteShouldThrowExceptionWhenIdDoesNotExist() {

        Assertions.assertDoesNotThrow(() -> {
            repository.deleteById(nonExistingId);
        });

    }
}
