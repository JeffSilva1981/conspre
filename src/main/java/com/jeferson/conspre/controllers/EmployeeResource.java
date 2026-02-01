package com.jeferson.conspre.controllers;

import com.jeferson.conspre.dto.EmployeeDTO;
import com.jeferson.conspre.dto.EmployeeMinDTO;
import com.jeferson.conspre.dto.MaterialMinDTO;
import com.jeferson.conspre.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/employees")
public class EmployeeResource {

    @Autowired
    private EmployeeService service;

    @RequestMapping
    public ResponseEntity<Page<EmployeeMinDTO>> findAll(@RequestParam(required = false) String name, Pageable pageable){
            Page<EmployeeMinDTO> result = service.findAll(name, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<EmployeeDTO> findById(@PathVariable Long id){
        EmployeeDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<EmployeeDTO> insert(@Valid @RequestBody EmployeeDTO dto){
        EmployeeDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.ok(newDto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO dto){
        EmployeeDTO newDto = service.update(id, dto);
        return ResponseEntity.ok(newDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
