package com.jeferson.conspre.dto;


import com.jeferson.conspre.entity.Category;
import com.jeferson.conspre.entity.Material;
import com.jeferson.conspre.enums.TypeUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


public class MaterialDTO {

    private Long id;

    @NotBlank(message = "Nome do material é obrigatório")
    private String name;

    @NotNull(message = "Unidade de medida é obrigatória")
    private TypeUnit unitOfMeasure;

    @NotNull
    @PositiveOrZero(message = "Estoque atual não pode ser negativo")
    private BigDecimal currentStock;

    @NotNull
    @PositiveOrZero(message = "Estoque mínimo não pode ser negativo")
    private BigDecimal minimumStock;

    @NotNull(message = "Status ativo é obrigatório")
    private Boolean ativo;

    @NotNull(message = "Categoria é obrigatória")
    private Set<CategoryDTO> categories = new HashSet<>();

    public MaterialDTO() {

    }

    public MaterialDTO(Long id, String name, TypeUnit unitOfMeasure, BigDecimal currentStock,
                       BigDecimal minimumStock, Boolean ativo, Set<CategoryDTO> categories) {
        this.id = id;
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.ativo = ativo;
        this.categories = categories;
    }

    public MaterialDTO(Material entity) {
        id = entity.getId();
        name = entity.getName();
        unitOfMeasure = entity.getUnitOfMeasure();
        currentStock = entity.getCurrentStock();
        minimumStock = entity.getMinimumStock();
        ativo = entity.getAtivo();

    }

    public MaterialDTO(Material entity, Set<Category> categories) {
        this(entity);

        categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
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

    public BigDecimal getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }
}
