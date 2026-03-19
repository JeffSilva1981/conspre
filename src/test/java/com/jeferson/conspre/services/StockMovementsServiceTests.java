package com.jeferson.conspre.services;

import com.jeferson.conspre.dto.StockMovementInputDTO;
import com.jeferson.conspre.entity.Material;
import com.jeferson.conspre.entity.StockMovement;
import com.jeferson.conspre.entity.User;
import com.jeferson.conspre.enums.TypeMovement;
import com.jeferson.conspre.repositories.MaterialRepository;
import com.jeferson.conspre.repositories.StockMovementRepository;
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
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class StockMovementsServiceTests {

    @InjectMocks
    private StockMovementsService service;

    @Mock
    private StockMovementRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MaterialRepository materialRepository;

    private Long existingId;
    private Long nonExistingId;

    private Material material;
    private User user;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;

        material = new Material();
        material.setId(existingId);
        material.setName("Cimento");

        user = new User();
        user.setId(existingId);

        // findById
        Mockito.when(repository.findById(existingId))
                .thenReturn(Optional.of(createValidStockMovement()));

        Mockito.when(repository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        // material
        Mockito.when(materialRepository.findById(existingId))
                .thenReturn(Optional.of(material));

        Mockito.when(materialRepository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        // user
        Mockito.when(userRepository.findById(existingId))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        // save
        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    // ===============================
    // 🔥 HELPER (PADRÃO PROFISSIONAL)
    // ===============================
    private StockMovement createValidStockMovement() {
        StockMovement sm = new StockMovement();
        sm.setId(existingId);
        sm.setMaterial(material);
        sm.setUser(user);
        sm.setQuantity(BigDecimal.TEN);
        sm.setType(TypeMovement.INPUT);
        sm.setMoment(Instant.now());
        return sm;
    }

    // ===============================
    // ✅ findById
    // ===============================
    @Test
    public void findByIdShouldReturnDTOWhenExists() {

        var result = service.findById(existingId);

        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowWhenNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    // ===============================
    // ✅ createMovement
    // ===============================
    @Test
    public void createMovementShouldSaveMovement() {

        StockMovement result = service.createMovement(
                TypeMovement.INPUT,
                material,
                BigDecimal.TEN,
                "teste",
                user,
                null,
                null
        );

        Assertions.assertNotNull(result);
        Mockito.verify(repository).save(Mockito.any());
    }

    // ===============================
    // ✅ createInputMovement
    // ===============================
    @Test
    public void createInputMovementShouldCreateWhenValid() {

        StockMovementInputDTO dto = new StockMovementInputDTO();
        dto.setMaterialId(existingId);
        dto.setUserId(existingId);
        dto.setQuantity(BigDecimal.TEN);
        dto.setObservation("entrada");

        var result = service.createInputMovement(dto);

        Assertions.assertNotNull(result);
        Mockito.verify(repository).save(Mockito.any());
    }

    @Test
    public void createInputMovementShouldThrowWhenMaterialNotFound() {

        StockMovementInputDTO dto = new StockMovementInputDTO();
        dto.setMaterialId(nonExistingId);
        dto.setUserId(existingId);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.createInputMovement(dto);
        });
    }

    @Test
    public void createInputMovementShouldThrowWhenUserNotFound() {

        StockMovementInputDTO dto = new StockMovementInputDTO();
        dto.setMaterialId(existingId);
        dto.setUserId(nonExistingId);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.createInputMovement(dto);
        });
    }

    // ===============================
    // ✅ calculateStock
    // ===============================
    @Test
    public void calculateStockShouldReturnCorrectValue() {

        StockMovement m1 = new StockMovement();
        m1.setType(TypeMovement.INPUT);
        m1.setQuantity(BigDecimal.valueOf(10));

        StockMovement m2 = new StockMovement();
        m2.setType(TypeMovement.OUTPUT);
        m2.setQuantity(BigDecimal.valueOf(4));

        Mockito.when(repository.findByMaterialId(existingId))
                .thenReturn(List.of(m1, m2));

        BigDecimal result = service.calculateStock(existingId);

        Assertions.assertEquals(BigDecimal.valueOf(6), result);
    }

    // ===============================
    // ✅ getMaterialStock
    // ===============================
    @Test
    public void getMaterialStockShouldReturnDTO() {

        Mockito.when(repository.findByMaterialId(existingId))
                .thenReturn(List.of());

        var result = service.getMaterialStock(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Cimento", result.getMaterialName());
    }

    @Test
    public void getMaterialStockShouldThrowWhenMaterialNotFound() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.getMaterialStock(nonExistingId);
        });
    }

    // ===============================
    // ✅ findByRequest
    // ===============================
    @Test
    public void findByRequestShouldReturnList() {

        Mockito.when(repository.findByMaterialRequestId(existingId))
                .thenReturn(List.of(createValidStockMovement()));

        var result = service.findByRequest(existingId);

        Assertions.assertFalse(result.isEmpty());
    }
}