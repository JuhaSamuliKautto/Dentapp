package com.university.Management.controller;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Lokikirja;
import com.university.Management.repository.KayttajaRepository;
import com.university.Management.repository.LokikirjaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lokikirjat")
@CrossOrigin(origins = "http://localhost:5173")
public class LokikirjaController {

    @Autowired
    private LokikirjaRepository lokikirjaRepository;

    @Autowired
    private KayttajaRepository kayttajaRepository;

    // --- DTO:t ---

    // Käytetään Lokikirjan luontiin ja päivitykseen
    public static class LokikirjaRequest {
        public String sisalto; 
    }
    
    // --- Autentikaatio-apumetodi ---

    /**
     * Hakee autentikoidun käyttäjän (Kayttaja) olion tietokannasta JWT-tokenin perusteella.
     */
    private Optional<Kayttaja> getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        // HUOM: KayttajaRepository tarvitsee findByKayttajatunnus-metodin!
        return kayttajaRepository.findByKayttajatunnus(username); 
    }

    // --- CRUD RAJAPINNAT ---

    /**
     * POST /api/lokikirjat - Luo uuden lokikirjan.
     * Vain OPPILAS saa luoda.
     */
    @PostMapping
    @PreAuthorize("hasRole('OPPILAS')") 
    public ResponseEntity<?> createLokikirja(@RequestBody LokikirjaRequest request) {
        
        Optional<Kayttaja> kayttajaOpt = getAuthenticatedUser();
        if (kayttajaOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
        }
        
        Lokikirja lokikirja = new Lokikirja();
        lokikirja.setKayttaja(kayttajaOpt.get());
        lokikirja.setSisalto(request.sisalto);
        lokikirja.setLuontiAika(LocalDateTime.now());
        
        Lokikirja tallennettuLokikirja = lokikirjaRepository.save(lokikirja);
        return new ResponseEntity<>(tallennettuLokikirja, HttpStatus.CREATED);
    }

    /**
     * GET /api/lokikirjat - Hakee kaikki autentikoidun käyttäjän Lokikirjat.
     * OPETTAJALLE JA OPPILAALLE (Oppilas saa vain omansa, Opettaja saa kaikki, jos repositoryssa sallitaan).
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('OPETTAJA', 'OPPILAS')")
    public ResponseEntity<List<Lokikirja>> getMyLokikirjat() {
        Optional<Kayttaja> kayttajaOpt = getAuthenticatedUser();
        
        if (kayttajaOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<Lokikirja> lokikirjat;
        Kayttaja kayttaja = kayttajaOpt.get();
        
        // Huomio: Jos Opettajien pitäisi nähdä kaikki, tarvittaisiin findAll() TAI oma metodi.
        // Tässä toteutuksessa käyttäjä näkee omat Lokikirjansa (Opettajalla voi olla myös Lokikirja/opetussuunnitelma)
        lokikirjat = lokikirjaRepository.findByKayttaja(kayttaja); 
        
        return ResponseEntity.ok(lokikirjat);
    }
    
    /**
     * GET /api/lokikirjat/{id} - Hakee yksittäisen lokikirjan ID:n perusteella.
     * Varmistaa, että käyttäjä omistaa lokikirjan (tai on OPETTAJA).
     */
    @GetMapping("/{lokikirjaId}")
    @PreAuthorize("hasAnyRole('OPETTAJA', 'OPPILAS')")
    public ResponseEntity<Lokikirja> getLokikirjaById(@PathVariable Long lokikirjaId) {
        Optional<Lokikirja> lokikirjaOpt = lokikirjaRepository.findById(lokikirjaId);
        
        if (lokikirjaOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Lokikirja lokikirja = lokikirjaOpt.get();
        Optional<Kayttaja> kayttajaOpt = getAuthenticatedUser();
        
        if (kayttajaOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        Kayttaja kayttaja = kayttajaOpt.get();
        
        // TARKISTUS: Omistaja TAI Opettaja
        boolean isOwner = lokikirja.getKayttaja().getId().equals(kayttaja.getId());
        boolean isOpettaja = kayttaja.getRooli().name().equals("OPETTAJA");
        
        if (!isOwner && !isOpettaja) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
        }
        
        return ResponseEntity.ok(lokikirja);
    }

    /**
     * PUT /api/lokikirjat/{lokikirjaId} - Päivittää olemassa olevan lokikirjan sisällön.
     * Vain omistaja TAI OPETTAJA saa päivittää.
     */
    @PutMapping("/{lokikirjaId}")
    @PreAuthorize("hasAnyRole('OPETTAJA', 'OPPILAS')")
    public ResponseEntity<?> updateLokikirja(@PathVariable Long lokikirjaId, @RequestBody LokikirjaRequest request) {
        
        Optional<Lokikirja> lokikirjaOpt = lokikirjaRepository.findById(lokikirjaId);
        if (lokikirjaOpt.isEmpty()) {
            return new ResponseEntity<>("Lokikirjaa ei löydy.", HttpStatus.NOT_FOUND);
        }

        Optional<Kayttaja> kayttajaOpt = getAuthenticatedUser();
        if (kayttajaOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Lokikirja lokikirja = lokikirjaOpt.get();
        Kayttaja kayttaja = kayttajaOpt.get();
        
        // TARKISTUS: Omistaja TAI Opettaja
        boolean isOwner = lokikirja.getKayttaja().getId().equals(kayttaja.getId());
        boolean isOpettaja = kayttaja.getRooli().name().equals("OPETTAJA");
        
        if (!isOwner && !isOpettaja) {
            return new ResponseEntity<>("Pääsy kielletty: Et omista tätä lokikirjaa.", HttpStatus.FORBIDDEN);
        }

        // Päivitä sisältö ja tallenna
        lokikirja.setSisalto(request.sisalto);
        Lokikirja paivitettyLokikirja = lokikirjaRepository.save(lokikirja);
        
        return ResponseEntity.ok(paivitettyLokikirja);
    }
    
    /**
     * DELETE /api/lokikirjat/{lokikirjaId} - Poistaa lokikirjan.
     * Vain omistaja TAI OPETTAJA saa poistaa.
     */
    @DeleteMapping("/{lokikirjaId}")
    @PreAuthorize("hasAnyRole('OPETTAJA', 'OPPILAS')")
    public ResponseEntity<Void> deleteLokikirja(@PathVariable Long lokikirjaId) {
        Optional<Lokikirja> lokikirjaOpt = lokikirjaRepository.findById(lokikirjaId);
        if (lokikirjaOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Kayttaja> kayttajaOpt = getAuthenticatedUser();
        if (kayttajaOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Lokikirja lokikirja = lokikirjaOpt.get();
        Kayttaja kayttaja = kayttajaOpt.get();

        // TARKISTUS: Omistaja TAI Opettaja
        boolean isOwner = lokikirja.getKayttaja().getId().equals(kayttaja.getId());
        boolean isOpettaja = kayttaja.getRooli().name().equals("OPETTAJA");
        
        if (!isOwner && !isOpettaja) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
        }

        // Poista Lokikirja
        lokikirjaRepository.delete(lokikirja);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }
}