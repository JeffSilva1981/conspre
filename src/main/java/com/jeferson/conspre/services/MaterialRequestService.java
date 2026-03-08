package com.jeferson.conspre.services;

import com.jeferson.conspre.dto.MaterialRequestCreateDTO;
import com.jeferson.conspre.dto.MaterialRequestResponseDTO;
import com.jeferson.conspre.dto.RequestMaterialItemDTO;
import com.jeferson.conspre.entity.Employee;
import com.jeferson.conspre.entity.Material;
import com.jeferson.conspre.entity.MaterialRequest;
import com.jeferson.conspre.entity.User;
import com.jeferson.conspre.repositories.*;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class MaterialRequestService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialRequestRepository materialRequestRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<MaterialRequestResponseDTO> findAll(Long employeeId, Boolean ativo, Instant dateMin, Instant dateMax, String observation, Pageable pageable) {

        Page<MaterialRequest> result = materialRequestRepository.search(
                employeeId, ativo, dateMax, dateMin, observation, pageable);

        return result.map(x -> new MaterialRequestResponseDTO(x));
    }

    @Transactional(readOnly = true)
    public MaterialRequestResponseDTO findById(Long id) {

        MaterialRequest entity = materialRequestRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Requisição de Material com id: " + id + " não encontrada."));

                return new MaterialRequestResponseDTO(entity);

    }


    @Transactional
    public MaterialRequestResponseDTO create(MaterialRequestCreateDTO dto) {

        Employee employee = employeeRepository.findById(dto.getEmployeeId()).orElseThrow(
                ()-> new ResourceNotFoundException("Funcionário com id: " + dto.getEmployeeId() + " não encontrado."));

        User user = userRepository.findById(dto.getUserId()).orElseThrow(
                ()-> new ResourceNotFoundException("Usuário com id: " + dto.getUserId() + " não encontrado."));

        MaterialRequest entity = new MaterialRequest();

        entity.setMoment(Instant.now());
        entity.setEmployee(employee);
        entity.setUser(user);
        entity.setObservation(dto.getObservation());
        entity.setAtivo(true);

        for (RequestMaterialItemDTO itemDTO : dto.getItems()){
            Material material = materialRepository.findById(itemDTO.getMaterialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Material not found"));

            entity.addItem(material, itemDTO.getQuantity());

        }

        entity = materialRequestRepository.save(entity);

        return new MaterialRequestResponseDTO(entity);
    }


    public void delete(Long id) {

        MaterialRequest entity = materialRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Requisição de material não existe."));

        entity.setAtivo(false);

        materialRequestRepository.save(entity);
    }
}
