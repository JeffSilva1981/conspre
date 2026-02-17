package com.jeferson.conspre.dto;

import java.util.Set;

public class UpdateUserDTO {

    private String name;
    private String login;
    private boolean ativo;
    private Set<Long> roleIds;

    public UpdateUserDTO() {
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

    public Set<Long> getRoleIds() {
        return roleIds;
    }
}
