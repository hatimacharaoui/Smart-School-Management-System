package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Eleve;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EleveRepository extends JpaRepository<Eleve, Long> {

    Page<Eleve> findByPrenomContainingIgnoreCaseOrNomContainingIgnoreCase(String prenom, String nom, Pageable pagination);
    List<Eleve> findByClasseId(Long classeId);
    List<Eleve> findByParentId(Long parentId);
    boolean existsByClasseId(Long classeId);
    boolean existsByParentId(Long parentId);

}
