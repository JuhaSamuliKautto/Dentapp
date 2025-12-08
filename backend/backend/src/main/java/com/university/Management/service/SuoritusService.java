package com.university.Management.service;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Lokikirja;
import com.university.Management.model.Suoritus;
import com.university.Management.model.SuoritusTila;
import com.university.Management.repository.SuoritusRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SuoritusService {

    private final SuoritusRepository suoritusRepository;
    private final LokikirjaService lokikirjaService; // Tarvitaan Lokikirjan hakuun ja omistajuuden tarkistukseen

    public SuoritusService(
            SuoritusRepository suoritusRepository,
            LokikirjaService lokikirjaService) {
        this.suoritusRepository = suoritusRepository;
        this.lokikirjaService = lokikirjaService;
    }

    // --- CRUD ja Hakumetodit ---

    public Suoritus save(Suoritus suoritus) {
        return suoritusRepository.save(suoritus);
    }

    public Optional<Suoritus> findById(Long id) {
        return suoritusRepository.findById(id);
    }

    public List<Suoritus> findByLokikirjaId(Long lokikirjaId) {
        return suoritusRepository.findByLokikirjaId(lokikirjaId);
    }
    
    public void deleteById(Long id) {
        suoritusRepository.deleteById(id);
    }

    // --- Liiketoimintalogiikka ---

    /**
     * Tarkistaa, onko Lokikirja olemassa annetulla ID:llä.
     */
    public Optional<Lokikirja> findLokikirjaById(Long lokikirjaId) {
        return lokikirjaService.findById(lokikirjaId);
    }

    /**
     * Delegointi: Tarkistaa, onko annettu käyttäjä (Kayttaja) Lokikirjan omistaja.
     */
    public boolean isLokikirjanOmistaja(Long lokikirjaId, Long kayttajaId) {
        return lokikirjaService.isLokikirjanOmistaja(lokikirjaId, kayttajaId);
    }

    /**
     * Arvioi suorituksen (Opettaja-roolille).
     */
    public Optional<Suoritus> arvioiSuoritus(Long suoritusId, Kayttaja arvioija, SuoritusTila tila, String palaute) {
        return suoritusRepository.findById(suoritusId).map(suoritus -> {
            
            // Päivitetään tila ja arvioija
            suoritus.setTila(tila);
            suoritus.setArvioija(arvioija);
            suoritus.setKuittausAika(LocalDateTime.now());
            suoritus.setPalaute(palaute);
            
            return suoritusRepository.save(suoritus);
        });
    }
}
