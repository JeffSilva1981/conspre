package com.jeferson.conspre.repositories;

import com.jeferson.conspre.dto.EmployeeMinDTO;
import com.jeferson.conspre.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByNameIgnoreCaseAndAtivoTrue(String name);
    boolean existsByNameIgnoreCaseAndAtivoTrueAndIdNot(String name, Long id);


    @Query("""
    SELECT e FROM Employee e
    WHERE
        (:name IS NULL OR
         UPPER(e.name) LIKE UPPER(CONCAT('%', :name, '%')))
""")
    Page<EmployeeMinDTO> search(@Param("name") String name, Pageable pageable);
}
