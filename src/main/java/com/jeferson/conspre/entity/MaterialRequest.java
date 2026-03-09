package com.jeferson.conspre.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_material_request")
public class MaterialRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant moment;

    private String observation;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private Boolean ativo = true;

    @OneToMany(mappedBy = "materialRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<RequestMaterialItem> requestMaterialItems = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.moment = Instant.now();
    }

    public MaterialRequest(){
        this.ativo = true;
    }

    public MaterialRequest(Long id, Instant moment, String observation, User user, Employee employee, boolean ativo) {
        this.id = id;
        this.moment = moment;
        this.observation = observation;
        this.user = user;
        this.employee = employee;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
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

    public List<RequestMaterialItem> getRequestMaterialItems() {
        return requestMaterialItems;
    }

    public void addItem(RequestMaterialItem item) {
        requestMaterialItems.add(item);
        item.setMaterialRequest(this);
    }

    public void removeItem(RequestMaterialItem item) {
        requestMaterialItems.remove(item);
        item.setMaterialRequest(null);
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void addItem(Material material, BigDecimal quantity) {

        RequestMaterialItem item = new RequestMaterialItem();
        item.setMaterial(material);
        item.setQuantity(quantity);

        // 🔹 importante: setar o lado ManyToOne
        item.setMaterialRequest(this);

        // 🔹 adicionar na lista
        this.requestMaterialItems.add(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaterialRequest that = (MaterialRequest) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
