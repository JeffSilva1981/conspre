package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.MaterialRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface MaterialRequestRepository extends JpaRepository<MaterialRequest, Long> {


    @Query("""
                SELECT m FROM MaterialRequest m
                LEFT JOIN FETCH m.requestMaterialItems
                WHERE
                    (:employeeId IS NULL OR m.employee.id = :employeeId)
                AND (:ativo IS NULL OR m.ativo = :ativo)
                AND (:dateMin IS NULL OR m.moment >= :dateMin)
                AND (:dateMax IS NULL OR m.moment <= :dateMax)
                AND (:observation IS NULL OR LOWER(m.observation) LIKE LOWER(CONCAT('%', :observation, '%')))
            """)
    Page<MaterialRequest> search(
            Long employeeId,
            Boolean ativo,
            Instant dateMin,
            Instant dateMax,
            String observation,
            Pageable pageable
    );

}
