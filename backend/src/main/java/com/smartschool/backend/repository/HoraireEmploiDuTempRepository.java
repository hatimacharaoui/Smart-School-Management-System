package com.smartschool.backend.repository;

import com.smartschool.backend.entity.HoraireEmploiDuTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoraireEmploiDuTempRepository extends JpaRepository<HoraireEmploiDuTemp, Long> {


    @Query("""
            SELECT h FROM HoraireEmploiDuTemp h
            WHERE (:jour IS NULL OR :jour = '' OR h.jour = :jour)
              AND (:classeId IS NULL OR h.classeId = :classeId)
              AND (:enseignantId IS NULL OR h.enseignantId = :enseignantId)
            ORDER BY CASE h.jour
                       WHEN 'Lundi' THEN 1
                       WHEN 'Mardi' THEN 2
                       WHEN 'Mercredi' THEN 3
                       WHEN 'Jeudi' THEN 4
                       WHEN 'Vendredi' THEN 5
                       WHEN 'Samedi' THEN 6
                       ELSE 7
                     END,
                     h.heureDebut
            """)
    Page<HoraireEmploiDuTemp> rechercher(
            @Param("jour") String jour, @Param("classeId") Long classeId,
            @Param("enseignantId") Long enseignantId, Pageable pageable);




}
