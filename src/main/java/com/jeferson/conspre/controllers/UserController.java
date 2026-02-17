package com.jeferson.conspre.controllers;

import com.jeferson.conspre.dto.ChangePasswordDTO;
import com.jeferson.conspre.dto.CreateUserDTO;
import com.jeferson.conspre.dto.UpdateUserDTO;
import com.jeferson.conspre.dto.UserResponseDTO;
import com.jeferson.conspre.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(
            @RequestParam(required = false) String name, Pageable pageable){

        Page<UserResponseDTO> result = service.findAll(name, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id){
        UserResponseDTO result = service.findById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> insert(@Valid @RequestBody CreateUserDTO dto){

        UserResponseDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
                buildAndExpand(newDto.getId()).toUri();

        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO dto){

        UserResponseDTO newDto = service.update(id, dto);

        return ResponseEntity.ok(newDto);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordDTO dto){

        service.changePassword(id, dto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
