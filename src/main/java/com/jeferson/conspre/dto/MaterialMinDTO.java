package com.jeferson.conspre.dto;

import com.jeferson.conspre.enums.TypeUnit;

import java.math.BigDecimal;

public class MaterialMinDTO {

    private Long id;
    private String name;
    private TypeUnit unitOfMeasure;
    private BigDecimal currentStock;
    private Boolean ativo;

    // Construtor necessário para JPQL
    public MaterialMinDTO(Long id, String name, TypeUnit unitOfMeasure, BigDecimal currentStock, Boolean ativo) {
        this.id = id;
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
        this.currentStock = currentStock;
        this.ativo = ativo;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TypeUnit getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnitOfMeasure(TypeUnit unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}