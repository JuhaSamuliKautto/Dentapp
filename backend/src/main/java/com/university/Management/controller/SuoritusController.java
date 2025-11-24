package com.university.Management.controller;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Lokikirja;
import com.university.Management.model.Suoritus;
import com.university.Management.model.Suoritekortti;
import com.university.Management.model.SuoritusTila;
import com.university.Management.repository.KayttajaRepository;
import com.university.Management.repository.LokikirjaRepository;
import com.university.Management.repository.SuoritusRepository;
import com.university.Management.repository.SuoritekorttiRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // TÄMÄ UUSI IMPORT
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// HUOM: Tarvitaan, jotta @PreAuthorize toimii!
// Tämä tehdään seuraavassa kohdassa SecurityConfigissa.
// Oletetaan, että se on jo tehty, jotta tämä koodi toimii.

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/suoritukset")
public class SuoritusController {
    // ... (Repository-injektiot pysyvät ennallaan)
    @Autowired private SuoritusRepository suoritusRepository;
    @Autowired private LokikirjaRepository lokikirjaRepository;
    @Autowired private SuoritekorttiRepository suoritekorttiRepository;
    @Autowired private KayttajaRepository kayttajaRepository;


    // --- RAJAPINNAT ---

    /**
     * Rajapinta uuden Suorituksen luomiseen. Vain OPPILAS saa luoda uuden suorituksen.
     * POST /api/suoritukset
     */
    @PostMapping
    @PreAuthorize("hasRole('OPPILAS')") // VAATII: Käyttäjällä on rooli 'OPPILAS'
    public ResponseEntity<?> luoUusiSuoritus(@RequestBody SuoritusRequest request) {
        
        // ... (Logiikka pysyy ennallaan)

        // 1. Varmista, että lokikirja ja kortti löytyvät
        Optional<Lokikirja> lokikirjaOpt = lokikirjaRepository.findById(request.lokikirjaId);
        Optional<Suoritekortti> korttiOpt = suoritekorttiRepository.findById(request.korttiId); 

        if (lokikirjaOpt.isEmpty() || korttiOpt.isEmpty()) {
            return new ResponseEntity<>("Lokikirjaa tai Suoritekorttia ei löydy.", HttpStatus.BAD_REQUEST);
        }

        // 2. Luo uusi Suoritus-olio
        Suoritus uusiSuoritus = new Suoritus();
        uusiSuoritus.setLokikirja(lokikirjaOpt.get());
        uusiSuoritus.setSuoritekortti(korttiOpt.get()); 
        
        uusiSuoritus.setTila(SuoritusTila.ODOTTAA_HYVAKSYNTAA);
        uusiSuoritus.setKuittausAika(LocalDateTime.now());
        
        // 3. Tallenna tietokantaan
        Suoritus tallennettuSuoritus = suoritusRepository.save(uusiSuoritus);

        // 4. Palauta onnistunut vastaus
        return new ResponseEntity<>(tallennettuSuoritus, HttpStatus.CREATED);
    }
    
    /**
     * Rajapinta suorituksen päivittämiseen (opettaja arvioi kuittauksen).
     * PUT /api/suoritukset/{suoritusId}/arvioi
     */
    @PutMapping("/{suoritusId}/arvioi")
    @PreAuthorize("hasRole('OPETTAJA')") // VAATII: Käyttäjällä on rooli 'OPETTAJA'
    public ResponseEntity<?> arvioiSuoritus(
            @PathVariable Long suoritusId, 
            @RequestBody ArvioiSuoritusRequest request) {

        // ... (Logiikka pysyy ennallaan)
        
        // 1. Etsi suoritus
        Optional<Suoritus> suoritusOpt = suoritusRepository.findById(suoritusId);
        if (suoritusOpt.isEmpty()) {
            return new ResponseEntity<>("Suoritusta ei löydy.", HttpStatus.NOT_FOUND);
        }
        Suoritus suoritus = suoritusOpt.get();
        
        // 2. Varmista, että arvioija (opettaja) löytyy - HUOM: PreAuthorize hoitaa roolitarkistuksen
        Optional<Kayttaja> arvioijaOpt = kayttajaRepository.findById(request.arvioijaId);
        if (arvioijaOpt.isEmpty()) { 
             // TÄRKEÄ: Poistetaan vanha manuaalinen roolitarkistus, koska PreAuthorize tekee sen paremmin
             return new ResponseEntity<>("Arvioijaa ei löydy.", HttpStatus.BAD_REQUEST);
        }
        
        // 3. Päivitä tiedot
        suoritus.setTila(request.tila); 
        suoritus.setArvioija(arvioijaOpt.get());
        
        // 4. Tallenna ja palauta
        Suoritus paivitettySuoritus = suoritusRepository.save(suoritus);
        return ResponseEntity.ok(paivitettySuoritus);
    }

    /**
     * Rajapinta kaikkien suoritusten hakemiseen tietyn Lokikirjan perusteella.
     * GET /api/suoritukset/lokikirja/{lokikirjaId}
     */
    @GetMapping("/lokikirja/{lokikirjaId}")
    @PreAuthorize("hasAnyRole('OPETTAJA', 'OPPILAS')") // SALLII MOLEMMAT ROOLIT
    public ResponseEntity<List<Suoritus>> haeSuorituksetLokikirjasta(@PathVariable Long lokikirjaId) {
        // HUOM: Tässä metodissa ei ole vanhaa GET /api/suoritukset -rajapintaa,
        // joten testataan tällä
        List<Suoritus> suoritukset = suoritusRepository.findByLokikirjaId(lokikirjaId); 
        return ResponseEntity.ok(suoritukset);
    }
    
    // --- DATA TRANSFER OBJECTS (DTO) ---
    
    // ... (DTO:t pysyvät ennallaan)
    public static class SuoritusRequest {
        public Long lokikirjaId;
        public Long korttiId;
    }

    public static class ArvioiSuoritusRequest {
        public SuoritusTila tila;
        public Long arvioijaId;
    }
}