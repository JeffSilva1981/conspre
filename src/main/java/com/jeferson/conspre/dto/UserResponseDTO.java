package com.jeferson.conspre.dto;

import com.jeferson.conspre.entity.User;

public class UserResponseDTO {

    private Long id;
    private String username; // login do usuário
    private String role;     // perfil do usuário

    public UserResponseDTO() {
    }

    public UserResponseDTO(User entity) {
        this.id = entity.getId();
        this.username = entity.getLogin();
        // Pega a primeira role do usuário (se tiver mais de uma)
        this.role = entity.getRoles().stream()
                .map(r -> r.getAuthority()) // seu Role implementa GrantedAuthority
                .findFirst()
                .orElse("USER"); // caso o usuário não tenha role, valor padrão
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}