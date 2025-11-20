package com.university.Management.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Suoritus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // KORJATTU: Käyttää nyt SuoritusTila-Enumia
    @Enumerated(EnumType.STRING) // TÄMÄ TALLENTAA ENUMIN TEKSTINÄ TIETOKANTAAN
    private SuoritusTila tila = SuoritusTila.KESKEN; // HUOM: Vaatii KESKEN-tilan Enumissa

    // Mikä kortti on suoritettu
    @ManyToOne
    @JoinColumn(name = "kortti_id", nullable = false)
    private Suoritekortti kortti; 

    // Missä Lokikirjassa suoritus tapahtuu
    @ManyToOne
    @JoinColumn(name = "lokikirja_id", nullable = false)
    private Lokikirja lokikirja; 

    // Viite opettajaan, joka on tehnyt kuittauksen (voi olla null ennen kuittausta)
    @ManyToOne
    @JoinColumn(name = "arvioija_id")
    private Kayttaja arvioija; 

    private LocalDateTime kuittausAika;
    
    // Lombokin @Data luo setTila(SuoritusTila tila) ja getTila() -metodit automaattisesti
}