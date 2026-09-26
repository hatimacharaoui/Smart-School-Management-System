package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Eleve;
import com.smartschool.backend.entity.StatutPaiement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
    Page<Eleve> chercherParClasse(@Param("classeId") Long classeId, @Param("recherche") String recherche, Pageable pagination);


    @Query("""
            SELECT e FROM Eleve e
            WHERE :statut IS NULL
               OR EXISTS (
                    SELECT p FROM Paiement p
                    WHERE p.eleveId = e.id
                      AND p.date >= :dateDebut
                      AND p.date <= :dateFin
                      AND p.statut = :statut
               )
               OR (
                    :statut = :statutEnAttente
                    AND NOT EXISTS (
                        SELECT p FROM Paiement p
                        WHERE p.eleveId = e.id
                          AND p.date >= :dateDebut
                          AND p.date <= :dateFin
                    )
               )
            """)
    Page<Eleve> chercherParStatutPaiementMensuel(
            @Param("statut") StatutPaiement statut,
            @Param("statutEnAttente") StatutPaiement statutEnAttente,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin,
            Pageable pagination
    );

    long countByClasseId(Long classeId);
    boolean existsByClasseId(Long classeId);
    boolean existsByParentId(Long parentId);
}
