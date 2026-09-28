package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Note;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Page<Note> findByEleveId(Long eleveId, Pageable pageable);


    @Query("""
            SELECT n FROM Note n, Devoir d, Eleve e
            WHERE d.id = n.devoirId
              AND e.id = n.eleveId
              AND (:eleveId IS NULL OR n.eleveId = :eleveId)
              AND (:devoirId IS NULL OR n.devoirId = :devoirId)
              AND (:enseignantId IS NULL OR n.enseignantId = :enseignantId)
              AND (:classeId IS NULL OR d.classeId = :classeId)
              AND (:matiereId IS NULL OR d.matiereId = :matiereId)
              AND (:rechercheEleve IS NULL OR :rechercheEleve = ''
                   OR LOWER(CONCAT(CONCAT(e.prenom, ' '), e.nom))
                      LIKE LOWER(CONCAT('%', :rechercheEleve, '%')))
            """)
    Page<Note> rechercher(
            @Param("eleveId") Long eleveId,
            @Param("devoirId") Long devoirId,
            @Param("enseignantId") Long enseignantId,
            @Param("classeId") Long classeId,
            @Param("matiereId") Long matiereId,
            @Param("rechercheEleve") String rechercheEleve,
            Pageable pagination
    );
    Optional<Note> findByEleveIdAndDevoirId(Long eleveId, Long devoirId);
}
