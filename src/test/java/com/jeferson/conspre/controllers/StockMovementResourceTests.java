package com.jeferson.conspre.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeferson.conspre.dto.StockMaterialDTO;
import com.jeferson.conspre.dto.StockMovementInputDTO;
import com.jeferson.conspre.dto.StockMovementResponseDTO;
import com.jeferson.conspre.services.StockMovementsService;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
public class StockMovementResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockMovementsService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId = 1L;
    private Long nonExistingId = 1000L;

    // ===============================
    // ✅ FIND ALL
    // ===============================
    @Test
    public void findAllShouldReturnPage() throws Exception {

        StockMovementResponseDTO dto = new StockMovementResponseDTO();
        dto.setId(existingId);

        Mockito.when(service.findAll(
                Mockito.any(), Mockito.any(), Mockito.any()
        )).thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/stock-movements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingId));
    }

    // ===============================
    // ✅ FIND BY ID
    // ===============================
    @Test
    public void findByIdShouldReturnObject() throws Exception {

        StockMovementResponseDTO dto = new StockMovementResponseDTO();
        dto.setId(existingId);

        Mockito.when(service.findById(existingId)).thenReturn(dto);

        mockMvc.perform(get("/stock-movements/{id}", existingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    public void findByIdShouldReturn404WhenNotFound() throws Exception {

        Mockito.when(service.findById(nonExistingId))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/stock-movements/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }

    // ===============================
    // ✅ CREATE INPUT
    // ===============================
    @Test
    public void createInputShouldReturn201() throws Exception {

        StockMovementInputDTO dto = new StockMovementInputDTO();
        dto.setMaterialId(1L);
        dto.setQuantity(BigDecimal.valueOf(10));
        dto.setObservation("Entrada de estoque");

        StockMovementResponseDTO response = new StockMovementResponseDTO();
        response.setId(existingId);

        Mockito.when(service.createInputMovement(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/stock-movements/input")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(existingId));
    }

    // ===============================
    // ✅ GET MATERIAL STOCK
    // ===============================
    @Test
    @WithMockUser
    public void getMaterialStockShouldReturnStock() throws Exception {

        StockMaterialDTO dto = new StockMaterialDTO();
        dto.setMaterialId(existingId);
        dto.setStock(BigDecimal.valueOf(100));

        Mockito.when(service.getMaterialStock(existingId)).thenReturn(dto);

        mockMvc.perform(get("/stock-movements/materials/{id}/stock", existingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.materialId").value(existingId))
                .andExpect(jsonPath("$.stock").value(100));
    }

    @Test
    public void getMaterialStockShouldReturn404WhenNotFound() throws Exception {

        Mockito.when(service.getMaterialStock(nonExistingId))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/stock-movements/materials/{id}/stock", nonExistingId))
                .andExpect(status().isNotFound());
    }
}