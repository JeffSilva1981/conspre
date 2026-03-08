package com.jeferson.conspre.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class MaterialRequestCreateDTO {

    @NotNull(message = "Id do Funcionário é obrigatório")
    private Long employeeId;

    @NotNull(message = "Id do Usuário é obrigatório")
    private Long userId;

    @NotEmpty(message = "A requisição precisa ter pelo menos um item")
    private List<RequestMaterialItemDTO> items;

    private String observation;

    public MaterialRequestCreateDTO(){

    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<RequestMaterialItemDTO> getItems() {
        return items;
    }

    public void setItems(List<RequestMaterialItemDTO> items) {
        this.items = items;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
