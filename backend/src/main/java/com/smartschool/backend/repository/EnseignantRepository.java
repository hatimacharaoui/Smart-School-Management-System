package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    boolean existsByMatiereId(Long matiereId);

}
