package com.university.Management.controller;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Lokikirja;
import com.university.Management.service.KayttajaService;
import com.university.Management.service.LokikirjaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/lokikirjat")
@CrossOrigin(origins = "http://localhost:5173")
public class LokikirjaController {

    private final LokikirjaService lokikirjaService;
    private final KayttajaService kayttajaService;
    
    // HUOM: DTO on tässä luokassa, mutta se kannattaa siirtää com.university.Management.dto -pakettiin
    public static class LokikirjaRequest {
        public String sisalto;
    }

    public LokikirjaController(LokikirjaService lokikirjaService, KayttajaService kayttajaService) {
        this.lokikirjaService = lokikirjaService;
        this.kayttajaService = kayttajaService;
    }

    private Kayttaja getAuthenticatedKayttajaOrThrow() {
        return kayttajaService.getAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Käyttäjä ei ole autentikoitu tai löydettävissä."));
    }
    
    // --- APUMETODI VALTUUTUKSEN TARKISTUKSEEN ---
    private boolean checkAuthorization(Long lokikirjaId, Kayttaja kayttaja) {
        boolean isTeacher = kayttaja.getRole().equals("TEACHER");
        
        // Jos opettaja, sallitaan aina
        if (isTeacher) {
            return true;
        }
        
        // Jos opiskelija, tarkistetaan omistajuus optimoidusti Service-kerroksessa
        return lokikirjaService.isLokikirjanOmistaja(lokikirjaId, kayttaja.getId());
    }


    // ------------------- LUONTI (STUDENT) -------------------

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Lokikirja> createLokikirja(@RequestBody LokikirjaRequest request) {

        Kayttaja kayttaja = getAuthenticatedKayttajaOrThrow();

        Lokikirja lokikirja = new Lokikirja();
        lokikirja.setKayttaja(kayttaja);
        lokikirja.setSisalto(request.sisalto);
        lokikirja.setLuontiAika(LocalDateTime.now()); 
        
        Lokikirja tallennettuLokikirja = lokikirjaService.save(lokikirja); 
        return new ResponseEntity<>(tallennettuLokikirja, HttpStatus.CREATED);
    }

    // ------------------- HAKU (STUDENT & TEACHER) -------------------

    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<List<Lokikirja>> getMyLokikirjat() {
        Kayttaja kayttaja = getAuthenticatedKayttajaOrThrow();

        // Huom: Tässä käytetään suoraan Kayttaja-oliota Service-kerroksessa
        List<Lokikirja> lokikirjat = lokikirjaService.findByKayttaja(kayttaja); 

        return ResponseEntity.ok(lokikirjat);
    }
    
    @GetMapping("/{lokikirjaId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<Lokikirja> getLokikirjaById(@PathVariable Long lokikirjaId) {
        
        Kayttaja kayttaja = getAuthenticatedKayttajaOrThrow();
        
        // OPTIMOITU TARKISTUS: Jos käyttäjä ei ole opettaja eikä omistaja, estetään.
        if (!checkAuthorization(lokikirjaId, kayttaja)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        // Vasta tässä ladataan Lokikirja, jos valtuutus on OK
        return lokikirjaService.findById(lokikirjaId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new LokikirjaNotFoundException("Lokikirjaa ei löydy."));
    }
    
    // ------------------- PÄIVITYS (OWNER & TEACHER) -------------------

    @PutMapping("/{lokikirjaId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<Lokikirja> updateLokikirja(@PathVariable Long lokikirjaId, @RequestBody LokikirjaRequest request) {
        
        Kayttaja kayttaja = getAuthenticatedKayttajaOrThrow();
        
        // OPTIMOITU TARKISTUS
        if (!checkAuthorization(lokikirjaId, kayttaja)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
        }

        // Vasta tässä ladataan Lokikirja, jos valtuutus on OK
        Lokikirja existingLokikirja = lokikirjaService.findById(lokikirjaId)
                .orElseThrow(() -> new LokikirjaNotFoundException("Lokikirjaa ei löydy."));

        existingLokikirja.setSisalto(request.sisalto);
        
        Lokikirja paivitettyLokikirja = lokikirjaService.save(existingLokikirja);
        
        return ResponseEntity.ok(paivitettyLokikirja);
    }
    
    // ------------------- POISTO (OWNER & TEACHER) -------------------
    
    @DeleteMapping("/{lokikirjaId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<Void> deleteLokikirja(@PathVariable Long lokikirjaId) {
        
        Kayttaja kayttaja = getAuthenticatedKayttajaOrThrow();
        
        // OPTIMOITU TARKISTUS
        if (!checkAuthorization(lokikirjaId, kayttaja)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
        }
        
        if (lokikirjaService.findById(lokikirjaId).isEmpty()) {
             return ResponseEntity.notFound().build(); // Käytetään notFound, jos poistettavaa ei löydy.
        }

        lokikirjaService.deleteById(lokikirjaId);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Yksinkertainen poikkeusluokka virheenkäsittelyyn
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private static class LokikirjaNotFoundException extends RuntimeException {
        public LokikirjaNotFoundException(String message) {
            super(message);
        }
    }
}