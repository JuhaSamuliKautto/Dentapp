package com.university.Management.controller;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Lokikirja;
import com.university.Management.model.Suoritekortti;
import com.university.Management.model.Suoritus;
import com.university.Management.model.SuoritusTila;
import com.university.Management.service.KayttajaService;
import com.university.Management.service.SuoritusService;
import com.university.Management.service.LokikirjaService;
import com.university.Management.service.SuoritekorttiService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suoritukset")
@CrossOrigin(origins = "http://localhost:5173")
public class SuoritusController {

    private final SuoritusService suoritusService;
    private final KayttajaService kayttajaService;
    private final LokikirjaService lokikirjaService;
    private final SuoritekorttiService suoritekorttiService;

    public SuoritusController(
            SuoritusService suoritusService, 
            KayttajaService kayttajaService, 
            LokikirjaService lokikirjaService,
            SuoritekorttiService suoritekorttiService) 
    {
        this.suoritusService = suoritusService;
        this.kayttajaService = kayttajaService;
        this.lokikirjaService = lokikirjaService;
        this.suoritekorttiService = suoritekorttiService;
    }

    private Kayttaja getAuthenticatedKayttajaOrThrow() {
        return kayttajaService.getAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Käyttäjä ei ole autentikoitu."));
    }

    // --- DTO:t ---
    public static class SuoritusRequest {
        public Long lokikirjaId;
        public Long korttiId;
    }

    public static class ArvioiSuoritusRequest {
        public SuoritusTila tila;
        public String palaute;
    }


    // -------------------------------------------------------------------------
    // LUONTI (STUDENT)
    // -------------------------------------------------------------------------

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')") 
    public ResponseEntity<?> luoUusiSuoritus(@RequestBody SuoritusRequest request) {

        Kayttaja opiskelija = getAuthenticatedKayttajaOrThrow();

        // 1. VALIDAATIO: Tarkista Lokikirjan omistajuus
        if (!lokikirjaService.isLokikirjanOmistaja(request.lokikirjaId, opiskelija.getId())) {
             return new ResponseEntity<>("Lokikirja ei kuulu autentikoidulle opiskelijalle.", HttpStatus.FORBIDDEN);
        }

        // 2. Hae Lokikirja ja Suoritekortti
        Optional<Lokikirja> lokikirjaOpt = lokikirjaService.findById(request.lokikirjaId);
        if (lokikirjaOpt.isEmpty()) {
            return new ResponseEntity<>("Lokikirjaa ei löydy.", HttpStatus.BAD_REQUEST);
        }
        
        Optional<Suoritekortti> korttiOpt = suoritekorttiService.findById(request.korttiId);
        if (korttiOpt.isEmpty()) {
             return new ResponseEntity<>("Suoritekorttia ei löydy.", HttpStatus.BAD_REQUEST);
        }
        
        // 3. Luo uusi Suoritus-olio
        Suoritus uusiSuoritus = new Suoritus();
        uusiSuoritus.setLokikirja(lokikirjaOpt.get());
        uusiSuoritus.setSuoritekortti(korttiOpt.get());
        uusiSuoritus.setTila(SuoritusTila.ODOTTAA_HYVAKSYNTAA);
        uusiSuoritus.setLuontiAika(LocalDateTime.now());
        
        // 4. Tallenna
        Suoritus tallennettuSuoritus = suoritusService.save(uusiSuoritus);
        return new ResponseEntity<>(tallennettuSuoritus, HttpStatus.CREATED);
    }


    // -------------------------------------------------------------------------
    // ARVIOINTI (TEACHER)
    // -------------------------------------------------------------------------

    @PutMapping("/{suoritusId}/arvioi")
    @PreAuthorize("hasRole('TEACHER')") 
    public ResponseEntity<?> arvioiSuoritus(
            @PathVariable Long suoritusId, 
            @RequestBody ArvioiSuoritusRequest request) {

        Kayttaja arvioija = getAuthenticatedKayttajaOrThrow();
        
        // Käytetään Service-kerroksen arvioiSuoritus-metodia
        Optional<Suoritus> paivitettySuoritus = suoritusService.arvioiSuoritus(
            suoritusId, 
            arvioija, 
            request.tila, 
            request.palaute
        );

        // KORJAUS: Käytetään selkeää if-rakennetta, joka poistaa tyypitysristiriidan
        if (paivitettySuoritus.isPresent()) {
            // Onnistunut vastaus (HTTP 200 OK, body: Suoritus)
            return ResponseEntity.ok(paivitettySuoritus.get());
        } else {
            // Epäonnistunut vastaus (HTTP 404 NOT FOUND, body: String)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Suoritusta ei löydy arvioitavaksi.");
        }
    }

    // -------------------------------------------------------------------------
    // HAKU LOKIKIRJASTA (STUDENT & TEACHER)
    // -------------------------------------------------------------------------

    @GetMapping("/lokikirja/{lokikirjaId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')") 
    public ResponseEntity<List<Suoritus>> haeSuorituksetLokikirjasta(@PathVariable Long lokikirjaId) {
        
        Kayttaja kayttaja = getAuthenticatedKayttajaOrThrow();

        // TÄRKEÄ VALIDAATIO: Tarkista, että käyttäjä on omistaja TAI opettaja
        boolean isOwner = lokikirjaService.isLokikirjanOmistaja(lokikirjaId, kayttaja.getId());
        boolean isTeacher = kayttaja.getRole().equals("TEACHER");

        if (!isOwner && !isTeacher) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        List<Suoritus> suoritukset = suoritusService.findByLokikirjaId(lokikirjaId); 
        return ResponseEntity.ok(suoritukset);
    }
}