package com.smartschool.backend.service;

import com.smartschool.backend.dto.UserDto;
import com.smartschool.backend.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> rechercher(Role role, String recherche, Pageable pagination);
    UserDto chercherParId(Long id);
    UserDto modifierProfil(Long id, UserDto donnees);
}
