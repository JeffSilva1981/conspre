package com.jeferson.conspre.controllers;

import com.jeferson.conspre.dto.CategoryDTO;
import com.jeferson.conspre.services.CategoryService;
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
@RequestMapping(value = "/categories")
@Tag(name = "Category", description = "Endpoints for managing categories")
public class CategoryResource {

    @Autowired
    private CategoryService service;

    @Operation(summary = "Retrieve all categories with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryDTO.class)
                    ))
    })
    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable) {
        Page<CategoryDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Retrieve a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        CategoryDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@Valid @RequestBody CategoryDTO dto) {
        CategoryDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @Operation(summary = "Update an existing category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        CategoryDTO newDto = service.update(id, dto);
        return ResponseEntity.ok(newDto);
    }

    @Operation(summary = "Delete a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}