package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Eleve;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EleveRepository extends JpaRepository<Eleve, Long> {

    Page<Eleve> findByPrenomContainingIgnoreCaseOrNomContainingIgnoreCase(
            String prenom, String nom, Pageable pagination );

    Page<Eleve> findByParentId(Long parentId, Pageable pageable);

    @Query("""
            SELECT e FROM Eleve e
            WHERE e.classeId = :classeId
              AND (:recherche IS NULL OR :recherche = ''
                   OR LOWER(CONCAT(CONCAT(e.prenom, ' '), e.nom))
                      LIKE LOWER(CONCAT('%', :recherche, '%'))
                   OR LOWER(e.matricule) LIKE LOWER(CONCAT('%', :recherche, '%')))
            """)
    Page<Eleve> chercherParClasse(@Param("classeId") Long classeId, @Param("recherche") String recherche, Pageable pagination
    );
}
