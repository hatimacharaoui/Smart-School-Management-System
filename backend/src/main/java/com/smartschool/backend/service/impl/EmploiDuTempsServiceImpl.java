package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.HoraireEmploiDuTempDto;
import com.smartschool.backend.entity.HoraireEmploiDuTemp;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.ClasseEnseignantRepository;
import com.smartschool.backend.repository.EleveRepository;
import com.smartschool.backend.repository.HoraireEmploiDuTempRepository;
import com.smartschool.backend.service.EmploiDuTempsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmploiDuTempsServiceImpl implements EmploiDuTempsService {
    private final HoraireEmploiDuTempRepository horaireEmploiDuTempRepository;
    private final Mappers mappers;
    private final EleveRepository eleveRepository;
    private final ClasseEnseignantRepository classeEnseignantRepository;



    @Override
    public Page<HoraireEmploiDuTempDto> afficher(String jour, Long classId, Long enseignantId, Pageable pagination) {
        return horaireEmploiDuTempRepository.rechercher(jour, classId, enseignantId, pagination)
                .map(mappers::toDto);
    }

    public HoraireEmploiDuTempDto creer(HoraireEmploiDuTempDto dto) {
        HoraireEmploiDuTemp horaire = mappers.toEntite(dto);

        return mappers.toDto(horaireEmploiDuTempRepository.save(horaire));
    }

    public HoraireEmploiDuTempDto modifier(Long id, HoraireEmploiDuTempDto dto) {
        HoraireEmploiDuTemp horaire = findById(id);
        mappers.update(dto, horaire);
        return mappers.toDto(horaireEmploiDuTempRepository.save(horaire));
    }

    public void supprimer(Long id) {
        HoraireEmploiDuTemp horaire = findById(id);
        horaireEmploiDuTempRepository.delete(horaire);
    }

    private HoraireEmploiDuTemp findById(Long id) {
        return horaireEmploiDuTempRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horaire introuvable"));
    }
}
