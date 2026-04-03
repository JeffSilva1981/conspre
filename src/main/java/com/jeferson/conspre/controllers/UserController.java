package com.jeferson.conspre.controllers;

import com.jeferson.conspre.dto.ChangePasswordDTO;
import com.jeferson.conspre.dto.CreateUserDTO;
import com.jeferson.conspre.dto.UpdateUserDTO;
import com.jeferson.conspre.dto.UserResponseDTO;
import com.jeferson.conspre.services.UserService;
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
@RequestMapping("/users")
@Tag(name = "User", description = "Endpoints for managing users")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(summary = "Retrieve all users with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDTO.class)
                    ))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserResponseDTO>> findAll(
            @RequestParam(required = false) String name,
            Pageable pageable) {

        Page<UserResponseDTO> result = service.findAll(name, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Retrieve a user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        UserResponseDTO result = service.findById(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> insert(@Valid @RequestBody CreateUserDTO dto) {

        UserResponseDTO newDto = service.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(newDto);
    }

    @Operation(summary = "Update an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDTO dto) {

        UserResponseDTO newDto = service.update(id, dto);
        return ResponseEntity.ok(newDto);
    }

    @Operation(summary = "Change user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping(value = "/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordDTO dto) {

        service.changePassword(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}