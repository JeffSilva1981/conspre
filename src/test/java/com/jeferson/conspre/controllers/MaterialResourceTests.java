package com.jeferson.conspre.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeferson.conspre.dto.MaterialDTO;
import com.jeferson.conspre.dto.MaterialMinDTO;
import com.jeferson.conspre.enums.TypeUnit;
import com.jeferson.conspre.services.MaterialService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
public class MaterialResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MaterialService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId = 1L;
    private Long nonExistingId = 1000L;

    // ===============================
    // ✅ FIND ALL
    // ===============================
    @Test
    public void findAllShouldReturnPage() throws Exception {

        MaterialMinDTO dto = new MaterialMinDTO();
        dto.setId(existingId);
        dto.setName("Material Teste");

        Mockito.when(service.findAll(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()
        )).thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/materials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingId))
                .andExpect(jsonPath("$.content[0].name").value("Material Teste"));
    }

    // ===============================
    // ✅ FIND BY ID
    // ===============================
    @Test
    public void findByIdShouldReturnObject() throws Exception {

        MaterialDTO dto = new MaterialDTO();
        dto.setId(existingId);
        dto.setName("Material Teste");

        Mockito.when(service.findById(existingId)).thenReturn(dto);

        mockMvc.perform(get("/materials/{id}", existingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value("Material Teste"));
    }

    @Test
    public void findByIdShouldReturn404WhenNotFound() throws Exception {

        Mockito.when(service.findById(nonExistingId))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/materials/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }

    // ===============================
    // ✅ INSERT
    // ===============================
    @Test
    public void insertShouldReturn201() throws Exception {

        MaterialDTO dto = new MaterialDTO();
        dto.setName("Novo Material");
        dto.setUnitOfMeasure(TypeUnit.KG);     // ✅ obrigatório
        dto.setCurrentStock(BigDecimal.valueOf(10));      // ✅ obrigatório
        dto.setMinimumStock(BigDecimal.valueOf(10));       // ✅ obrigatório
        dto.setAtivo(true);             // ✅ obrigatório

        MaterialDTO response = new MaterialDTO();
        response.setId(existingId);
        response.setName("Novo Material");
        response.setUnitOfMeasure(TypeUnit.KG);
        response.setCurrentStock(BigDecimal.valueOf(10));
        response.setMinimumStock(BigDecimal.valueOf(10));
        response.setAtivo(true);

        Mockito.when(service.insert(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/materials")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(existingId));
    }

    // ===============================
    // ✅ UPDATE
    // ===============================
    @Test
    public void updateShouldReturn200() throws Exception {

        MaterialDTO dto = new MaterialDTO();
        dto.setName("Atualizado");
        dto.setUnitOfMeasure(TypeUnit.KG);     // ✅ obrigatório
        dto.setCurrentStock(BigDecimal.valueOf(10));      // ✅ obrigatório
        dto.setMinimumStock(BigDecimal.valueOf(10));       // ✅ obrigatório
        dto.setAtivo(true);             // ✅ obrigatório

        MaterialDTO response = new MaterialDTO();
        response.setId(existingId);
        response.setName("Atualizado");
        response.setUnitOfMeasure(TypeUnit.KG);
        response.setCurrentStock(BigDecimal.valueOf(10));
        response.setMinimumStock(BigDecimal.valueOf(10));
        response.setAtivo(true);

        Mockito.when(service.update(Mockito.eq(existingId), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(put("/materials/{id}", existingId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Atualizado"));
    }

    // ===============================
    // ✅ DELETE
    // ===============================
    @Test
    public void deleteShouldReturn204() throws Exception {

        mockMvc.perform(delete("/materials/{id}", existingId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}