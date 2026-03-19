package com.jeferson.conspre.dto;

import java.math.BigDecimal;

public class StockMaterialDTO {

    private Long materialId;
    private String materialName;
    private BigDecimal stock;

    public StockMaterialDTO() {
    }

    public StockMaterialDTO(Long materialId, String materialName, BigDecimal stock) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.stock = stock;
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

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }
}
