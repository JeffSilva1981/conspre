package com.jeferson.conspre.dto;

import java.math.BigDecimal;

public class StockMovementInputDTO {

    private Long materialId;
    private BigDecimal quantity;
    private String observation;
    private Long userId;

    public Long getMaterialId() {
        return materialId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getObservation() {
        return observation;
    }

    public Long getUserId() {
        return userId;
    }

}
