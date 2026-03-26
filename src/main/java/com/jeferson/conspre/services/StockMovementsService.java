package com.jeferson.conspre.services;

import com.jeferson.conspre.dto.StockMaterialDTO;
import com.jeferson.conspre.dto.StockMovementInputDTO;
import com.jeferson.conspre.dto.StockMovementResponseDTO;
import com.jeferson.conspre.entity.*;
import com.jeferson.conspre.enums.TypeMovement;
import com.jeferson.conspre.repositories.MaterialRepository;
import com.jeferson.conspre.repositories.StockMovementRepository;
import com.jeferson.conspre.repositories.UserRepository;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class StockMovementsService {

    @Autowired
    private StockMovementRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Transactional(readOnly = true)
    public Page<StockMovementResponseDTO> findAll(String materialName, Instant moment, Pageable pageable) {
        Page<StockMovement> page = repository.search(materialName, moment, pageable);

        return page.map((x -> new StockMovementResponseDTO(x)));
    }

    @Transactional(readOnly = true)
    public StockMovementResponseDTO findById(Long id) {
        StockMovement entity = repository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Movimentação com id: " + id + " não encontrada."));

        return new StockMovementResponseDTO(entity);
    }

    @Transactional
    public StockMovement createMovement(
            TypeMovement type,
            Material material,
            BigDecimal quantity,
            String observation,
            User user,
            Employee employee,
            MaterialRequest request) {

        // 🔥 AJUSTA ESTOQUE
        if (type == TypeMovement.OUTPUT) {

            if (material.getCurrentStock().compareTo(quantity) < 0) {
                throw new RuntimeException("Estoque insuficiente para o material: " + material.getName());
            }

            material.setCurrentStock(
                    material.getCurrentStock().subtract(quantity)
            );

        } else if (type == TypeMovement.INPUT) {

            material.setCurrentStock(
                    material.getCurrentStock().add(quantity)
            );
        }

        // 💾 SALVA MATERIAL ATUALIZADO
        materialRepository.save(material);

        // 🧾 CRIA MOVIMENTAÇÃO
        StockMovement movement = new StockMovement();

        movement.setType(type);
        movement.setMoment(Instant.now());
        movement.setMaterial(material);
        movement.setQuantity(quantity);
        movement.setObservation(observation);
        movement.setUser(user);
        movement.setEmployee(employee);
        movement.setMaterialRequest(request);

        return repository.save(movement);
    }

    @Transactional
    public StockMovementResponseDTO createInputMovement(StockMovementInputDTO dto) {

        Material material = materialRepository.findById(dto.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Material não encontrado"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado"));


        StockMovement movement = new StockMovement();

        movement.setType(TypeMovement.INPUT);
        movement.setMoment(Instant.now());
        movement.setMaterial(material);
        movement.setQuantity(dto.getQuantity());
        movement.setObservation(dto.getObservation());
        movement.setUser(user);

        movement = repository.save(movement);

        return new StockMovementResponseDTO(movement);

    }

    @Transactional(readOnly = true)
    public StockMaterialDTO getMaterialStock(Long materialId) {

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Material não encontrado"));

        BigDecimal stock = calculateStock(materialId);

        return new StockMaterialDTO(
                material.getId(),
                material.getName(),
                stock
        );
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateStock(Long materialId) {

        List<StockMovement> movements = repository.findByMaterialId(materialId);

        BigDecimal stock = BigDecimal.ZERO;

        for (StockMovement m : movements) {

            if (m.getType() == TypeMovement.INPUT) {
                stock = stock.add(m.getQuantity());
            }
            if (m.getType() == TypeMovement.OUTPUT) {
                stock = stock.subtract(m.getQuantity());
            }
        }
        return stock;
    }

    @Transactional(readOnly = true)
    public List<StockMovementResponseDTO> findByRequest(Long requestId) {

        List<StockMovement> list = repository.findByMaterialRequestId(requestId);

        return list.stream()
                .map(StockMovementResponseDTO::new)
                .toList();
    }
}
