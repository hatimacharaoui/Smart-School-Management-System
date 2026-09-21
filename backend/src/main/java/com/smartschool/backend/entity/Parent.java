package com.smartschool.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Entity
@Table(name = "parent")
@PrimaryKeyJoinColumn(name = "id")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Parent extends User {

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;


}
