package com.jeferson.conspre.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeferson.conspre.dto.ChangePasswordDTO;
import com.jeferson.conspre.dto.CreateUserDTO;
import com.jeferson.conspre.dto.UpdateUserDTO;
import com.jeferson.conspre.dto.UserResponseDTO;
import com.jeferson.conspre.services.UserService;
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

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId = 1L;
    private Long nonExistingId = 1000L;

    // ===============================
    // ✅ FIND ALL
    // ===============================
    @Test
    public void findAllShouldReturnPage() throws Exception {

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(existingId);
        dto.setName("Jeferson");

        Mockito.when(service.findAll(Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingId));
    }

    // ===============================
    // ✅ FIND BY ID
    // ===============================
    @Test
    public void findByIdShouldReturnUser() throws Exception {

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(existingId);
        dto.setName("Jeferson");

        Mockito.when(service.findById(existingId)).thenReturn(dto);

        mockMvc.perform(get("/users/{id}", existingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    public void findByIdShouldReturn404WhenNotFound() throws Exception {

        Mockito.when(service.findById(nonExistingId))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/users/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }

    // ===============================
    // ✅ INSERT
    // ===============================
    @Test
    public void insertShouldReturn201() throws Exception {

        CreateUserDTO dto = new CreateUserDTO();
        dto.setName("Jeferson");
        dto.setLogin("teste@email.com");
        dto.setPassword("123456");

        UserResponseDTO response = new UserResponseDTO();
        response.setId(existingId);
        response.setName("Jeferson");

        Mockito.when(service.insert(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    // ===============================
    // ✅ UPDATE
    // ===============================
    @Test
    public void updateShouldReturn200() throws Exception {

        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setName("Atualizado");

        UserResponseDTO response = new UserResponseDTO();
        response.setId(existingId);
        response.setName("Atualizado");

        Mockito.when(service.update(Mockito.eq(existingId), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(put("/users/{id}", existingId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Atualizado"));
    }

    // ===============================
    // ✅ CHANGE PASSWORD
    // ===============================
    @Test
    public void changePasswordShouldReturn204() throws Exception {

        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setCurrentPassword("123");
        dto.setNewPassword("456");
        dto.setConfirmPassword("456");

        mockMvc.perform(put("/users/{id}/password", existingId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    // ===============================
    // ✅ DELETE
    // ===============================
    @Test
    public void deleteShouldReturn204() throws Exception {

        mockMvc.perform(delete("/users/{id}", existingId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}