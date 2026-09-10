package com.smartschool.backend.repository;

import com.smartschool.backend.entity.ClasseScolaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClasseScolaireRepository extends JpaRepository<ClasseScolaire, Long> {

    Page<ClasseScolaire> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    boolean existsByEnseignantPrincipalId(Long enseignantPrincipalId);
}
