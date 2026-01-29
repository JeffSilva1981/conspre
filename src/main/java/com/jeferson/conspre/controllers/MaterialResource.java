package com.jeferson.conspre.controllers;


import com.jeferson.conspre.dto.MaterialDTO;
import com.jeferson.conspre.dto.MaterialMinDTO;
import com.jeferson.conspre.services.MaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/materials")
public class MaterialResource {

    @Autowired
    private MaterialService service;

    @GetMapping
    public ResponseEntity<Page<MaterialMinDTO>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Boolean currentStock,
            Pageable pageable){
        Page<MaterialMinDTO> dto = service.findAll(name, categoryId,ativo, currentStock, pageable);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialDTO> findById(@PathVariable Long id){
        MaterialDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MaterialDTO> insert(@Valid @RequestBody MaterialDTO dto){
        MaterialDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialDTO> update(@PathVariable Long id,@Valid @RequestBody MaterialDTO dto){
        MaterialDTO newDto = service.update(id, dto);
        return ResponseEntity.ok(newDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
