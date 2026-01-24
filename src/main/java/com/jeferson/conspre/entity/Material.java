package com.jeferson.conspre.entity;

import com.jeferson.conspre.enums.TypeUnit;
import jakarta.persistence.*;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private TypeUnit unitOfMeasure;
    private DecimalFormat currentStock;
    private DecimalFormat minimumStock;
    private boolean ativo;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categories;

    @OneToMany(mappedBy = "material")
    private Set<StockMovement> stockMovements = new HashSet<>();

    public Material(){

    }

    public Material(Long id, String name, TypeUnit unitOfMeasure, DecimalFormat currentStock, DecimalFormat minimumStock,
                    boolean ativo, Category categories) {
        this.id = id;
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.ativo = ativo;
        this.categories = categories;
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

    public DecimalFormat getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(DecimalFormat currentStock) {
        this.currentStock = currentStock;
    }

    public DecimalFormat getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(DecimalFormat minimumStock) {
        this.minimumStock = minimumStock;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Category getCategories() {
        return categories;
    }

    public void setCategories(Category categories) {
        this.categories = categories;
    }

    public Set<StockMovement> getStockMovements() {
        return stockMovements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Material material = (Material) o;
        return id.equals(material.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
