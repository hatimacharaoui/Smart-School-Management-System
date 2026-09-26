package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Paiement;
import com.smartschool.backend.entity.StatutPaiement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    @Query("""
            SELECT p FROM Paiement p
            WHERE (:eleveId IS NULL OR p.eleveId = :eleveId)
              AND (:statut IS NULL OR p.statut = :statut)
              AND (:dateDebut IS NULL OR p.date >= :dateDebut)
              AND (:dateFin IS NULL OR p.date <= :dateFin)
            """)
    Page<Paiement> rechercher(@Param("eleveId") Long eleveId, @Param("statut") StatutPaiement statut,
            @Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin, Pageable pagination
    );

    boolean existsByParentId(Long parentId);

    boolean existsByEleveIdAndDateBetween(Long eleveId, LocalDate dateDebut, LocalDate dateFin);

    List<Paiement> findByEleveIdInAndDateBetween(List<Long> eleveIds, LocalDate dateDebut, LocalDate dateFin);

}
