package com.jeferson.conspre.controllers;

import com.jeferson.conspre.dto.EmployeeDTO;
import com.jeferson.conspre.dto.EmployeeMinDTO;
import com.jeferson.conspre.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/employees")
@Tag(name = "Employee", description = "Endpoints for managing employees")
public class EmployeeResource {

    @Autowired
    private EmployeeService service;

    @Operation(summary = "Retrieve all employees with optional name filter and pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeeMinDTO.class)
                    ))
    })
    @GetMapping
    public ResponseEntity<Page<EmployeeMinDTO>> findAll(
            @RequestParam(required = false) String name,
            Pageable pageable) {

        Page<EmployeeMinDTO> result = service.findAll(name, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Retrieve an employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeeDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable Long id) {
        EmployeeDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeeDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping
    public ResponseEntity<EmployeeDTO> insert(@Valid @RequestBody EmployeeDTO dto) {

        EmployeeDTO newDto = service.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(newDto);
    }

    @Operation(summary = "Update an existing employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeeDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<EmployeeDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO dto) {

        EmployeeDTO newDto = service.update(id, dto);
        return ResponseEntity.ok(newDto);
    }

    @Operation(summary = "Delete an employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}