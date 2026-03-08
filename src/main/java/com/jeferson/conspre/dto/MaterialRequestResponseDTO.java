package com.jeferson.conspre.dto;

import com.jeferson.conspre.entity.MaterialRequest;

import java.time.Instant;
import java.util.List;

public class MaterialRequestResponseDTO {

    private Long id;
    private Instant date;
    private Long employeeId;
    private Long userId;
    private String observation;

    private List<RequestMaterialItemResponseDTO> items;

    public MaterialRequestResponseDTO(){

    }

    public MaterialRequestResponseDTO(Long id, Instant date, Long employeeId, Long userId, String observation, List<RequestMaterialItemResponseDTO> items) {
        this.id = id;
        this.date = date;
        this.employeeId = employeeId;
        this.userId = userId;
        this.observation = observation;
        this.items = items;
    }

    public MaterialRequestResponseDTO(MaterialRequest entity) {
        this.id = entity.getId();
        this.date = entity.getMoment();
        this.employeeId = entity.getEmployee().getId();
        this.userId = entity.getUser().getId();
        this.observation = entity.getObservation();

        this.items = entity.getRequestMaterialItems().stream()
                .map(item -> new RequestMaterialItemResponseDTO(
                        item.getMaterial().getId(),
                        item.getMaterial().getName(),
                        item.getQuantity()
                ))
                .toList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<RequestMaterialItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<RequestMaterialItemResponseDTO> items) {
        this.items = items;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
