package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.EnseignantDto;
import com.smartschool.backend.entity.Enseignant;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.*;
import com.smartschool.backend.service.EnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnseignantServiceImpl implements EnseignantService {
    private final EnseignantRepository enseignantRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mappers mappers;


    public Page<EnseignantDto> afficherEnseignants(String recherche, Pageable pageable) {
        if (recherche == null || recherche.isBlank()) {
            return enseignantRepository.findAll(pageable)
                    .map(mappers::toDto);
        }
        return enseignantRepository.findByPrenomContainingIgnoreCaseOrNomContainingIgnoreCase(
                recherche, recherche, pageable).map(mappers::toDto);

    }


    @Cacheable(value = "enseignants", key = "'id-' + #id")
    public EnseignantDto chercherParId(Long id) {

        return mappers.toDto(findById(id));
    }


    @Override
    @Caching(evict = {
            @CacheEvict(value = "enseignants", allEntries = true),
            @CacheEvict(value = "utilisateurs", allEntries = true)
    })
    public EnseignantDto enregistrer(EnseignantDto dto) {
        if (dto.getMotDePasse() == null || dto.getMotDePasse().isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire.");
        }
        Enseignant enseignant = mappers.toEntite(dto);
        enseignant.setNomComplet(enseignant.getPrenom() + " " + enseignant.getNom());
        enseignant.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        enseignant.setRole(Role.ENSEIGNANT);
        enseignant.setActif(true);
        return mappers.toDto(enseignantRepository.save(enseignant));
    }


    @Caching(evict = {
            @CacheEvict(value = "enseignants", allEntries = true),
            @CacheEvict(value = "utilisateurs", allEntries = true)
    })
    public EnseignantDto modifier(Long id, EnseignantDto dto) {
        Enseignant enseignant = findById(id);
        mappers.update(dto, enseignant);
        enseignant.setNomComplet(dto.getPrenom() + " " + dto.getNom());
        return mappers.toDto(enseignantRepository.save(enseignant));
    }


    @Caching(evict = {
            @CacheEvict(value = "enseignants", allEntries = true),
            @CacheEvict(value = "utilisateurs", allEntries = true)
    })
    public void supprimer(Long id) {
        Enseignant enseignant = findById(id);

        enseignantRepository.delete(enseignant);
    }

    private Enseignant findById(Long id) {
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));
    }
}
