package com.university.Management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data; // Sisältää @Getter, @Setter, @ToString, @EqualsAndHashCode
import lombok.Getter;
import lombok.Setter;

/**
 * Kayttaja-entiteetti (Käyttäjä).
 */
@Entity
@Table(name = "kayttaja")
// Käytetään erikseen Getter/Setter varmistamaan, että kaikki Lombokin osat toimivat
@Getter
@Setter 
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class Kayttaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; 

    @JsonIgnore 
    @Column(nullable = false)
    private String salasana; 

    private String nimi; 
    private String email; 

    @Column(nullable = false)
    private String role; 
    
    // --- KUSTOMOIDUT OSAT ---

    // 1. Konstruktori (tarvitaan JPA:ta varten)
    public Kayttaja() {
    }

    /**
     * 2. Konstruktori käytettäväksi rekisteröinnissä/stubissa, ILMAN ID:tä.
     * KORJATTU: LISÄTTY salasana.
     */
    public Kayttaja(String username, String salasana, String nimi, String email, String role) {
        this.username = username;
        this.salasana = salasana; // <--- KRIITTINEN LISÄYS
        this.nimi = nimi;
        this.email = email;
        this.role = role;
    }

    // Poistettu getSalasana-metodin manuaalinen lisäys, koska @Getter hoitaa sen.
    // (Vaikka @JsonIgnore estää sen JSON-vastauksissa, Spring Security tarvitsee sen.)

    /**
     * Palautettu isTilapäinen (Stub-metodi).
     */
    public boolean isTilapäinen() { 
        return false; 
    }
}