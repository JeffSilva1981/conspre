package com.jeferson.conspre.dto;

import com.jeferson.conspre.entity.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "O nome da categoria é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotBlank
    @Size(min = 1, max = 60)
    private String registration;


    private Boolean ativo;

    public EmployeeDTO(){

    }

    public EmployeeDTO(Long id, String name, String registration, Boolean ativo) {
        this.id = id;
        this.name = name;
        this.registration = registration;
        this.ativo = ativo;
    }

    public EmployeeDTO(Employee entity) {
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
