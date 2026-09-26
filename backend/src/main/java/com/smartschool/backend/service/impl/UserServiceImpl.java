package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.UserDto;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.entity.User;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.UserRepository;
import com.smartschool.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Mappers mappers;

    public Page<UserDto> rechercher(Role role, String recherche, Pageable pagination) {
        if (recherche != null && !recherche.isBlank()) {
            return userRepository.findByRoleAndNomCompletContainingIgnoreCase(role, recherche, pagination
            ).map(mappers::toDto);
        } else {
            return userRepository.findByRole(role, pagination).map(mappers::toDto);
        }
    }

    @Cacheable(value = "utilisateurs", key = "'id-' + #id")
    public UserDto chercherParId(Long id) {
        return mappers.toDto(findById(id));
    }

    @Caching(evict = {
            @CacheEvict(value = "utilisateurs", allEntries = true),
            @CacheEvict(value = "enseignants", allEntries = true),
            @CacheEvict(value = "parents", allEntries = true),
            @CacheEvict(value = "eleves", allEntries = true)
    })
    public UserDto modifierProfil(Long id, UserDto donnees) {
        User user = findById(id);
        mappers.update(donnees, user);
        return mappers.toDto(userRepository.save(user));
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }
}
