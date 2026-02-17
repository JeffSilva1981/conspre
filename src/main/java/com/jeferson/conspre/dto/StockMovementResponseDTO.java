package com.jeferson.conspre.dto;

import com.jeferson.conspre.entity.StockMovement;
import com.jeferson.conspre.enums.TypeMovement;

import java.math.BigDecimal;
import java.time.Instant;

public class StockMovementResponseDTO {

    private Long id;
    private TypeMovement type;
    private Instant date;
    private BigDecimal quantity;
    private String observation;

    private Long materialId;
    private String materialName;

    public StockMovementResponseDTO() {

    }

    public StockMovementResponseDTO(StockMovement entity) {
        this.id = entity.getId();
        this.type = entity.getType();
        this.date = entity.getDate();
        this.quantity = entity.getQuantity();
        this.observation = entity.getObservation();

        if (entity.getMaterial() != null) {
            this.materialId = entity.getMaterial().getId();
            this.materialName = entity.getMaterial().getName();
        }
    }

    public Long getId() {
        return id;
    }

    public TypeMovement getType() {
        return type;
    }

    public Instant getDate() {
        return date;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getObservation() {
        return observation;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public String getMaterialName() {
        return materialName;
    }
}