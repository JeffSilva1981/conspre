package com.jeferson.conspre.services;

import com.jeferson.conspre.dto.MaterialRequestCreateDTO;
import com.jeferson.conspre.dto.RequestMaterialItemDTO;
import com.jeferson.conspre.entity.Employee;
import com.jeferson.conspre.entity.Material;
import com.jeferson.conspre.entity.MaterialRequest;
import com.jeferson.conspre.entity.User;
import com.jeferson.conspre.enums.TypeMovement;
import com.jeferson.conspre.repositories.EmployeeRepository;
import com.jeferson.conspre.repositories.MaterialRepository;
import com.jeferson.conspre.repositories.MaterialRequestRepository;
import com.jeferson.conspre.repositories.UserRepository;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class MaterialRequestServiceTests {

    @InjectMocks
    private MaterialRequestService service;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private MaterialRequestRepository materialRequestRepository;

    @Mock
    private StockMovementsService stockService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    private Long existingId;
    private Long nonExistingId;

    private Employee employee;
    private User user;
    private Material material;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;

        employee = new Employee();
        employee.setId(existingId);

        user = new User();
        user.setId(existingId);

        material = new Material();
        material.setId(existingId);
        material.setName("Cimento");

        // mocks básicos
        Mockito.when(employeeRepository.findById(existingId)).thenReturn(Optional.of(employee));
        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));
        Mockito.when(materialRepository.findById(existingId)).thenReturn(Optional.of(material));

        Mockito.when(materialRequestRepository.save(Mockito.any())).thenAnswer(invocation -> {
            MaterialRequest mr = invocation.getArgument(0);
            mr.setId(1L);
            return mr;
        });

        Mockito.when(stockService.calculateStock(existingId))
                .thenReturn(BigDecimal.valueOf(100));
    }

    // ===============================
    // ✅ CREATE
    // ===============================
    @Test
    public void createShouldCreateMaterialRequestWhenValid() {

        RequestMaterialItemDTO itemDTO = new RequestMaterialItemDTO();
        itemDTO.setMaterialId(existingId);
        itemDTO.setQuantity(BigDecimal.valueOf(10));

        MaterialRequestCreateDTO dto = new MaterialRequestCreateDTO();
        dto.setEmployeeId(existingId);
        dto.setUserId(existingId);
        dto.setObservation("Teste");
        dto.setItems(List.of(itemDTO));

        var result = service.create(dto);

        Assertions.assertNotNull(result);
        Mockito.verify(materialRequestRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(stockService, Mockito.times(1))
                .createMovement(
                        Mockito.eq(TypeMovement.OUTPUT),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.anyString(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any()
                );
    }

    // ===============================
    // ❌ EMPLOYEE NOT FOUND
    // ===============================
    @Test
    public void createShouldThrowWhenEmployeeNotFound() {

        Mockito.when(employeeRepository.findById(existingId))
                .thenReturn(Optional.empty());

        MaterialRequestCreateDTO dto = new MaterialRequestCreateDTO();
        dto.setEmployeeId(existingId);
        dto.setUserId(existingId);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.create(dto);
        });
    }

    // ===============================
    // ❌ USER NOT FOUND
    // ===============================
    @Test
    public void createShouldThrowWhenUserNotFound() {

        Mockito.when(userRepository.findById(existingId))
                .thenReturn(Optional.empty());

        MaterialRequestCreateDTO dto = new MaterialRequestCreateDTO();
        dto.setEmployeeId(existingId);
        dto.setUserId(existingId);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.create(dto);
        });
    }

    // ===============================
    // ❌ MATERIAL NOT FOUND
    // ===============================
    @Test
    public void createShouldThrowWhenMaterialNotFound() {

        Mockito.when(materialRepository.findById(existingId))
                .thenReturn(Optional.empty());

        RequestMaterialItemDTO itemDTO = new RequestMaterialItemDTO();
        itemDTO.setMaterialId(existingId);
        itemDTO.setQuantity(BigDecimal.ONE);

        MaterialRequestCreateDTO dto = new MaterialRequestCreateDTO();
        dto.setEmployeeId(existingId);
        dto.setUserId(existingId);
        dto.setItems(List.of(itemDTO));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.create(dto);
        });
    }

    // ===============================
    // ❌ STOCK INSUFFICIENT
    // ===============================
    @Test
    public void createShouldThrowWhenStockIsInsufficient() {

        Mockito.when(stockService.calculateStock(existingId))
                .thenReturn(BigDecimal.valueOf(5));

        RequestMaterialItemDTO itemDTO = new RequestMaterialItemDTO();
        itemDTO.setMaterialId(existingId);
        itemDTO.setQuantity(BigDecimal.valueOf(10));

        MaterialRequestCreateDTO dto = new MaterialRequestCreateDTO();
        dto.setEmployeeId(existingId);
        dto.setUserId(existingId);
        dto.setItems(List.of(itemDTO));

        Assertions.assertThrows(RuntimeException.class, () -> {
            service.create(dto);
        });
    }

    // ===============================
    // ✅ FIND BY ID
    // ===============================
    @Test
    public void findByIdShouldReturnDTOWhenExists() {

        MaterialRequest mr = new MaterialRequest();
        mr.setId(existingId);

        Mockito.when(materialRequestRepository.findById(existingId))
                .thenReturn(Optional.of(mr));

        mr.setEmployee(employee);
        mr.setUser(user);

        var result = service.findById(existingId);

        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowWhenNotExists() {

        Mockito.when(materialRequestRepository.findById(existingId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(existingId);
        });
    }

    // ===============================
    // ✅ DELETE (SOFT DELETE)
    // ===============================
    @Test
    public void deleteShouldSoftDeleteWhenExists() {

        MaterialRequest mr = new MaterialRequest();
        mr.setId(existingId);

        Mockito.when(materialRequestRepository.findById(existingId))
                .thenReturn(Optional.of(mr));

        service.delete(existingId);

        Mockito.verify(materialRequestRepository, Mockito.times(1)).save(mr);
    }

    @Test
    public void deleteShouldThrowWhenNotExists() {

        Mockito.when(materialRequestRepository.findById(existingId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(existingId);
        });
    }
}
