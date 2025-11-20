package com.university.Management.controller;

import com.university.Management.model.*;
import com.university.Management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/suoritukset") // Rajapinta /api/suoritukset
public class SuoritusController {

    @Autowired
    private SuoritusRepository suoritusRepository;
    
    @Autowired
    private LokikirjaRepository lokikirjaRepository;
    
    @Autowired
    private SuoritekorttiRepository korttiRepository;
    
    @Autowired
    private KayttajaRepository kayttajaRepository;

    // --- RAJAPINNAT ---

    /**
     * Rajapinta uuden Suorituksen luomiseen (oppilas kuittaa kortin valmiiksi).
     * POST /api/suoritukset
     */
    @PostMapping
    public ResponseEntity<?> luoUusiSuoritus(@RequestBody SuoritusRequest request) {
        
        // 1. Varmista, että lokikirja ja kortti löytyvät
        Optional<Lokikirja> lokikirjaOpt = lokikirjaRepository.findById(request.lokikirjaId);
        Optional<Suoritekortti> korttiOpt = korttiRepository.findById(request.korttiId);

        if (lokikirjaOpt.isEmpty() || korttiOpt.isEmpty()) {
            return new ResponseEntity<>("Lokikirjaa tai Suoritekorttia ei löydy.", HttpStatus.BAD_REQUEST);
        }

        // 2. Luo uusi Suoritus-olio
        Suoritus uusiSuoritus = new Suoritus();
        uusiSuoritus.setLokikirja(lokikirjaOpt.get());
        uusiSuoritus.setKortti(korttiOpt.get());
        
        // Aseta alkutila ja kuittausaika (oppilas kuitannut valmiiksi)
        // KUITATTU on nyt SuoritusTila-tyyppiä
        uusiSuoritus.setTila(SuoritusTila.KUITATTU); 
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
    public ResponseEntity<?> arvioiSuoritus(
            @PathVariable Long suoritusId, 
            @RequestBody ArvioiSuoritusRequest request) {

        // 1. Etsi suoritus
        Optional<Suoritus> suoritusOpt = suoritusRepository.findById(suoritusId);
        if (suoritusOpt.isEmpty()) {
            return new ResponseEntity<>("Suoritusta ei löydy.", HttpStatus.NOT_FOUND);
        }
        Suoritus suoritus = suoritusOpt.get();
        
        // 2. Varmista, että arvioija (opettaja) löytyy
        Optional<Kayttaja> arvioijaOpt = kayttajaRepository.findById(request.arvioijaId);
        if (arvioijaOpt.isEmpty() || arvioijaOpt.get().getRooli() != Rooli.OPETTAJA) {
            return new ResponseEntity<>("Arvioijaa ei löydy tai rooli virheellinen.", HttpStatus.FORBIDDEN);
        }
        
        // 3. Päivitä tiedot
        // request.tila on nyt SuoritusTila-tyyppiä
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
    public ResponseEntity<List<Suoritus>> haeSuorituksetLokikirjasta(@PathVariable Long lokikirjaId) {
        List<Suoritus> suoritukset = suoritusRepository.findByLokikirjaId(lokikirjaId);
        return ResponseEntity.ok(suoritukset);
    }
    
    // --- DATA TRANSFER OBJECTS (DTO) ---
    
    // DTO uuden suorituksen luomiseen - KORJATTU
    public static class SuoritusRequest {
        public Long lokikirjaId;
        public Long korttiId;
    }

    // DTO suorituksen arviointiin (opettaja) - KORJATTU
    public static class ArvioiSuoritusRequest {
        public SuoritusTila tila; // Nyt SuoritusTila-tyyppi
        public Long arvioijaId;
    }
}