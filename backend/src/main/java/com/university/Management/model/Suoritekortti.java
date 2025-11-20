package com.university.Management.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Suoritekortti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nimi; // Esim. "Web-kehityksen perusteet"
    
    // Kortin yksityiskohtainen sisältö (Ohjeet, kriteerit jne.)
    @Lob 
    private String sisaltoJson; 

    // Viite luoneeseen opettajaan
    @ManyToOne
    @JoinColumn(name = "luoja_id", nullable = false)
    private Kayttaja luoja; 
}
