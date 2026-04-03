package com.jeferson.conspre.controllers;

import com.jeferson.conspre.dto.MaterialRequestCreateDTO;
import com.jeferson.conspre.dto.MaterialRequestResponseDTO;
import com.jeferson.conspre.services.MaterialRequestService;
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

@RestController
@RequestMapping("/material-requests")
@Tag(name = "Material Request", description = "Endpoints for managing material requests")
public class MaterialRequestController {

    @Autowired
    private MaterialRequestService service;

    @Operation(summary = "Retrieve all material requests with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material requests retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MaterialRequestResponseDTO.class)
                    ))
    })
    @GetMapping
    public ResponseEntity<Page<MaterialRequestResponseDTO>> findAll(Pageable pageable) {

        Page<MaterialRequestResponseDTO> response = service.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retrieve a material request by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material request found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MaterialRequestResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Material request not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<MaterialRequestResponseDTO> findById(@PathVariable Long id) {

        MaterialRequestResponseDTO result = service.findById(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Create a new material request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Material request created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MaterialRequestResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping
    public ResponseEntity<MaterialRequestResponseDTO> create(
            @Valid @RequestBody MaterialRequestCreateDTO dto) {

        MaterialRequestResponseDTO result = service.create(dto);
        return ResponseEntity.status(201).body(result);
    }

    @Operation(summary = "Delete a material request by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Material request deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Material request not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}