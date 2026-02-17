package com.jeferson.conspre.dto;

import com.jeferson.conspre.entity.Role;

public class RoleResponseDTO {

    private Long id;
    private String authority;

    public RoleResponseDTO() {
    }

    public RoleResponseDTO(Role entity) {
        this.id = entity.getId();
        this.authority = entity.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }
}
