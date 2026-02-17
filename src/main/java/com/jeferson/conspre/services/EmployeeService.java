package com.jeferson.conspre.services;

import com.jeferson.conspre.dto.EmployeeDTO;
import com.jeferson.conspre.dto.EmployeeMinDTO;
import com.jeferson.conspre.dto.MaterialDTO;
import com.jeferson.conspre.entity.Employee;
import com.jeferson.conspre.entity.Material;
import com.jeferson.conspre.repositories.EmployeeRepository;
import com.jeferson.conspre.services.exceptions.DatabaseException;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Transactional(readOnly = true)
    public Page<EmployeeMinDTO> findAll(String name, Pageable pageable) {
       return repository.search(name, pageable);
    }

    @Transactional(readOnly = true)
    public EmployeeDTO findById(Long id) {
        Employee entity = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Funcionário com id:  "+ id +" não encontrado."));
        return new EmployeeDTO(entity);
    }

    @Transactional
    public EmployeeDTO insert(EmployeeDTO dto) {

        if (repository.existsByNameIgnoreCaseAndAtivoTrue(dto.getName())) {
            throw new DatabaseException("Já existe um funcionário ativo com esse nome");
        }

        Employee entity = new Employee();
        copyDtoToEntity(entity, dto);
        entity.setAtivo(true);
        entity = repository.save(entity);
        return new EmployeeDTO(entity);
    }

    public EmployeeDTO update(Long id, EmployeeDTO dto) {

        if (repository.existsByNameIgnoreCaseAndAtivoTrueAndIdNot(dto.getName(), id)) {
            throw new DatabaseException("Já existe um funcionário ativo com esse nome");
        }

        try {
            Employee entity = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee " + id + " não encontrado"));
            copyDtoToEntity(entity, dto);
            entity.setAtivo(true);
            entity = repository.save(entity);
            return new EmployeeDTO(entity);

        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Funcionário com id:  "+ id +" não encontrado.");
        }
    }

    public void delete(Long id) {

        Employee entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Funcionário com id:  "+ id +" não encontrado." )
                );

        entity.setAtivo(false);
        repository.save(entity);
    }

    private void copyDtoToEntity(Employee entity, EmployeeDTO dto) {

        entity.setName(dto.getName());
        entity.setRegistration(dto.getRegistration());
    }

}
