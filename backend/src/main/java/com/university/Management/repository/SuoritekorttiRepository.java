package com.university.Management.repository;

import com.university.Management.model.Suoritekortti;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SuoritekorttiRepository extends JpaRepository<Suoritekortti, Long> {
    
    // Hae kaikki kortit, jotka tietty opettaja (luoja) on luonut
    List<Suoritekortti> findByLuojaId(Long luojaId);
}