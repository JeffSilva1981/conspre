package com.jeferson.conspre.services;

import com.jeferson.conspre.dto.CategoryDTO;
import com.jeferson.conspre.entity.Category;
import com.jeferson.conspre.repositories.CategoryRepository;
import com.jeferson.conspre.services.exceptions.DatabaseException;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {
        Page<Category> categories = repository.findAll(pageable);
        return categories.map(x-> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category entity = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Categoria "+ id +" não encontrada."));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {

        if (repository.existsByNameIgnoreCaseAndAtivoTrue(dto.getName())) {
            throw new DatabaseException("Já existe uma categoria ativa com esse nome");
        }

        Category entity = new Category();
        copyDtoToEntity(entity, dto);
        entity.setAtivo(true);
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {


        if (repository.existsById(id)){
            if (repository.existsByNameIgnoreCaseAndAtivoTrue(dto.getName())) {
                throw new DatabaseException("Já existe uma categoria ativa com esse nome");
            }
        }

        try {
            Category entity = repository.getReferenceById(id);
            copyDtoToEntity(entity, dto);
            entity = repository.save(entity);
            entity.setAtivo(true);
            return new CategoryDTO(entity);

        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Categoria "+ id +" não encontrada.");
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {

        Category entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria "+ id +" não encontrada." )
                );

        entity.setAtivo(false);
        repository.save(entity);
    }

    private void copyDtoToEntity(Category entity, CategoryDTO dto) {

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}
