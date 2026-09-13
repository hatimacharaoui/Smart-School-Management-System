package com.smartschool.backend.service;

import com.smartschool.backend.dto.HoraireEmploiDuTempDto;
import com.smartschool.backend.entity.HoraireEmploiDuTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmploiDuTempsService {
    Page<HoraireEmploiDuTempDto> afficher(Pageable pagination);

    HoraireEmploiDuTempDto creer(HoraireEmploiDuTempDto dto);

    HoraireEmploiDuTempDto modifier(Long id, HoraireEmploiDuTempDto dto);

    void supprimer(Long id);
}
