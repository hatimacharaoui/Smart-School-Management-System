package com.smartschool.backend.service;

import com.smartschool.backend.dto.HoraireEmploiDuTempDto;
import com.smartschool.backend.entity.HoraireEmploiDuTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmploiDuTempsService {
    Page<HoraireEmploiDuTempDto> afficher(String jour, Long classId, Long enseignantId ,Pageable pagination);

    Page<HoraireEmploiDuTempDto> chercherParJour(String jour, Pageable pagination);
    Page<HoraireEmploiDuTempDto> chercherParEnseignant(Long enseignantId, Pageable pagination);
    Page<HoraireEmploiDuTempDto> chercherParClasse(Long classeId, Pageable pagination);
    HoraireEmploiDuTempDto enregistrer(HoraireEmploiDuTempDto horaire);
    HoraireEmploiDuTempDto modifier(Long id, HoraireEmploiDuTempDto horaire);
}
