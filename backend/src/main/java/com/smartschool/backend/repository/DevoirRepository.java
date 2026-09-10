package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Devoir;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.ListResourceBundle;

public interface DevoirRepository extends JpaRepository<Devoir, Long> {

    List<Devoir> findByEnseignantId(Long enseignantId);
    List<Devoir> findByClasseId(Long classeId);
    List<Devoir> findByTitreContainingIgnoreCase(String titre);
    boolean existsByEnseignantId(Long enseignantId);
    boolean existsByClasseId(Long classeId);
    boolean existsByMatiereId(Long matiereId);


}
