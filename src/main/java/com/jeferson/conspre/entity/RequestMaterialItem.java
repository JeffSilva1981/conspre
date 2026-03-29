package com.jeferson.conspre.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "tb_Request_Material_Item")
public class RequestMaterialItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "material_Request_id")
    private MaterialRequest materialRequest;

    @ManyToOne()
    @JoinColumn(name = "material_id")
    @JsonIgnore
    private Material material;

    public RequestMaterialItem() {

    }

    public RequestMaterialItem(Long id, BigDecimal quantity, MaterialRequest materialRequest, Material material) {
        this.id = id;
        this.quantity = quantity;
        this.materialRequest = materialRequest;
        this.material = material;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public MaterialRequest getMaterialRequest() {
        return materialRequest;
    }

    public void setMaterialRequest(MaterialRequest materialRequest) {
        this.materialRequest = materialRequest;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestMaterialItem)) return false;
        RequestMaterialItem that = (RequestMaterialItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
