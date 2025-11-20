package com.university.Management.controller;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Lokikirja;
import com.university.Management.model.Rooli;
import com.university.Management.repository.KayttajaRepository;
import com.university.Management.repository.LokikirjaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/lokikirjat") // Rajapinta /api/lokikirjat
public class LokikirjaController {

    @Autowired
    private LokikirjaRepository lokikirjaRepository;

    @Autowired
    private KayttajaRepository kayttajaRepository;

    // --- RAJAPINNAT ---

    /**
     * Rajapinta uuden Lokikirjan (Kurssin/Vihkon) luomiseen.
     * Vastaa mm. CreateCourse_1.jsx ja CreateCourse_2.jsx-komponenttien tarpeisiin.
     * POST /api/lokikirjat
     */
    @PostMapping
    public ResponseEntity<?> luoUusiLokikirja(@RequestBody LokikirjaLuojaRequest request) {
        
        // 1. Varmista, että oppilas (kayttaja) löytyy ja on rooliltaan OPPILAS
        Optional<Kayttaja> oppilasOpt = kayttajaRepository.findById(request.oppilasId);
        
        if (oppilasOpt.isEmpty() || oppilasOpt.get().getRooli() != Rooli.OPPILAS) {
            return new ResponseEntity<>("Oppilasta ei löydy tai rooli virheellinen.", HttpStatus.BAD_REQUEST);
        }

        // 2. Luo uusi Lokikirja-olio
        Lokikirja uusiLokikirja = new Lokikirja();
        uusiLokikirja.setNimi(request.nimi);
        uusiLokikirja.setOppilas(oppilasOpt.get());

        // 3. Tallenna tietokantaan
        Lokikirja tallennettuLokikirja = lokikirjaRepository.save(uusiLokikirja);

        // 4. Palauta onnistunut vastaus
        return new ResponseEntity<>(tallennettuLokikirja, HttpStatus.CREATED);
    }
    
    /**
     * Rajapinta tietyn oppilaan Lokikirjojen hakemiseen.
     * GET /api/lokikirjat/oppilas/{oppilasId}
     */
    @GetMapping("/oppilas/{oppilasId}")
    public ResponseEntity<List<Lokikirja>> haeOppilaanLokikirjat(@PathVariable Long oppilasId) {
        List<Lokikirja> lokikirjat = lokikirjaRepository.findByOppilasId(oppilasId);
        return ResponseEntity.ok(lokikirjat);
    }

    // --- DATA TRANSFER OBJECT (DTO) ---
    
    public static class LokikirjaLuojaRequest {
        public String nimi;
        public Long oppilasId; // Oppilaan ID, jolle lokikirja luodaan
    }
}