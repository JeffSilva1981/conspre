package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.User;
import com.jeferson.conspre.projections.UserDetailsProjection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private String existingLogin;
    private String nonExistingLogin;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;

        existingLogin = "Jeferson";
        nonExistingLogin = "naoexiste";

    }

    // =========================
    // BASIC CRUD
    // =========================

    @Test
    public void findAllShouldReturnPage() {

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<User> result = repository.findAll(pageRequest);

        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void findByIdShouldReturnUserWhenIdExists() {

        Optional<User> result = repository.findById(existingId);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyWhenIdDoesNotExist() {

        Optional<User> result = repository.findById(nonExistingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void saveShouldPersistUserWhenIdIsNull() {

        User user = Factory.createdUser();
        user = repository.save(user);
        Assertions.assertNotNull(user.getId());
    }

    @Test
    public void saveShouldThrowExceptionWhenLoginAlreadyExists() {

        User user = new User();
        user.setName("Outro Usuário");
        user.setLogin(existingLogin); // já existe
        user.setPassword("123456");
        user.setAtivo(true);

        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> repository.save(user)
        );
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        repository.deleteById(existingId);
        Optional<User> result = repository.findById(existingId);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void deleteShouldThrowExceptionWhenIdDoesNotExist() {

        Assertions.assertDoesNotThrow(
                () -> {
                    repository.deleteById(nonExistingId);
                });
    }

    // =========================
    // EXISTS BY LOGIN
    // =========================

    @Test
    public void existsByLoginShouldReturnTrueWhenLoginExists() {

        boolean result = repository.existsByLogin(existingLogin);
        Assertions.assertTrue(result);
    }

    @Test
    public void existsByLoginShouldReturnFalseWhenLoginDoesNotExist() {

        boolean result = repository.existsByLogin(nonExistingLogin);
        Assertions.assertFalse(result);
    }

    // =========================
    // SEARCH BY NAME
    // =========================

    @Test
    public void searchByNameShouldReturnPageWhenNameExists() {

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<User> result = repository.searchByName("jeferson", pageRequest);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void searchByNameShouldReturnEmptyPageWhenNameDoesNotExist() {

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<User> result = repository.searchByName("xxxxx", pageRequest);
        Assertions.assertTrue(result.isEmpty());
    }

    // =========================
    // PROJECTION (LOGIN + ROLES)
    // =========================

    @Test
    public void searchUserAndRolesByLoginShouldReturnListWhenLoginExists() {

        List<UserDetailsProjection> result =
                repository.searchUserAndRolesByLogin(existingLogin);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void searchUserAndRolesByLoginShouldReturnEmptyListWhenLoginDoesNotExist() {

        List<UserDetailsProjection> result =
                repository.searchUserAndRolesByLogin(nonExistingLogin);
        Assertions.assertTrue(result.isEmpty());
    }

}
