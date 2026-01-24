package com.jeferson.conspre.entity;


import com.jeferson.conspre.enums.Status;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_material_request")
public class MaterialRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant moment;
    private Status statusRequest;
    private String observation;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToMany(mappedBy = "materialRequest")
    private Set<RequestMaterialItem> requestMaterialItems = new HashSet<>();

    public MaterialRequest(){

    }

    public MaterialRequest(Long id, Instant moment, Status statusRequest, String observation, User user, Employee employee) {
        this.id = id;
        this.moment = moment;
        this.statusRequest = statusRequest;
        this.observation = observation;
        this.user = user;
        this.employee = employee;
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
