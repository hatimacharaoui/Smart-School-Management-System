package com.smartschool.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "eleve")
@PrimaryKeyJoinColumn(name = "id")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Eleve extends User{

    @Column(nullable = false, unique = true)
    private String matricule;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    private LocalDate dateNaissance;

    private String adresse;

    @Column(nullable = false)
    private Long classeId;

    @Column(nullable = false)
    private Long parentId;


}
