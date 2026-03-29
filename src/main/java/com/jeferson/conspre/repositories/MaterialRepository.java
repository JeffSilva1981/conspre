package com.jeferson.conspre.repositories;

import com.jeferson.conspre.dto.MaterialMinDTO;
import com.jeferson.conspre.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    boolean existsByNameIgnoreCaseAndAtivoTrue(String name);

    boolean existsByNameIgnoreCaseAndAtivoTrueAndIdNot(String name, Long id);

    @Query("""
                SELECT new com.jeferson.conspre.dto.MaterialMinDTO(
                    m.id,
                    m.name,
                    m.unitOfMeasure,
                    m.currentStock,
                    m.ativo
                )
                FROM Material m
                WHERE (:currentStock IS NULL
                    OR (:currentStock = true AND m.currentStock > 0)
                    OR (:currentStock = false AND m.currentStock <= 0))
                ORDER BY m.name
            """)
    Page<MaterialMinDTO> searchByStock(@Param("currentStock") Boolean currentStock, Pageable pageable);
}
