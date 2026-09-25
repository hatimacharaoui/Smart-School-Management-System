package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Enseignant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    Page<Enseignant> findByPrenomContainingIgnoreCaseOrNomContainingIgnoreCase(String prenom, String nom, Pageable pagination);

    boolean existsByMatiereId(Long matiereId);

}
