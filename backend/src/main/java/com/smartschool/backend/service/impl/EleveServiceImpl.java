package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.EleveDto;
import com.smartschool.backend.entity.Eleve;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.EleveRepository;
import com.smartschool.backend.repository.UserRepository;
import com.smartschool.backend.service.EleveService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EleveServiceImpl implements EleveService {
    private final EleveRepository eleveRepository;
    private final Mappers mapper;
    private final UserRepository userRepository;

    public Page<EleveDto> chercherParParent(Long parentId, Pageable pageable) {

        return eleveRepository.findByParentId(parentId, pageable)
                .map(mapper::toDto);
    }

    public Page<EleveDto> afficher(Pageable pagination) {

        return eleveRepository.findAll(pagination).map(mapper::toDto);
    }


    public EleveDto creer(EleveDto dto) {
        Eleve eleve = mapper.toEntite(dto);

        return mapper.toDto(eleveRepository.save(eleve));
    }


    public EleveDto modifier(Long id, EleveDto dto) {
        Eleve eleve = findById(id);
        mapper.update(dto, eleve);
        return mapper.toDto(eleveRepository.save(eleve));
    }


    public void Supprimer(Long id) {
        Eleve eleve = findById(id);
        eleveRepository.delete(eleve);
    }

    private Eleve findById(Long id) {
        return eleveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Eleve introuvable"));
    }
}
