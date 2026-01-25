package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCaseAndAtivoTrue(String name);

}
