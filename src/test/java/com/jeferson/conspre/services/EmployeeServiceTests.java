package com.jeferson.conspre.services;

import com.jeferson.conspre.dto.EmployeeDTO;
import com.jeferson.conspre.dto.EmployeeMinDTO;
import com.jeferson.conspre.entity.Employee;
import com.jeferson.conspre.repositories.EmployeeRepository;
import com.jeferson.conspre.services.exceptions.DatabaseException;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTests {

    @InjectMocks
    private EmployeeService service;

    @Mock
    private EmployeeRepository repository;

    private Long existingId;
    private Long nonExistingId;

    private EmployeeMinDTO employeeMinDTO;

    private Employee employee;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;

        employee = new Employee();
        employee.setId(existingId);
        employee.setName("Kauan");
        employee.setRegistration("Lider");
        employee.setAtivo(true);

        // findById
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(employee));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // existsByName
        Mockito.when(repository.existsByNameIgnoreCaseAndAtivoTrue("Kauan")).thenReturn(true);
        Mockito.when(repository.existsByNameIgnoreCaseAndAtivoTrue("Novo")).thenReturn(false);

        Mockito.when(repository.existsByNameIgnoreCaseAndAtivoTrueAndIdNot("Outro", existingId)).thenReturn(false);
        Mockito.when(repository.existsByNameIgnoreCaseAndAtivoTrueAndIdNot("Kauan", existingId)).thenReturn(true);

        // save
        Mockito.when(repository.save(Mockito.any())).thenReturn(employee);

        // search (Page)
        Page<Employee> page = new PageImpl<>(List.of(employee));
        Mockito.when(repository.search(Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(page.map(EmployeeMinDTO::new));
    }

    // ===============================
    // ✅ findAll
    // ===============================
    @Test
    public void findAllShouldReturnPage() {

        PageRequest pageRequest = PageRequest.of(0, 10);

        var result = service.findAll("", pageRequest);

        Assertions.assertFalse(result.isEmpty());
    }

    // ===============================
    // ✅ findById
    // ===============================
    @Test
    public void findByIdShouldReturnDTOWhenIdExists() {

        var result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Kauan", result.getName());
    }

    @Test
    public void findByIdShouldThrowWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    // ===============================
    // ✅ insert
    // ===============================
    @Test
    public void insertShouldReturnDTOWhenValid() {

        EmployeeDTO dto = new EmployeeDTO();
        dto.setName("Novo");
        dto.setRegistration("Operador");

        var result = service.insert(dto);

        Assertions.assertNotNull(result);
    }

    @Test
    public void insertShouldThrowWhenNameExists() {

        EmployeeDTO dto = new EmployeeDTO();
        dto.setName("Kauan");

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.insert(dto);
        });
    }

    // ===============================
    // ✅ update
    // ===============================
    @Test
    public void updateShouldReturnDTOWhenValid() {

        EmployeeDTO dto = new EmployeeDTO();
        dto.setName("Outro");
        dto.setRegistration("Supervisor");

        var result = service.update(existingId, dto);

        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowWhenIdDoesNotExist() {

        EmployeeDTO dto = new EmployeeDTO();

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, dto);
        });
    }

    @Test
    public void updateShouldThrowWhenNameExists() {

        EmployeeDTO dto = new EmployeeDTO();
        dto.setName("Kauan");

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.update(existingId, dto);
        });
    }

    // ===============================
    // ✅ delete
    // ===============================
    @Test
    public void deleteShouldSoftDeleteWhenIdExists() {

        service.delete(existingId);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void deleteShouldThrowWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }
}
