package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Parent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    Page<Parent> findByPrenomContainingIgnoreCaseOrNomContainingIgnoreCase(
            String prenom,
            String nom,
            Pageable pagination
    );
}
