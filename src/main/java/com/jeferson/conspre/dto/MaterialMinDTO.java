package com.jeferson.conspre.dto;

import com.jeferson.conspre.entity.Material;
import com.jeferson.conspre.enums.TypeUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class MaterialMinDTO {

    private Long id;

    @NotBlank(message = "Nome do material é obrigatório")
    private String name;

    @NotNull(message = "Unidade de medida é obrigatória")
    private TypeUnit unitOfMeasure;

    @NotNull
    @PositiveOrZero(message = "Estoque atual não pode ser negativo")
    private BigDecimal currentStock;

    @NotNull(message = "Status ativo é obrigatório")
    private Boolean ativo;

    @NotNull(message = "Categoria é obrigatória")
    private Long categoryId;

    public MaterialMinDTO(){

    }

    public MaterialMinDTO(Long id, String name, TypeUnit unitOfMeasure,
                          BigDecimal currentStock, Boolean ativo, Long categoryId) {
        this.id = id;
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
        this.currentStock = currentStock;
        this.ativo = ativo;
        this.categoryId = categoryId;
    }

    public MaterialMinDTO(Material entity) {
        id = entity.getId();
        name = entity.getName();
        unitOfMeasure = entity.getUnitOfMeasure();
        currentStock = entity.getCurrentStock();
        ativo = entity.isAtivo();

        if (entity.getCategory() != null) {
            categoryId = entity.getCategory().getId();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeUnit getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(TypeUnit unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
