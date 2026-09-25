package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Role;
import com.smartschool.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    Page<User> findByRole(Role role, Pageable pagination);

    Page<User> findByRoleAndNomCompletContainingIgnoreCase(Role role, String nomComplet, Pageable pagination);
}
