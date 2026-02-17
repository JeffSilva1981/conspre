package com.jeferson.conspre.dto;

import com.jeferson.conspre.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String login;
    private boolean ativo;
    private Set<RoleResponseDTO> roles;

    public UserResponseDTO() {
    }

    public UserResponseDTO(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.login = entity.getLogin();
        this.ativo = entity.isAtivo();
        this.roles = entity.getRoles()
                .stream()
                .map(RoleResponseDTO::new)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Set<RoleResponseDTO> getRoles() {
        return roles;
    }
}
