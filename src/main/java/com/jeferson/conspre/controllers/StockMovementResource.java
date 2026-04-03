package com.jeferson.conspre.controllers;

import com.jeferson.conspre.dto.StockMaterialDTO;
import com.jeferson.conspre.dto.StockMovementInputDTO;
import com.jeferson.conspre.dto.StockMovementResponseDTO;
import com.jeferson.conspre.services.StockMovementsService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock-movements")
@Tag(name = "Stock Movement", description = "Endpoints for managing stock movements")
public class StockMovementResource {

    @Autowired
    private StockMovementsService service;

    @Operation(summary = "Retrieve all stock movements with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock movements retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StockMovementResponseDTO.class)
                    ))
    })
    @GetMapping(value = "/requests")
    public ResponseEntity<Page<StockMovementResponseDTO>> findAllRequests(Pageable pageable) {
        Page<StockMovementResponseDTO> result = service.findAllRequests(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Retrieve a stock movement by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock movement found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StockMovementResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Stock movement not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<StockMovementResponseDTO> findById(@PathVariable Long id) {
        StockMovementResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new stock input movement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stock movement created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StockMovementResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping(value = "/input")
    public ResponseEntity<StockMovementResponseDTO> createInput(
            @Valid @RequestBody StockMovementInputDTO dto) {

        StockMovementResponseDTO result = service.createInputMovement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Retrieve material stock by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StockMaterialDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Material not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/materials/{id}/stock")
    public ResponseEntity<StockMaterialDTO> getMaterialStock(@PathVariable Long id) {

        StockMaterialDTO dto = service.getMaterialStock(id);
        return ResponseEntity.ok(dto);
    }
}