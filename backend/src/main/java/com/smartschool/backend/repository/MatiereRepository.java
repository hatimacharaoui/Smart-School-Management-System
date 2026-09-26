package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Matiere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatiereRepository extends JpaRepository<Matiere, Long> {

    Page<Matiere> findByNomContainingIgnoreCase(String nom, Pageable pageable);

}
