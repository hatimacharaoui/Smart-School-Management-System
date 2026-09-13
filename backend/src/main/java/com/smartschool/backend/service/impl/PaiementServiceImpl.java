package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.PaiementDto;
import com.smartschool.backend.entity.Paiement;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.PaiementRepository;
import com.smartschool.backend.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements PaiementService {
    private final PaiementRepository paiementRepository;
    private final Mappers mappers;

    public Page<PaiementDto> afficher(Pageable pagination) {
        return paiementRepository.findAll(pagination)
                .map(mappers::toDto);
    }

    public PaiementDto creer(PaiementDto dto) {
        Paiement paiement = mappers.toEntite(dto);

        return mappers.toDto(paiementRepository.save(paiement));
    }

    public PaiementDto modifier(Long id, PaiementDto dto) {
        Paiement paiement = findById(id);
        mappers.update(dto, paiement);
        return mappers.toDto(paiementRepository.save(paiement));
    }

    public void supprimer(Long id) {
        Paiement paiement = findById(id);
        paiementRepository.delete(paiement);
    }

    private Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement introuvalbe"));
    }
}
