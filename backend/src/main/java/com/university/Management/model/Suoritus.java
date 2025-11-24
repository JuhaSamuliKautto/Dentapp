package com.university.Management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data; // TARVITAAN settereiden luomiseen
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; // TARVITAAN KuittausAika-kentälle

@Entity
@Data // TÄMÄ LUO PUUTTUVAT SETTERIT (setLokikirja, setKortti, setTila jne.)
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Suoritus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- RIIPPUVUUDET (Korjaavat setLokikirja/setSuoritekortti virheet) ---
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lokikirja_id")
    private Lokikirja lokikirja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kortti_id")
    private Suoritekortti suoritekortti; // HUOM: Kentän nimi on suoritekortti, EI kortti

    // --- UUDET KENTÄT (Korjaavat setTila/setArvioija/setKuittausAika virheet) ---

    // setTila (SuoritusTila on enum, joka sinun on luotava)
    @Enumerated(EnumType.STRING)
    private SuoritusTila tila = SuoritusTila.ALOITETTU;

    // setKuittausAika (LocalDateTime)
    private LocalDateTime kuittausAika;

    // setArvioija (Kayttaja)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arvioija_id")
    private Kayttaja arvioija; // TÄMÄ LUO setArvioija-metodin

}