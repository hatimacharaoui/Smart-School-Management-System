package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.ParentDto;
import com.smartschool.backend.entity.Parent;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.EleveRepository;
import com.smartschool.backend.repository.PaiementRepository;
import com.smartschool.backend.repository.ParentRepository;
import com.smartschool.backend.service.ParentService;
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
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mappers mappers;

    @Override
    public Page<ParentDto> lister(String recherche, Pageable pagination) {
        if (recherche == null || recherche.isBlank()) {
            return parentRepository.findAll(pagination).map(mappers::toDto);
        }
        return parentRepository.findByPrenomContainingIgnoreCaseOrNomContainingIgnoreCase(
                recherche,
                recherche,
                pagination
        ).map(mappers::toDto);
    }

    @Override
    @Cacheable(value = "parents", key = "'id-' + #id")
    public ParentDto chercherParId(Long id) {
        return mappers.toDto(findById(id));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "parents", allEntries = true),
            @CacheEvict(value = "utilisateurs", allEntries = true)
    })
    public ParentDto enregistrer(ParentDto dto) {
        if (dto.getMotDePasse() == null || dto.getMotDePasse().isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire.");
        }
        Parent parent = mappers.toEntite(dto);
        parent.setNomComplet(parent.getPrenom() + " " + parent.getNom());
        parent.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        parent.setRole(Role.PARENT);
        parent.setActif(true);
        return mappers.toDto(parentRepository.save(parent));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "parents", allEntries = true),
            @CacheEvict(value = "utilisateurs", allEntries = true)
    })
    public ParentDto modifier(Long id, ParentDto dto) {
        Parent parent = findById(id);
        mappers.update(dto, parent);
        parent.setNomComplet(dto.getPrenom() + " " + dto.getNom());
        return mappers.toDto(parentRepository.save(parent));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "parents", allEntries = true),
            @CacheEvict(value = "utilisateurs", allEntries = true)
    })
    public void supprimer(Long id) {
        Parent parent = findById(id);

        parentRepository.delete(parent);
    }

    private Parent findById(Long id) {
        return parentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent introuvable"));
    }
}
