package com.university.Management.repository;

import com.university.Management.model.Lokikirja;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LokikirjaRepository extends JpaRepository<Lokikirja, Long> {
    
    // Hae kaikki Lokikirjat, jotka kuuluvat tietylle oppilaalle
    List<Lokikirja> findByOppilasId(Long oppilasId);
}
