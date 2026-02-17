package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {


    List<StockMovement> findByMaterial(Long materialId);

    List<StockMovement> findByMaterialRequestId(Long requestId);


    @Query("""
           SELECT s FROM StockMovement s
           WHERE (:materialName IS NULL 
                  OR LOWER(s.material.name) LIKE LOWER(CONCAT('%', :materialName, '%')))
           AND (:date IS NULL 
                  OR s.date = :date)
           """)
    Page<StockMovement> search(String materialName, LocalDate date, Pageable pageable);
}
