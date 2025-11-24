package com.university.Management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // UUSI RIVI
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // UUSI RIVI
public class Suoritekortti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nimi; 
    
    @Lob 
    private String sisaltoJson; 

    @ManyToOne
    @JoinColumn(name = "luoja_id", nullable = false)
    private Kayttaja luoja; 
}