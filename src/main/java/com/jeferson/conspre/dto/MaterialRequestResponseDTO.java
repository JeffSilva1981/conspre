package com.jeferson.conspre.dto;

import com.jeferson.conspre.entity.MaterialRequest;

import java.time.Instant;
import java.util.List;

public class MaterialRequestResponseDTO {

    private Long id;
    private Instant date;
    private String employeeName;
    private Long userId;
    private String observation;
    private String userName;

    private List<RequestMaterialItemResponseDTO> items;

    public MaterialRequestResponseDTO() {

    }

    public MaterialRequestResponseDTO(Long id, Instant date, String employeeName, Long userId, String observation, List<RequestMaterialItemResponseDTO> items) {
        this.id = id;
        this.date = date;
        this.employeeName = employeeName;
        this.userId = userId;
        this.observation = observation;
        this.items = items;
    }

    public MaterialRequestResponseDTO(MaterialRequest entity) {
        this.id = entity.getId();
        this.date = entity.getMoment();

        if (entity.getEmployee() != null) {
            this.employeeName = entity.getEmployee().getName();
        }

        if (entity.getUser() != null) {
            this.userId = entity.getUser().getId();
            this.userName = entity.getUser().getName();
        }

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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
