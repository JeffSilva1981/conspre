package com.jeferson.conspre.dto;

import java.util.Set;

public class UpdateUserDTO {

    private String name;
    private String login;
    private boolean ativo;
    private Set<String> role;

    public UpdateUserDTO() {
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Set<String> getRole() {
        return role;
    }
}
