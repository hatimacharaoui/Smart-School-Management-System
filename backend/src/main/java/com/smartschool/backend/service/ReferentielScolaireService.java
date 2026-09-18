package com.smartschool.backend.service;

import com.smartschool.backend.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReferentielScolaireService {

    Page<EnseignantDto> afficherEnseignants(Pageable pagination);

    Page<ParentDto> afficherParents(Pageable pagination);

    Page<ClasseScolaireDto> afficherClasses(Pageable pagination);

    Page<MatiereDto> afficherMatieres(Pageable pagination);

    Page<ClasseEnseignantDto> afficherAffectionsClasses(Pageable pagination);


}
