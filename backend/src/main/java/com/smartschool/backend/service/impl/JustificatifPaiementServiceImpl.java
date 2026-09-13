package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.PaiementDto;
import com.smartschool.backend.entity.Paiement;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.EleveRepository;
import com.smartschool.backend.repository.HoraireEmploiDuTempRepository;
import com.smartschool.backend.repository.PaiementRepository;
import com.smartschool.backend.service.JustificatifPaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JustificatifPaiementServiceImpl {
    private final PaiementRepository paiementRepository;
    private final Mappers mappers;



}
