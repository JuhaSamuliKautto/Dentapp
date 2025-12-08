package com.university.Management.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // TÄRKEÄ IMPORT
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

/**
 * Suoritekortti-entiteetti (Task template).
 */
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
    private String sisaltoJson; // Kortin varsinainen sisältö esim. JSON-muodossa

    /**
     * Relaatio luojaan (Kayttaja/Opettaja).
     * LISÄTTY @JsonIgnore estämään JSON-sarjalistuksen ikuinen silmukka.
     */
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "luoja_id", nullable = false)
    @JsonIgnore // <--- KRIITTINEN LISÄYS
    private Kayttaja luoja; 
    
    /**
     * LISÄYS: Viittaus kaikkiin Suorituksiin, joissa tätä korttia on käytetty.
     */
    @OneToMany(mappedBy = "suoritekortti", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Suoritus> suoritukset = new HashSet<>();
}