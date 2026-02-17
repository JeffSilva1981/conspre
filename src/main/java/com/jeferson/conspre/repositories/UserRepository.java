package com.jeferson.conspre.repositories;

import com.jeferson.conspre.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLogin(String login);

    @Query("SELECT u FROM User u WHERE u.ativo = true AND LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<User> searchByName(@Param("name") String name, Pageable pageable);

}
