package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByMaterialId(Long materialId);

    List<StockMovement> findByMaterialRequestId(Long requestId);

}