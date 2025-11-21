package com.university.Management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Lokikirja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kayttaja_id")
    private Kayttaja kayttaja; 

    @Lob 
    private String sisalto; 
    
    private LocalDateTime luontiAika = LocalDateTime.now();

    // --- GETTERIT JA SETTERIT ---
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Kayttaja getKayttaja() { return kayttaja; }
    public void setKayttaja(Kayttaja kayttaja) { this.kayttaja = kayttaja; }
    public String getSisalto() { return sisalto; }
    public void setSisalto(String sisalto) { this.sisalto = sisalto; }
    public LocalDateTime getLuontiAika() { return luontiAika; }
    public void setLuontiAika(LocalDateTime luontiAika) { this.luontiAika = luontiAika; }
}