package com.university.Management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data; 

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@Data 
public class Kayttaja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String kayttajatunnus;
    
    // TÄMÄ MUUTETTU TAKAISIN "salasana"-NIMEEN AUTHCONTROLLERIN TAKIA
    @Column(nullable = false)
    private String salasana; 
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rooli rooli; // OPETTAJA tai OPPILAS
    
    // --- LISÄÄMME NÄMÄ GETTERIT/SETTERIT VARMUUDEN VUOKSI ---
    
    /**
     * @Data luo tämän, mutta explicit antaa selkeyttä.
     * TÄMÄ KORJAA VIRHEEN "The method getSalasana() is undefined for the type Kayttaja".
     */
    public String getSalasana() {
        return salasana;
    }
    
    public void setSalasana(String salasana) {
        this.salasana = salasana;
    }
    
    public Rooli getRooli() {
        return rooli;
    }
    
    public void setRooli(Rooli rooli) {
        this.rooli = rooli;
    }
}