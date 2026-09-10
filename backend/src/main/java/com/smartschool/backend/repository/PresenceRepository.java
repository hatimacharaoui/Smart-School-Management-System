package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Presence;
import com.smartschool.backend.entity.StatutPresence;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PresenceRepository extends JpaRepository<Presence, Long> {

    List<Presence> findByEleveId(Long eleveId);

    List<Presence> findByClasseIdAndDate(Long classeId, LocalDate date);

    List<Presence> findByStatutIn(List<StatutPresence> statuts);

    Optional<Presence> findByEleveIdAndDateAndMatiereId(Long eleveId, LocalDate date, Long matiereId);

    boolean existsByEnseignantId(Long enseignantId);

    boolean existsByClasseId(Long classeId);

    boolean existsByMatiereId(Long matiereId);
}
