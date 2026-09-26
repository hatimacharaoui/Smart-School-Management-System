package com.smartschool.backend.service;

import com.smartschool.backend.dto.PaiementDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface JustificatifPaiementService {

    PaiementDto enregistrer(Long paiementId, MultipartFile fichier);

    Resource charger(Long paiementId);

    String determinerTypeContenu(Resource fichier);

}
