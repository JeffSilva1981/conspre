package com.jeferson.conspre.dto;

import java.math.BigDecimal;

public class RequestMaterialItemResponseDTO {

    private Long materialId;
    private String materialName;
    private BigDecimal quantity;

    public RequestMaterialItemResponseDTO() {
    }

    public RequestMaterialItemResponseDTO(Long materialId, String materialName, BigDecimal quantity) {
        this.materialId = materialId;
        this.materialName = materialName;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
