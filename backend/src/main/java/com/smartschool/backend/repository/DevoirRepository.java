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


    @Query("""
            SELECT d FROM Devoir d
            WHERE (:enseignantId IS NULL OR d.enseignantId = :enseignantId)
              AND (:classeId IS NULL OR d.classeId = :classeId)
              AND (:statut IS NULL OR d.statut = :statut)
              AND (:recherche IS NULL OR :recherche = ''
                   OR LOWER(d.titre) LIKE LOWER(CONCAT('%', :recherche, '%')))
            """)
    Page<Devoir> rechercher(
            @org.springframework.data.repository.query.Param("recherche") String recherche,
            @org.springframework.data.repository.query.Param("statut") StatutDevoir statut,
            @org.springframework.data.repository.query.Param("enseignantId") Long enseignantId,
            @org.springframework.data.repository.query.Param("classeId") Long classeId,
            Pageable pagination
    );

}
