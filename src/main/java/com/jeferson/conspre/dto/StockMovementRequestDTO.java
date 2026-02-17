package com.jeferson.conspre.dto;

import com.jeferson.conspre.entity.StockMovement;
import com.jeferson.conspre.enums.TypeMovement;

import java.math.BigDecimal;

public class StockMovementRequestDTO {

    private TypeMovement type;
    private BigDecimal quantity;
    private String observation;

    private Long materialId;
    private Long userId;
    private Long employeeId;
    private Long requestId;

    public StockMovementRequestDTO(){

    }

    public StockMovementRequestDTO(StockMovement entity) {

        this.type = entity.getType();
        this.quantity = entity.getQuantity();
        this.observation = entity.getObservation();
        this.materialId = entity.getMaterial().getId();
        this.userId = entity.getUser().getId();
        this.employeeId = entity.getEmployee().getId();
    }

    public TypeMovement getType() {
        return type;
    }

    public void setType(TypeMovement type) {
        this.type = type;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
}
