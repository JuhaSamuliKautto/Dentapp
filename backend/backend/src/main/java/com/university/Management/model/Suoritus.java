package com.university.Management.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // UUSI IMPORT
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data; 
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Suoritus-entiteetti (Task performance).
 * Pitää kirjaa yksittäisen Suoritekortin toteutuksesta tietyssä Lokikirjassa.
 */
@Entity
@Data 
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Suoritus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- RIIPPUVUUDET (Lokikirja, Kortti, Arvioija) ---

    // Viittaus siihen Lokikirjaan, johon tämä suoritus kuuluu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lokikirja_id", nullable = false)
    @JsonIgnore // <-- KRIITTINEN LISÄYS: Estää Lokikirja <-> Suoritus silmukan
    private Lokikirja lokikirja;

    // Viittaus siihen Suoritekorttiin, jota tämä suoritus koskee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kortti_id", nullable = false)
    @JsonIgnore // <-- KRIITTINEN LISÄYS: Estää Kortti <-> Suoritus silmukan
    private Suoritekortti suoritekortti; 

    // Viittaus Opettajaan, joka on arvioinut suorituksen
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arvioija_id")
    @JsonIgnore // <-- KRIITTINEN LISÄYS: Estää Kayttaja <-> Suoritus silmukan
    private Kayttaja arvioija; 

    // --- TILAT JA AJAT ---
    
    // Tila (esim. ODOTTAA_HYVAKSYNTAA, HYVAKSYTTY, HYLATTY)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuoritusTila tila = SuoritusTila.ODOTTAA_HYVAKSYNTAA; // Päivitetään default tila

    // Kenttä suoritteen luomisajalle
    private LocalDateTime luontiAika = LocalDateTime.now(); // <-- UUSI KENTTÄ

    // Kenttä sille ajalle, jolloin arvioija (opettaja) kuittasi suorituksen
    private LocalDateTime kuittausAika; 
    
    // Palautekenttä (esim. 500 merkkiä)
    @Column(length = 500)
    private String palaute; // <-- LISÄTTY PALAUTEKENTTÄ ARVIOINTIA VARTEN
}