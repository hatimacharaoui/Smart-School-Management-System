package com.smartschool.backend.service;

import com.smartschool.backend.dto.AffectationClassesEnseignantDto;
import com.smartschool.backend.dto.ClasseEnseignantDto;
import com.smartschool.backend.entity.ClasseEnseignant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClasseEnseignantService {

    Page<ClasseEnseignantDto> lister(Pageable pagination);

    Page<ClasseEnseignantDto> chercherParEnseignant(Long enseignantId, Pageable pagination);

    List<ClasseEnseignantDto> remplacerAffectations(Long enseignantId, AffectationClassesEnseignantDto affectation);
}
