package com.jeferson.conspre.services;


import com.jeferson.conspre.dto.MaterialDTO;
import com.jeferson.conspre.dto.MaterialMinDTO;
import com.jeferson.conspre.entity.Category;
import com.jeferson.conspre.entity.Material;
import com.jeferson.conspre.repositories.CategoryRepository;
import com.jeferson.conspre.repositories.MaterialRepository;
import com.jeferson.conspre.services.exceptions.DatabaseException;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<MaterialMinDTO> findAll(String name, Long categoryId, Boolean ativo,
                                        Boolean currentStock, Pageable pageable) {
        return repository.search(name, categoryId, ativo, currentStock, pageable);
    }

    @Transactional(readOnly = true)
    public MaterialDTO findById(Long id) {
        Material entity = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Material "+ id +" não encontrado."));
        return new MaterialDTO(entity);
    }

    @Transactional
    public MaterialDTO insert(MaterialDTO dto) {

        if (repository.existsByNameIgnoreCaseAndAtivoTrue(dto.getName())) {
            throw new DatabaseException("Já existe um material ativo com esse nome");
        }

        Material entity = new Material();
        copyDtoToEntity(entity, dto);
        entity.setAtivo(true);
        entity = repository.save(entity);
        return new MaterialDTO(entity);
    }

    @Transactional
    public MaterialDTO update(Long id, MaterialDTO dto) {


        if (repository.existsByNameIgnoreCaseAndAtivoTrueAndIdNot(dto.getName(), id)) {
            throw new DatabaseException("Já existe um material ativo com esse nome");
        }

            Material entity = repository.getReferenceById(id);
            copyDtoToEntity(entity, dto);
            entity.setAtivo(true);
            entity = repository.save(entity);
            return new MaterialDTO(entity);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {

        Material entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Material "+ id +" não encontrado." )
                );

        entity.setAtivo(false);
        repository.save(entity);
    }

    private void copyDtoToEntity(Material entity, MaterialDTO dto) {

        entity.setName(dto.getName());
        entity.setUnitOfMeasure(dto.getUnitOfMeasure());
        entity.setCurrentStock(dto.getCurrentStock());
        entity.setMinimumStock(dto.getMinimumStock());

        Category category = categoryRepository.getReferenceById(dto.getCategoryId());
        entity.setCategory(category);

    }

}
