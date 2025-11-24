package com.university.Management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; 
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class Suoritekortti {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nimi; 
    
    @Lob 
    private String sisaltoJson; 

    // TÄRKEIN MUUTOS: Lisätään JsonIgnore, jotta vältetään ikuinen silmukka.
    // Muuten JSON-sarjalistus yrittää listata Kayttajan, joka taas viittaa Suoritekortteihin jne.
    @ManyToOne(fetch = FetchType.LAZY) // FetchType.LAZY on suositeltu
    @JoinColumn(name = "luoja_id", nullable = false)
    @JsonIgnoreProperties({"suoritekortit", "lokikirjat", "salasanaHash"}) // Poistaa toistuvat kentät vastauksesta
    private Kayttaja luoja; 
}