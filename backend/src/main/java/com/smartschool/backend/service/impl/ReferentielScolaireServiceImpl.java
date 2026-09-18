package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.*;
import com.smartschool.backend.entity.ClasseEnseignant;
import com.smartschool.backend.entity.ClasseScolaire;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.*;
import com.smartschool.backend.service.ReferentielScolaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReferentielScolaireServiceImpl implements ReferentielScolaireService {
    private final EnseignantRepository enseignantRepository;
    private final ParentRepository parentRepository;
    private final ClasseScolaireRepository classeScolaireRepository;
    private final MatiereRepository matiereRepository;
    private final ClasseEnseignantRepository classeEnseignantRepository;
    private final EleveRepository eleveRepository;
    private final DevoirRepository devoirRepository;
    private final PresenceRepository presenceRepository;
    private final PaiementRepository paiementRepository;
    private final HoraireEmploiDuTempRepository horaireEmploiDuTempRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mappers mappers;



    public Page<EnseignantDto> afficherEnseignants(Pageable pagination) {
        return enseignantRepository.findAll(pagination).map(mappers::toDto);
    }

    public Page<ParentDto> afficherParents(Pageable pagination) {
        return parentRepository.findAll(pagination).map(mappers::toDto);
    }

    public Page<ClasseScolaireDto> afficherClasses(Pageable pagination) {
        return classeScolaireRepository.findAll(pagination).map(mappers::toDto);
    }

    public Page<MatiereDto> afficherMatieres(Pageable pagination) {
        return matiereRepository.findAll(pagination).map(mappers::toDto);
    }

    public Page<ClasseEnseignantDto> afficherAffectionsClasses(Pageable pagination) {
        return classeEnseignantRepository.findAll(pagination).map(mappers::toDto);

    }
}
