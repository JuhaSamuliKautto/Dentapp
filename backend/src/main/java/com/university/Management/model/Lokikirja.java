package com.university.Management.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Lokikirja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nimi; // Kurssin/vihkon nimi
    
    // Viite oppilaaseen, jolle tämä lokikirja kuuluu
    @ManyToOne
    @JoinColumn(name = "oppilas_id", nullable = false)
    private Kayttaja oppilas; 
}
