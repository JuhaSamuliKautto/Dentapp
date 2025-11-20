package com.university.Management.repository;

import com.university.Management.model.Suoritus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SuoritusRepository extends JpaRepository<Suoritus, Long> {
    
    // Hae kaikki suoritukset, jotka kuuluvat tiettyyn Lokikirjaan
    List<Suoritus> findByLokikirjaId(Long lokikirjaId);
}
