package com.jeferson.conspre.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeferson.conspre.dto.MaterialRequestCreateDTO;
import com.jeferson.conspre.dto.MaterialRequestResponseDTO;
import com.jeferson.conspre.dto.RequestMaterialItemDTO;
import com.jeferson.conspre.services.MaterialRequestService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
public class MaterialRequestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MaterialRequestService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId = 1L;
    private Long nonExistingId = 1000L;

    // ===============================
    // ✅ FIND ALL
    // ===============================
    @Test
    public void findAllShouldReturnPage() throws Exception {

        MaterialRequestResponseDTO dto = new MaterialRequestResponseDTO();
        dto.setId(existingId);

        Mockito.when(service.findAll(
                Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any()
        )).thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/material-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingId));
    }

    // ===============================
    // ✅ FIND BY ID
    // ===============================
    @Test
    public void findByIdShouldReturnObject() throws Exception {

        MaterialRequestResponseDTO dto = new MaterialRequestResponseDTO();
        dto.setId(existingId);

        Mockito.when(service.findById(existingId)).thenReturn(dto);

        mockMvc.perform(get("/material-requests/{id}", existingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    public void findByIdShouldReturn404WhenNotFound() throws Exception {

        Mockito.when(service.findById(nonExistingId))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/material-requests/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }

    // ===============================
    // ✅ CREATE
    // ===============================
    @Test
    public void createShouldReturn201() throws Exception {

        MaterialRequestCreateDTO dto = new MaterialRequestCreateDTO();
        dto.setEmployeeId(1L);
        dto.setUserId(1L);
        dto.setObservation("Teste");

        // ✅ ITEM CORRETO
        RequestMaterialItemDTO item = new RequestMaterialItemDTO();
        item.setMaterialId(1L);
        item.setQuantity(BigDecimal.valueOf(5));

        dto.setItems(List.of(item));

        MaterialRequestResponseDTO response = new MaterialRequestResponseDTO();
        response.setId(existingId);

        Mockito.when(service.create(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/material-requests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(existingId));
    }

    // ===============================
    // ✅ DELETE
    // ===============================
    @Test
    public void deleteShouldReturn204() throws Exception {

        Mockito.doNothing().when(service).delete(existingId);

        mockMvc.perform(delete("/material-requests/{id}", existingId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturn404WhenNotFound() throws Exception {

        Mockito.doThrow(new ResourceNotFoundException("Not found"))
                .when(service).delete(nonExistingId);

        mockMvc.perform(delete("/material-requests/{id}", nonExistingId)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}