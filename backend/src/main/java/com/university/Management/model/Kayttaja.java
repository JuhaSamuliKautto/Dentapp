package com.university.Management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // UUSI RIVI
import jakarta.persistence.*;
import lombok.Data; 

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // UUSI RIVI
@Data 
public class Kayttaja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String kayttajatunnus;
    
    @Column(nullable = false)
    private String salasanaHash; 
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rooli rooli; // OPETTAJA tai OPPILAS
}