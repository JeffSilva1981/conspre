package com.jeferson.conspre.services;

import com.jeferson.conspre.dto.ChangePasswordDTO;
import com.jeferson.conspre.dto.CreateUserDTO;
import com.jeferson.conspre.dto.UpdateUserDTO;
import com.jeferson.conspre.dto.UserResponseDTO;
import com.jeferson.conspre.entity.User;
import com.jeferson.conspre.repositories.UserRepository;
import com.jeferson.conspre.services.exceptions.DatabaseException;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(String name, Pageable pageable) {

        if (name == null){
            name = "";
        }

        Page<User> page = repository.searchByName(name, pageable);
        return page.map(user -> new UserResponseDTO(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {

        User user = repository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Usuário com id: " + id + " não encontrado"));

        return new UserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO insert(CreateUserDTO dto) {

        if (repository.existsByLogin(dto.getLogin())){
            throw new DatabaseException
                    ("Já existe um usuário com este login " + dto.getLogin() + " cadastrado");
        }

        User entity = new User();
        copyDtoToEntity(entity, dto);
        entity.setAtivo(true);
        entity = repository.save(entity);
        return new UserResponseDTO(entity);
    }

    @Transactional
    public UserResponseDTO update(Long id, UpdateUserDTO dto) {

        User entity = repository.findById(id).orElseThrow
                (()-> new ResourceNotFoundException("Usuário com id " + id + " não encontrado"));

        if (!entity.getLogin().equals(dto.getLogin())
                && repository.existsByLogin(dto.getLogin())) {
            throw new DatabaseException("Já existe um usuário com este login " + dto.getLogin());
        }

        copyUpdateDtoToEntity(entity, dto);
        entity = repository.save(entity);
        return new UserResponseDTO(entity);

    }

    @Transactional
    public void changePassword(Long id, ChangePasswordDTO dto) {

        User entity = repository.findById(id).orElseThrow
                (()-> new ResourceNotFoundException("Usuário com id " + id + " não encontrado"));

        // Verifica senha atual
        if (!passwordEncoder.matches(dto.getCurrentPassword(), entity.getPassword())) {
            throw new DatabaseException("Senha atual incorreta");
        }

        // Verifica confirmação da nova senha
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new DatabaseException("Nova senha e confirmação não coincidem");
        }

        entity.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {

        User entity = repository.findById(id).orElseThrow
                (()-> new ResourceNotFoundException("Usuário com id " + id + " não encontrado"));

        entity.setAtivo(false);

        repository.save(entity);
    }

    private void copyUpdateDtoToEntity
            (User entity, UpdateUserDTO dto) {
        entity.setName(dto.getName());
        entity.setLogin(dto.getLogin());
    }

    private void copyDtoToEntity(User entity, CreateUserDTO dto) {
        entity.setName(dto.getName());
        entity.setLogin(dto.getLogin());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
    }
}
