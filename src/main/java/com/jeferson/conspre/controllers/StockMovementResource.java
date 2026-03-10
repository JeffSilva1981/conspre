package com.jeferson.conspre.controllers;

import com.jeferson.conspre.dto.StockMaterialDTO;
import com.jeferson.conspre.dto.StockMovementInputDTO;
import com.jeferson.conspre.dto.StockMovementResponseDTO;
import com.jeferson.conspre.services.StockMovementsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;


@RestController
@RequestMapping("/stock-movements")
public class StockMovementResource {

    @Autowired
    private StockMovementsService service;

    @GetMapping
    public ResponseEntity<Page<StockMovementResponseDTO>> findAllPage(
            @RequestParam(required = false) String materialName,
            @RequestParam(required = false) Instant moment,
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

    @PostMapping("/input")
    public ResponseEntity<StockMovementResponseDTO> createInput(
           @Valid @RequestBody StockMovementInputDTO dto){

        StockMovementResponseDTO result = service.createInputMovement(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/materials/{id}/stock")
    public ResponseEntity<StockMaterialDTO> getMaterialStock(
            @PathVariable Long id) {

        StockMaterialDTO dto = service.getMaterialStock(id);

        return ResponseEntity.ok(dto);
    }
}
