package com.jeferson.conspre.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class RequestMaterialItemDTO {

    @NotNull(message = "MaterialId é obrigatório")
    private Long materialId;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    private BigDecimal quantity;

    public RequestMaterialItemDTO() {

    }

    public RequestMaterialItemDTO(Long materialId, BigDecimal quantity) {
        this.materialId = materialId;
        this.quantity = quantity;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
