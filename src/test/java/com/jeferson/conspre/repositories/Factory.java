package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.Category;
import com.jeferson.conspre.entity.Material;
import com.jeferson.conspre.enums.TypeUnit;

import java.math.BigDecimal;

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
}
