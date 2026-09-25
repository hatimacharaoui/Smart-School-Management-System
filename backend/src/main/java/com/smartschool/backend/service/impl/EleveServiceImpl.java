package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.EleveDto;
import com.smartschool.backend.entity.Eleve;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.entity.TypeNotification;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.EleveRepository;
import com.smartschool.backend.repository.UserRepository;
import com.smartschool.backend.service.EleveService;
import com.smartschool.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EleveServiceImpl implements EleveService {
    private final EleveRepository eleveRepository;
    private final Mappers mapper;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    public Page<EleveDto> chercherParParent(Long parentId, Pageable pageable) {

        return eleveRepository.findByParentId(parentId, pageable)
                .map(mapper::toDto);
    }

    public Page<EleveDto> afficher(String recherche, Pageable pagination) {
        if (recherche != null && !recherche.isBlank()) {
            return eleveRepository.findByPrenomContainingIgnoreCaseOrNomContainingIgnoreCase(
                    recherche, recherche, pagination ).map(mapper::toDto);
        } else {
            return eleveRepository.findAll(pagination).map(mapper::toDto);
        }
        }

    public Page<EleveDto> chercherParClasse(Long classeId, String recherche, Pageable pagination) {
        return eleveRepository.chercherParClasse(classeId, recherche, pagination)
                .map(mapper::toDto);
    }

    public EleveDto chercherParId(Long id) {
        Eleve eleve = findById(id);

        return mapper.toDto(eleve);
    }



    public EleveDto creer(EleveDto dto) {
        if (dto.getMotDePasse() == null || dto.getMotDePasse().isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire.");
        }
        Eleve eleve = mapper.toEntite(dto);
        eleve.setNomComplet(eleve.getPrenom() + " " + eleve.getNom());
        eleve.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        eleve.setRole(Role.ELEVE);
        eleve.setActif(true);
        Eleve eleveEnregistre = eleveRepository.save(eleve);
        notificationService.notifierRole(
                Role.ENSEIGNANT,
                "Nouvel élève",
                "Un élève a été ajouté à une classe.",
                TypeNotification.UTILISATEUR,
                eleveEnregistre.getId()
        );
        return mapper.toDto(eleveEnregistre);
    }


    public EleveDto modifier(Long id, EleveDto dto) {
        Eleve eleve = findById(id);
        mapper.update(dto, eleve);
        return mapper.toDto(eleveRepository.save(eleve));
    }


    public void Supprimer(Long id) {
        Eleve eleve = findById(id);
        eleveRepository.delete(eleve);
    }

    private Eleve findById(Long id) {
        return eleveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Eleve introuvable"));
    }
}
