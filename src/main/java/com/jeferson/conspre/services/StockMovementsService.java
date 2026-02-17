package com.jeferson.conspre.services;

import com.jeferson.conspre.dto.StockMovementResponseDTO;
import com.jeferson.conspre.entity.StockMovement;
import com.jeferson.conspre.repositories.StockMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class StockMovementsService {

    @Autowired
    private StockMovementRepository repository;


    @Transactional(readOnly = true)
    public Page<StockMovementResponseDTO> findAll(String materialName, LocalDate date, Pageable pageable) {
        Page<StockMovement> page = repository.search(materialName, date, pageable);

        return page.map((x-> new StockMovementResponseDTO(x)));
    }
}
