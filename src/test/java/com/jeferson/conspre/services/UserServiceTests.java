package com.jeferson.conspre.services;

import com.jeferson.conspre.dto.ChangePasswordDTO;
import com.jeferson.conspre.dto.CreateUserDTO;
import com.jeferson.conspre.dto.UpdateUserDTO;
import com.jeferson.conspre.dto.UserResponseDTO;
import com.jeferson.conspre.entity.User;
import com.jeferson.conspre.projections.UserDetailsProjection;
import com.jeferson.conspre.repositories.UserRepository;
import com.jeferson.conspre.services.exceptions.DatabaseException;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Long existingId;
    private Long nonExistingId;

    private User user;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;

        user = new User();
        user.setId(existingId);
        user.setName("Jeferson");
        user.setLogin("jef");
        user.setPassword("123456");
        user.setAtivo(true);

        // findById
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(user));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // existsByLogin
        Mockito.when(repository.existsByLogin("jef")).thenReturn(true);
        Mockito.when(repository.existsByLogin("novo")).thenReturn(false);

        // save
        Mockito.when(repository.save(Mockito.any())).thenReturn(user);

        // password encoder
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("encoded");
        Mockito.when(passwordEncoder.matches("123", "123456")).thenReturn(true);
    }

    // ===============================
    // ✅ loadUserByUsername
    // ===============================
    @Test
    public void loadUserByUsernameShouldReturnUserWhenExists() {

        UserDetailsProjection projection = Mockito.mock(UserDetailsProjection.class);

        Mockito.when(projection.getPassword()).thenReturn("123456");
        Mockito.when(projection.getRoleId()).thenReturn(1L);
        Mockito.when(projection.getAuthority()).thenReturn("ROLE_ADMIN");

        Mockito.when(repository.searchUserAndRolesByLogin("jef"))
                .thenReturn(List.of(projection));

        var result = service.loadUserByUsername("jef");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("jef", result.getUsername());
    }

    @Test
    public void loadUserByUsernameShouldThrowWhenNotFound() {

        Mockito.when(repository.searchUserAndRolesByLogin("x"))
                .thenReturn(List.of());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("x");
        });
    }

    // ===============================
    // ✅ findById
    // ===============================
    @Test
    public void findByIdShouldReturnDTOWhenIdExists() {

        UserResponseDTO dto = service.findById(existingId);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals("Jeferson", dto.getName());
    }

    @Test
    public void findByIdShouldThrowWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    // ===============================
    // ✅ insert
    // ===============================
    @Test
    public void insertShouldReturnDTOWhenValid() {

        CreateUserDTO dto = new CreateUserDTO();
        dto.setName("Novo");
        dto.setLogin("novo");
        dto.setPassword("123");

        UserResponseDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
    }

    @Test
    public void insertShouldThrowWhenLoginExists() {

        CreateUserDTO dto = new CreateUserDTO();
        dto.setLogin("jef");

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.insert(dto);
        });
    }

    // ===============================
    // ✅ update
    // ===============================
    @Test
    public void updateShouldReturnDTOWhenValid() {

        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setName("Novo Nome");
        dto.setLogin("jef");

        UserResponseDTO result = service.update(existingId, dto);

        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowWhenIdNotExists() {

        UpdateUserDTO dto = new UpdateUserDTO();

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, dto);
        });
    }

    // ===============================
    // ✅ changePassword
    // ===============================
    @Test
    public void changePasswordShouldUpdateWhenValid() {

        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setCurrentPassword("123");
        dto.setNewPassword("456");
        dto.setConfirmPassword("456");

        service.changePassword(existingId, dto);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void changePasswordShouldThrowWhenWrongCurrentPassword() {

        Mockito.when(passwordEncoder.matches("errada", "123456"))
                .thenReturn(false);

        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setCurrentPassword("errada");
        dto.setNewPassword("456");
        dto.setConfirmPassword("456");

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.changePassword(existingId, dto);
        });
    }

    @Test
    public void changePasswordShouldThrowWhenConfirmationFails() {

        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setCurrentPassword("123");
        dto.setNewPassword("456");
        dto.setConfirmPassword("999");

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.changePassword(existingId, dto);
        });
    }

    // ===============================
    // ✅ delete
    // ===============================
    @Test
    public void deleteShouldSoftDeleteWhenIdExists() {

        service.delete(existingId);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void deleteShouldThrowWhenIdNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

}
