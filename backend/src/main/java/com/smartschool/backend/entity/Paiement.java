package com.smartschool.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "paiement")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Paiement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long eleveId;

    @Column(nullable = false)
    private Long parentId;

    @Column(nullable = false)
    private BigDecimal montant;

    @Column(nullable = false)
    private String methode;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPaiement statut;

    private String urlJustificatif;




}
