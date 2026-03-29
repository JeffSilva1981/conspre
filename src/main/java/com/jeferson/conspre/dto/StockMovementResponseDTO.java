package com.jeferson.conspre.dto;

import com.jeferson.conspre.entity.StockMovement;
import com.jeferson.conspre.enums.TypeMovement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

public class StockMovementResponseDTO {

    private Long id;
    private TypeMovement type;
    private Instant moment;

    @NotNull
    @Positive
    private BigDecimal quantity;

    private Long materialId;
    private String materialName;
    private Long materialRequestId;
    private Long userId;
    private String userName;

    public StockMovementResponseDTO() {

    }

    public StockMovementResponseDTO(StockMovement entity) {
        this.id = entity.getId();
        this.type = entity.getType();
        this.moment = entity.getMoment();
        this.quantity = entity.getQuantity();
        this.materialId = entity.getMaterial().getId();
        this.materialName = entity.getMaterial().getName();

        if (entity.getMaterialRequest() != null) {
            this.materialRequestId = entity.getMaterialRequest().getId();
        }

        if (entity.getUser() != null) {
            this.userId = entity.getUser().getId();
            this.userName = entity.getUser().getName();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeMovement getType() {
        return type;
    }

    public void setType(TypeMovement type) {
        this.type = type;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getMaterialRequestId() {
        return materialRequestId;
    }

    public void setMaterialRequestId(Long materialRequestId) {
        this.materialRequestId = materialRequestId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}