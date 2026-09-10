package com.smartschool.backend.repository;

import com.smartschool.backend.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findByEleveId(Long eleveId);

    boolean existsByParentId(Long parentId);
}
