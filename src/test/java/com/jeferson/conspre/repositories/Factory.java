package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.*;
import com.jeferson.conspre.enums.TypeMovement;
import com.jeferson.conspre.enums.TypeUnit;

import java.math.BigDecimal;
import java.time.Instant;

public class Factory {


    public static Material createNewMaterial() {

        Material material = new Material();
        material.setName("Eletrodo");
        material.setUnitOfMeasure(TypeUnit.KG);
        material.setCurrentStock(BigDecimal.valueOf(10));
        material.setMinimumStock(BigDecimal.valueOf(2));
        material.setAtivo(true);

        Category category = new Category();
        category.setId(3L);
        category.setName("Eletrodo");
        category.setDescription("Material para solda");
        category.setAtivo(true);

        material.getCategories().add(category);

        return material;
    }

    public static Category createdCategory() {

        Category category = new Category();
        category.setName("Especial");
        category.setDescription("Material para obra especifica");
        category.setAtivo(true);

        return category;
    }

    public static Employee createdEmployee() {

        Employee employee = new Employee();
        employee.setName("Jeferson Ferreira da silva");
        employee.setRegistration("Lider de Producção");
        employee.setAtivo(true);

        return employee;
    }

    public static User createdUser() {

        User user = new User();
        user.setName("Novo Usuário");
        user.setLogin("novoLogin");
        user.setPassword("123456");
        user.setAtivo(true);

        return user;
    }

    public static MaterialRequest createdMaterialRequest(User user, Employee employee) {

        MaterialRequest request = new MaterialRequest();
        request.setObservation("Teste de requisição");
        request.setUser(user);
        request.setEmployee(employee);

        return request;
    }

    public static StockMovement createdStockMovement(Material material, User user,
                                                     Employee employee, MaterialRequest request) {

        StockMovement stockMovement = new StockMovement();
        stockMovement.setType(TypeMovement.INPUT);
        stockMovement.setMoment(Instant.now());
        stockMovement.setQuantity(BigDecimal.valueOf(1));
        stockMovement.setObservation("test");
        stockMovement.setMaterial(material);
        stockMovement.setUser(user);
        stockMovement.setEmployee(employee);
        stockMovement.setMaterialRequest(request);

        return stockMovement;


    }
}
