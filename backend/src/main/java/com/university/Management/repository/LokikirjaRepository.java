package com.university.Management.repository;

import com.university.Management.model.Kayttaja; // TÄRKEÄ IMPORT
import com.university.Management.model.Lokikirja;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // TÄRKEÄ IMPORT

public interface LokikirjaRepository extends JpaRepository<Lokikirja, Long> {
    
    // UUSI METODI: Hakee kaikki lokikirjat Kayttaja-olion perusteella
    // Spring Data JPA luo toteutuksen automaattisesti kentän nimen perusteella.
    List<Lokikirja> findByKayttaja(Kayttaja kayttaja); 
    
}