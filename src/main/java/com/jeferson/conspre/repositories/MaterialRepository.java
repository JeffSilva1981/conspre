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
                    m.ativo,
                    c.id
                )
                FROM Material m
                LEFT JOIN m.categories c
                WHERE
                    (:ativo IS NULL OR m.ativo = :ativo)
                AND (:name IS NULL OR UPPER(m.name) LIKE UPPER(CONCAT('%', :name, '%')))
                AND (:categoryId IS NULL OR c.id = :categoryId)
                AND (
                    :currentStock IS NULL
                    OR (:currentStock = true AND m.currentStock > 0)
                    OR (:currentStock = false AND m.currentStock <= 0)
                )
            """)
    Page<MaterialMinDTO> search(
            @Param("name") String name,
            @Param("categoryId") Long categoryId,
            @Param("ativo") Boolean ativo,
            @Param("currentStock") Boolean currentStock,
            Pageable pageable
    );


}
