package com.university.Management.repository;

import com.university.Management.model.Suoritekortti;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // <-- TÄMÄ IMPORT PUUTTUI LUULTAVASTI

public interface SuoritekorttiRepository extends JpaRepository<Suoritekortti, Long> {
    
    // Hae kaikki kortit, jotka tietty opettaja (luoja) on luonut
    // Metodin nimen tulee vastata Suoritekortti-entiteetin kenttää (luoja) ja sen ID:tä.
    List<Suoritekortti> findByLuojaId(Long luojaId);
}