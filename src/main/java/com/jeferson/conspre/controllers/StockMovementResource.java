package com.jeferson.conspre.controllers;

import com.jeferson.conspre.dto.StockMovementResponseDTO;
import com.jeferson.conspre.services.StockMovementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;

@RestController
@RequestMapping("/stock-movements")
public class StockMovementResource {

    @Autowired
    private StockMovementsService service;

    @GetMapping
    public ResponseEntity<Page<StockMovementResponseDTO>> findAllPage(
            @RequestParam(required = false) String materialName,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Instant moment,
            Pageable pageable
            ) {

        Page<StockMovementResponseDTO> result = service.findAll(materialName, moment, pageable);

        return ResponseEntity.ok(result);

    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovementResponseDTO> findById(@PathVariable Long id){
        StockMovementResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }
}
