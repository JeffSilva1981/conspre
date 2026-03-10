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

    public String getMaterialName() {
        return materialName;
    }

    public BigDecimal getStock() {
        return stock;
    }
}
