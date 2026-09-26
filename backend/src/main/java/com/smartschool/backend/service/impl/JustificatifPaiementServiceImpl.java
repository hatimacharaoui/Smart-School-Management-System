package com.smartschool.backend.service.impl;

import com.smartschool.backend.dto.PaiementDto;
import com.smartschool.backend.entity.Paiement;
import com.smartschool.backend.entity.Role;
import com.smartschool.backend.entity.TypeNotification;
import com.smartschool.backend.entity.User;
import com.smartschool.backend.mapper.Mappers;
import com.smartschool.backend.repository.EleveRepository;
import com.smartschool.backend.repository.HoraireEmploiDuTempRepository;
import com.smartschool.backend.repository.PaiementRepository;
import com.smartschool.backend.repository.UserRepository;
import com.smartschool.backend.service.JustificatifPaiementService;
import com.smartschool.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JustificatifPaiementServiceImpl {
    private final PaiementRepository paiementRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final Mappers mappers;


    @Value("${app.upload.justificatifs}")
    private String dossierJustificatifs;

    @CacheEvict(value = "paiements", allEntries = true)
    public PaiementDto enregistrer(Long paiementId, MultipartFile fichier) {
        Paiement paiement = chercherPaiement(paiementId);
        verifierAcces(paiement);
        verifierFichier(fichier);

        try {
            Path dossier = Paths.get(dossierJustificatifs).toAbsolutePath().normalize();
            Files.createDirectories(dossier);

            String extension = chercherExtension(fichier.getOriginalFilename());
            String nomFichier = "paiement-" + paiementId + "-" + UUID.randomUUID() + extension;
            Path destination = dossier.resolve(nomFichier).normalize();
            Files.copy(fichier.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            paiement.setUrlJustificatif(nomFichier);
            Paiement paiementModifie = paiementRepository.save(paiement);
            notificationService.notifierRole(
                    Role.ADMINISTRATEUR,
                    "Nouveau justificatif de paiement",
                    "Un parent a ajouté un justificatif de paiement.",
                    TypeNotification.PAIEMENT,
                    paiementId
            );
            return mappers.toDto(paiementModifie);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Impossible d’enregistrer le justificatif.");
        }
    }

    public Resource charger(Long paiementId) {
        Paiement paiement = chercherPaiement(paiementId);
        verifierAcces(paiement);

        String nomFichier = paiement.getUrlJustificatif();
        if (nomFichier == null || nomFichier.isBlank() || nomFichier.startsWith("/")) {
            throw new RuntimeException("Justificatif introuvable");
        }

        try {
            Path dossier = Paths.get(dossierJustificatifs).toAbsolutePath().normalize();
            Path chemin = dossier.resolve(nomFichier).normalize();
            if (!chemin.startsWith(dossier)) {
                throw new IllegalArgumentException("Chemin du justificatif invalide.");
            }

            Resource fichier = new UrlResource(chemin.toUri());
            if (!fichier.exists() || !fichier.isReadable()) {
                throw new RuntimeException("Justificatif introuvable");
            }
            return fichier;
        } catch (IOException exception) {
            throw new RuntimeException("Justificatif introuvable");
        }
    }

    public String determinerTypeContenu(Resource fichier) {
        try {
            String type = Files.probeContentType(fichier.getFile().toPath());
            if (type != null) {
                return type;
            }
        } catch (IOException exception) {
            return "application/octet-stream";
        }
        return "application/octet-stream";
    }

    private Paiement chercherPaiement(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));
    }

    private void verifierAcces(Paiement paiement) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalArgumentException("Utilisateur non authentifié.");
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        if (user.getRole() == Role.PARENT
                && !Objects.equals(user.getId(), paiement.getParentId())) {
            throw new IllegalArgumentException("Ce paiement ne concerne pas votre compte.");
        }
    }

    private void verifierFichier(MultipartFile fichier) {
        if (fichier == null || fichier.isEmpty()) {
            throw new IllegalArgumentException("Sélectionnez un justificatif.");
        }
        if (fichier.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Le fichier ne doit pas dépasser 5 Mo.");
        }

        String extension = chercherExtension(fichier.getOriginalFilename()).toLowerCase();
        List<String> extensionsAutorisees = List.of(".pdf", ".png", ".jpg", ".jpeg");
        if (!extensionsAutorisees.contains(extension)) {
            throw new IllegalArgumentException("Formats acceptés : PDF, PNG, JPG et JPEG.");
        }
    }

    private String chercherExtension(String nomOriginal) {
        if (nomOriginal == null) {
            return "";
        }
        int positionPoint = nomOriginal.lastIndexOf('.');
        if (positionPoint < 0) {
            return "";
        }
        return nomOriginal.substring(positionPoint);
    }



}
