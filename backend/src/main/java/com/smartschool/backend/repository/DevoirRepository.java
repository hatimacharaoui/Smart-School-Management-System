package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Devoir;
import com.smartschool.backend.entity.StatutDevoir;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.ListResourceBundle;

public interface DevoirRepository extends JpaRepository<Devoir, Long> {

    @Query("""
    SELECT d FROM Devoir d
    WHERE d.enseignantId = :enseignantId AND (:statut IS NULL OR d.statut = :statut)
    """)
    Page<Devoir> chercherParEnseignant(
            @Param("enseignantId") Long enseignantId,
            @Param("statut") StatutDevoir statut, Pageable pagination );


}
