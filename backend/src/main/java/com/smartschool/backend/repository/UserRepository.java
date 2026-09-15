package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Role;
import com.smartschool.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);
}
