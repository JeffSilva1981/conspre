package com.jeferson.conspre.entity;

import com.jeferson.conspre.enums.TypeMovement;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "tb_stock_movement")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMovement type;

    @Column(nullable = false)
    private Instant date;


    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;

    @Column(length = 500)
    private String observation;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private MaterialRequest materialRequest;

    @PrePersist
    public void prePersist() {
        this.date = Instant.now();
    }

    public StockMovement(){

    }

    public StockMovement(Long id, TypeMovement type, Instant date, BigDecimal quantity, String observation, Material material,
                         User user, Employee employee, MaterialRequest materialRequest) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.quantity = quantity;
        this.observation = observation;
        this.material = material;
        this.user = user;
        this.employee = employee;
        this.materialRequest = materialRequest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeMovement getType() {
        return type;
    }

    public void setType(TypeMovement type) {
        this.type = type;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public MaterialRequest getMaterialRequest() {
        return materialRequest;
    }

    public void setMaterialRequest(MaterialRequest materialRequest) {
        this.materialRequest = materialRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockMovement that = (StockMovement) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
