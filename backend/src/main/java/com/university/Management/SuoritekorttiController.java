package com.university.Management;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Suoritekortti;
import com.university.Management.repository.KayttajaRepository;
import com.university.Management.repository.SuoritekorttiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

// Sallii pyynnöt React-Front-Endistä
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/kortit") // Kaikki rajapinnat alkavat /api/kortit
public class SuoritekorttiController {

    @Autowired
    private SuoritekorttiRepository korttiRepository;

    @Autowired
    private KayttajaRepository kayttajaRepository;

    // --- RAJAPINNAT ---

    /**
     * Rajapinta uuden kortin luomiseen. Vastaa CreateCard.jsx-komponentin tarpeisiin.
     * POST /api/kortit
     */
    @PostMapping
    public ResponseEntity<?> luoUusiKortti(@RequestBody KorttiLuojaRequest request) {
        
        // 1. Varmista, että kortin luoja (opettaja) löytyy ja on opettaja
        Optional<Kayttaja> luojaOpt = kayttajaRepository.findById(request.luojaId);
        
        // Tarkistetaan, että käyttäjä löytyy ja rooli on OPETTAJA
        if (luojaOpt.isEmpty() || luojaOpt.get().getRooli() != com.university.Management.model.Rooli.OPETTAJA) {
            return new ResponseEntity<>("Opettajaa (luojaa) ei löydy tai rooli virheellinen.", HttpStatus.FORBIDDEN);
        }

        // 2. Luo uusi Suoritekortti-olio
        Suoritekortti uusiKortti = new Suoritekortti();
        uusiKortti.setNimi(request.nimi);
        uusiKortti.setSisaltoJson(request.sisaltoJson);
        uusiKortti.setLuoja(luojaOpt.get());

        // 3. Tallenna tietokantaan
        Suoritekortti tallennettuKortti = korttiRepository.save(uusiKortti);

        // 4. Palauta onnistunut vastaus
        return new ResponseEntity<>(tallennettuKortti, HttpStatus.CREATED);
    }
    
    /**
     * Rajapinta kaikkien korttien hakemiseen.
     * GET /api/kortit
     */
    @GetMapping
    public ResponseEntity<List<Suoritekortti>> haeKaikkiKortit() {
        List<Suoritekortti> kortit = korttiRepository.findAll();
        return ResponseEntity.ok(kortit);
    }
    
    /**
     * Rajapinta tietyn opettajan luomien korttien hakemiseen.
     * GET /api/kortit/luoja/{luojaId}
     */
    @GetMapping("/luoja/{luojaId}")
    public ResponseEntity<List<Suoritekortti>> haeKortitLuojaIdnPerusteella(@PathVariable Long luojaId) {
        List<Suoritekortti> kortit = korttiRepository.findByLuojaId(luojaId);
        return ResponseEntity.ok(kortit);
    }

    // --- DATA TRANSFER OBJECT (DTO) ---
    
    // Tämä luokka vastaa Reactilta tulevaa pyyntöä
    public static class KorttiLuojaRequest {
        public String nimi;
        public String sisaltoJson;
        public Long luojaId; 
    }
}