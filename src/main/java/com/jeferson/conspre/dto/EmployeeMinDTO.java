package com.jeferson.conspre.dto;

import com.jeferson.conspre.entity.Employee;

public class EmployeeMinDTO {

    private Long id;
    private String name;
    private String registration;


    private Boolean ativo;

    public EmployeeMinDTO() {

    }

    public EmployeeMinDTO(Long id, String name, String registration, Boolean ativo) {
        this.id = id;
        this.name = name;
        this.registration = registration;
        this.ativo = ativo;
    }

    public EmployeeMinDTO(Employee entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.registration = entity.getRegistration();
        this.ativo = entity.isAtivo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
