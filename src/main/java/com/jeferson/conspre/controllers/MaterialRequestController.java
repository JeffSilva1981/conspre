package com.jeferson.conspre.controllers;

import com.jeferson.conspre.dto.MaterialRequestCreateDTO;
import com.jeferson.conspre.dto.MaterialRequestResponseDTO;
import com.jeferson.conspre.services.MaterialRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/material-requests")
public class MaterialRequestController {

    @Autowired
    private MaterialRequestService service;

    @GetMapping
    public ResponseEntity<Page<MaterialRequestResponseDTO>> findAll(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Instant dateMin,
            @RequestParam(required = false) Instant dateMax,
            @RequestParam(required = false) String observation,
            Pageable pageable){

        Page<MaterialRequestResponseDTO> response = service.findAll(employeeId, ativo, dateMin, dateMax, observation, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialRequestResponseDTO> findById(@PathVariable Long id){

        MaterialRequestResponseDTO result = service.findById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<MaterialRequestResponseDTO> create(@Valid @RequestBody MaterialRequestCreateDTO dto){

        MaterialRequestResponseDTO result = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
