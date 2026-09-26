package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.HoraireEmploiDuTempDto;
import com.smartschool.backend.entity.HoraireEmploiDuTemp;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.ClasseEnseignantRepository;
import com.smartschool.backend.repository.EleveRepository;
import com.smartschool.backend.repository.HoraireEmploiDuTempRepository;
import com.smartschool.backend.service.EmploiDuTempsService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmploiDuTempsServiceImpl implements EmploiDuTempsService {
    private final HoraireEmploiDuTempRepository horaireEmploiDuTempRepository;
    private final Mappers mappers;



    @Override
    public Page<HoraireEmploiDuTempDto> afficher(String jour, Long classId, Long enseignantId, Pageable pagination) {
        return horaireEmploiDuTempRepository.rechercher(jour, classId, enseignantId, pagination)
                .map(mappers::toDto);
    }



    public Page<HoraireEmploiDuTempDto> chercherParJour(String jour, Pageable pagination) {
        return horaireEmploiDuTempRepository.rechercher(jour, null, null, pagination)
                .map(mappers::toDto);
    }

    public Page<HoraireEmploiDuTempDto> chercherParEnseignant(
            Long enseignantId,
            Pageable pagination
    ) {
        return horaireEmploiDuTempRepository.rechercher(null, null, enseignantId, pagination)
                .map(mappers::toDto);
    }

    public Page<HoraireEmploiDuTempDto> chercherParClasse(Long classeId, Pageable pagination) {
        return horaireEmploiDuTempRepository.rechercher(null, classeId, null, pagination)
                .map(mappers::toDto);
    }

    @CacheEvict(value = "horaires", allEntries = true)
    public HoraireEmploiDuTempDto enregistrer(HoraireEmploiDuTempDto dto) {
        HoraireEmploiDuTemp horaire = mappers.toEntite(dto);
        return mappers.toDto(horaireEmploiDuTempRepository.save(horaire));
    }

    @CacheEvict(value = "horaires", allEntries = true)
    public HoraireEmploiDuTempDto modifier(Long id, HoraireEmploiDuTempDto donnees) {
        HoraireEmploiDuTemp horaire = horaireEmploiDuTempRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horaire introuvable"));
        mappers.update(donnees, horaire);
        return mappers.toDto(horaireEmploiDuTempRepository.save(horaire));
    }
}
