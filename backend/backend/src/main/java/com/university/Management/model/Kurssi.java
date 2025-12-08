package com.university.Management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data; 
import java.util.HashSet;
import java.util.Set;

/**
 * Kurssi-entiteetti (Kurssi).
 */
@Entity
@Table(name = "kurssi")
@Data 
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Kurssi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nimi;

    @Lob 
    private String kuvaus;

    /**
     * Relaatio Kayttaja-olioon (VastuuOpettaja).
     * KORJAUS: CascadeType.ALL on poistettu, jotta opettajaa ei poisteta kurssin poiston yhteydessä.
     */
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "vastuu_opettaja_id", nullable = false)
    private Kayttaja vastuuOpettaja;

    /**
     * LISÄYS: Viittaus kaikkiin Lokikirjoihin, jotka on luotu tälle kurssille.
     * TÄMÄ ON TÄRKEÄÄ LokikirjaControllerin ja arviointijärjestelmän kannalta.
     */
    @OneToMany(mappedBy = "kurssi", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Vältä ikuinen silmukka Kurssi <-> Lokikirja
    private Set<Lokikirja> lokikirjat = new HashSet<>();

    // Default-konstruktori (tarvitaan JPA:ta varten)
    public Kurssi() {
    }
    
    // Lombokin @Data hoitaa loput getterit ja setterit automaattisesti.
}