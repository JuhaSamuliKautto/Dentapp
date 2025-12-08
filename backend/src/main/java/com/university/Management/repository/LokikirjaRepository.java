package com.university.Management.repository;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Lokikirja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // HUOM: @Repository annotaatio puuttui, lisätty
public interface LokikirjaRepository extends JpaRepository<Lokikirja, Long> {
    
    /**
     * Hakee kaikki Lokikirjat, jotka kuuluvat tietylle Kayttaja-oliolle (omistajalle).
     */
    List<Lokikirja> findByKayttaja(Kayttaja kayttaja);

    /**
     * UUSI METODI: Tarkistaa tehokkaasti, onko Lokikirja (ID) tietyn Kayttajan (ID) omistuksessa.
     * Käytetään LokikirjaServicessa valtuutuksen tarkistamiseen.
     */
    boolean existsByIdAndKayttajaId(Long id, Long kayttajaId); // <-- KRIITTINEN LISÄYS
}