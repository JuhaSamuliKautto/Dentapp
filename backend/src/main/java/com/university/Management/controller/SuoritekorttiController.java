package com.university.Management.controller;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Suoritekortti;
import com.university.Management.repository.KayttajaRepository;
import com.university.Management.repository.SuoritekorttiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // TÄRKEÄ UUSI IMPORT
import org.springframework.security.core.Authentication; // TÄRKEÄ UUSI IMPORT
import org.springframework.security.core.context.SecurityContextHolder; // TÄRKEÄ UUSI IMPORT
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

// Sallii pyynnöt React-Front-Endistä
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/suoritekortit") // MUUTETTU: Käytetään johdonmukaista osoitetta
public class SuoritekorttiController {

    @Autowired
    private SuoritekorttiRepository korttiRepository;

    @Autowired
    private KayttajaRepository kayttajaRepository;

    // --- RAJAPINNAT ---

    /**
     * Rajapinta uuden kortin luomiseen. 
     * Opettajan rooli tarkistetaan JWT-tokenista.
     * POST /api/suoritekortit
     */
    @PostMapping
    @PreAuthorize("hasRole('OPETTAJA')") // LISÄTTY: Vain OPETTAJA saa luoda kortteja
    public ResponseEntity<?> luoUusiKortti(@RequestBody KorttiLuojaRequest request) {
        
        // 1. Lue Opettajan käyttäjätunnus JWT-tokenista (turvallinen tapa)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = authentication.getName(); // Käyttäjätunnus on tokenin Subject (Sub)

        // 2. Hae Opettaja tietokannasta Kayttajatunnuksen perusteella
        Optional<Kayttaja> luojaOpt = kayttajaRepository.findByKayttajatunnus(kayttajatunnus);
        
        // TÄRKEÄÄ: Tämä tarkistus on nyt turha, sillä @PreAuthorize on jo tarkistanut roolin.
        // Mutta varmistetaan, että käyttäjä löytyy.
        if (luojaOpt.isEmpty()) {
             return new ResponseEntity<>("Virhe: Opettajaa (luojaa) ei löytynyt JWT-tokenin perusteella.", HttpStatus.FORBIDDEN);
        }
        
        Kayttaja luoja = luojaOpt.get();

        // 3. Luo uusi Suoritekortti-olio
        Suoritekortti uusiKortti = new Suoritekortti();
        uusiKortti.setNimi(request.nimi);
        uusiKortti.setSisaltoJson(request.sisaltoJson);
        uusiKortti.setLuoja(luoja); // Asetetaan luoja JWT-tokenista luetun käyttäjän perusteella

        // 4. Tallenna tietokantaan
        Suoritekortti tallennettuKortti = korttiRepository.save(uusiKortti);

        // 5. Palauta onnistunut vastaus
        return new ResponseEntity<>(tallennettuKortti, HttpStatus.CREATED);
    }
    
    /**
     * Rajapinta kaikkien korttien hakemiseen.
     * GET /api/suoritekortit
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('OPETTAJA', 'OPPILAS')") // LISÄTTY: Molemmat roolit saavat hakea
    public ResponseEntity<List<Suoritekortti>> haeKaikkiKortit() {
        List<Suoritekortti> kortit = korttiRepository.findAll();
        return ResponseEntity.ok(kortit);
    }
    
    /**
     * Rajapinta tietyn opettajan luomien korttien hakemiseen.
     * GET /api/suoritekortit/luoja/{luojaId}
     */
    @GetMapping("/luoja/{luojaId}")
    @PreAuthorize("hasAnyRole('OPETTAJA', 'OPPILAS')") // LISÄTTY: Molemmat roolit saavat hakea
    public ResponseEntity<List<Suoritekortti>> haeKortitLuojaIdnPerusteella(@PathVariable Long luojaId) {
        List<Suoritekortti> kortit = korttiRepository.findByLuojaId(luojaId);
        return ResponseEntity.ok(kortit);
    }

    // --- DATA TRANSFER OBJECT (DTO) ---
    
    // KorttiLuojaRequestin ei tarvitse sisältää enää luojaId:tä, mutta jätetään se 
    // toistaiseksi, jos joku toinen komponentti sitä tarvitsee.
    public static class KorttiLuojaRequest {
        public String nimi;
        public String sisaltoJson;
        // POISTETTU TÄSTÄ KÄYTÖSTÄ: public Long luojaId; 
    }
}