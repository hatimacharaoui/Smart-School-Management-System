package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Devoir;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevoirRepository extends JpaRepository<Devoir, Long> {

}
