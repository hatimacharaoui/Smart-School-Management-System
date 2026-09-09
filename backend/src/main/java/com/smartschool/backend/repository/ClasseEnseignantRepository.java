package com.smartschool.backend.repository;

import com.smartschool.backend.entity.ClasseEnseignant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasseEnseignantRepository extends JpaRepository<ClasseEnseignant, Long> {

    List<ClasseEnseignant> findByEnseignantId(Long enseignantId);
}
