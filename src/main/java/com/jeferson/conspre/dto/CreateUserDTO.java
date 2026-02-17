package com.jeferson.conspre.dto;

import java.util.Set;

public class CreateUserDTO {

    private String name;
    private String login;
    private String password;
    private boolean ativo;
    private Set<Long> roleIds;

    public CreateUserDTO() {
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }
}
