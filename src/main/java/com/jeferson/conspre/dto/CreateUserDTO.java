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

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }
}
