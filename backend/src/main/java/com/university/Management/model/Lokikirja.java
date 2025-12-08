package com.university.Management.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // TÄRKEÄ: Estää ikuiset silmukat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set; // Käytetään Setiä listan sijaan

/**
 * Lokikirja-entiteetti, joka pitää kirjaa opiskelijan (Kayttaja) suorituksista.
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class Lokikirja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1. VIITTAUS OMISTAJAKÄYTTÄJÄÄN (STUDENT)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kayttaja_id")
    // TÄRKEÄ: LokikirjaController tarvitsee tämän tiedon
    private Kayttaja kayttaja; 

    // 2. VIITTAUS KURSSIIN (Valinnainen, mutta suositeltava)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kurssi_id")
    private Kurssi kurssi; 

    @Lob 
    private String sisalto; 
    
    private LocalDateTime luontiAika = LocalDateTime.now();

    // 3. SUORITUKSET: Viittaus kaikkiin tähän Lokikirjaan tehtyihin suorituksiin.
    // TÄMÄ ON KRIITTINEN LISÄYS LokikirjaControllerin ja SuoritusControllerin toimintaan.
    @OneToMany(mappedBy = "lokikirja", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Estä ikuinen silmukka Lokikirja <-> Suoritus
    private Set<Suoritus> suoritukset = new HashSet<>();

    // --- GETTERIT JA SETTERIT ---
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Kayttaja getKayttaja() { return kayttaja; }
    public void setKayttaja(Kayttaja kayttaja) { this.kayttaja = kayttaja; }
    
    public Kurssi getKurssi() { return kurssi; }
    public void setKurssi(Kurssi kurssi) { this.kurssi = kurssi; }
    
    public String getSisalto() { return sisalto; }
    public void setSisalto(String sisalto) { this.sisalto = sisalto; }
    
    public LocalDateTime getLuontiAika() { return luontiAika; }
    public void setLuontiAika(LocalDateTime luontiAika) { this.luontiAika = luontiAika; }

    public Set<Suoritus> getSuoritukset() { return suoritukset; }
    public void setSuoritukset(Set<Suoritus> suoritukset) { this.suoritukset = suoritukset; }
}