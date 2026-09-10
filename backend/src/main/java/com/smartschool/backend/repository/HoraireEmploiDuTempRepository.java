package com.smartschool.backend.repository;

import com.smartschool.backend.entity.HoraireEmploiDuTemp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoraireEmploiDuTempRepository extends JpaRepository<HoraireEmploiDuTemp, Long> {

    List<HoraireEmploiDuTemp> findByEnseignantId(Long enseignantId);

    List<HoraireEmploiDuTemp> findByJour(String jour);

    List<HoraireEmploiDuTemp> findByClasseId(Long classeId);

    boolean existsByEnseignantId(Long enseignantId);

    boolean existsByClasseId(Long classeId);

    boolean existsByMatiereId(Long matiereId);

}
