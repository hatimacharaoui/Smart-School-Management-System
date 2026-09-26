package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Devoir;
import com.smartschool.backend.entity.StatutDevoir;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.ListResourceBundle;

public interface DevoirRepository extends JpaRepository<Devoir, Long> {

    @Query("""
    SELECT d FROM Devoir d
            WHERE (:enseignantId IS NULL OR d.enseignantId = :enseignantId)
              AND (:classeId IS NULL OR d.classeId = :classeId)
              AND (:statut IS NULL OR d.statut = :statut)
              AND (:recherche IS NULL OR :recherche = ''
                   OR LOWER(d.titre) LIKE LOWER(CONCAT('%', :recherche, '%')))
    """)
    Page<Devoir> rechercher(
            @Param("recherche") String recherche,
            @Param("statut") StatutDevoir statut,
            @Param("enseignantId") Long enseignantId,
            @Param("classeId") Long classeId,
            Pageable pagination );


    Page<Devoir> findByStatut(StatutDevoir statut, Pageable pagination);
    boolean existsByEnseignantId(Long enseignantId);
    boolean existsByClasseId(Long classeId);
    boolean existsByMatiereId(Long matiereId);


}
