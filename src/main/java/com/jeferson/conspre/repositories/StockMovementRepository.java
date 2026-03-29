package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByMaterialId(Long materialId);

    List<StockMovement> findByMaterialRequestId(Long requestId);

    @Query("""
            SELECT s FROM StockMovement s
            WHERE (:materialName IS NULL 
                   OR LOWER(s.material.name) LIKE LOWER(CONCAT('%', :materialName, '%')))
            AND (:moment IS NULL 
                   OR s.moment = :moment)
            """)
    Page<StockMovement> search(String materialName, Instant moment, Pageable pageable);

    // ✅ Nova query: todos os movimentos de requisição
    @Query("""
            SELECT s FROM StockMovement s
            WHERE s.materialRequest IS NOT NULL
            ORDER BY s.moment DESC
            """)
    Page<StockMovement> findAllRequests(Pageable pageable);
}