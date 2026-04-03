package com.jeferson.conspre.controllers;

import com.jeferson.conspre.dto.MaterialDTO;
import com.jeferson.conspre.dto.MaterialMinDTO;
import com.jeferson.conspre.services.MaterialService;
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
@RequestMapping(value = "/materials")
@Tag(name = "Material", description = "Endpoints for managing materials")
public class MaterialResource {

    @Autowired
    private MaterialService service;

    @Operation(summary = "Retrieve all materials with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materials retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MaterialMinDTO.class)
                    ))
    })
    @GetMapping
    public ResponseEntity<Page<MaterialMinDTO>> findAll(
            @RequestParam(required = false) Boolean currentStock,
            Pageable pageable) {

        Page<MaterialMinDTO> dto = service.findAll(currentStock, pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Retrieve a material by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MaterialDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Material not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<MaterialDTO> findById(@PathVariable Long id) {
        MaterialDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Material created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MaterialDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping
    public ResponseEntity<MaterialDTO> insert(@Valid @RequestBody MaterialDTO dto) {

        MaterialDTO newDto = service.insert(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(newDto);
    }

    @Operation(summary = "Update an existing material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MaterialDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Material not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<MaterialDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MaterialDTO dto) {

        MaterialDTO newDto = service.update(id, dto);
        return ResponseEntity.ok(newDto);
    }

    @Operation(summary = "Delete a material by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Material deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Material not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}