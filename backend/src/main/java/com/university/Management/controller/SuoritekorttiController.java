package com.university.Management.controller;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Suoritekortti;
import com.university.Management.service.KayttajaService;
import com.university.Management.service.SuoritekorttiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suoritekortit")
@CrossOrigin(origins = "http://localhost:5173")
public class SuoritekorttiController {

    private final SuoritekorttiService suoritekorttiService;
    private final KayttajaService kayttajaService;

    // DTO-luokkaa (esim. SuoritekorttiRequest) tulisi käyttää POST/PUT-metodeissa.
    // Koska DTO:ta ei ole määritelty, käytämme Suoritekortti-entiteettiä pyynnössä, mutta se ei ole suositeltu tapa.

    public SuoritekorttiController(SuoritekorttiService suoritekorttiService, KayttajaService kayttajaService) {
        this.suoritekorttiService = suoritekorttiService;
        this.kayttajaService = kayttajaService;
    }

    private Kayttaja getAuthenticatedKayttajaOrThrow() {
        return kayttajaService.getAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Käyttäjä ei ole autentikoitu."));
    }

    // ------------------- KORTIN LUONTI (TEACHER) -------------------

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')") 
    // Huom: Tässä pitäisi vastaanottaa SuoritekorttiRequest DTO:n sijaan, mutta pysytään koodissasi
    public ResponseEntity<Suoritekortti> createCard(@RequestBody Suoritekortti korttiData) { 
        
        Kayttaja luoja = getAuthenticatedKayttajaOrThrow();

        // Estetään frontendiä asettamasta luojaa suoraan. Asetetaan autentikoitu käyttäjä.
        korttiData.setLuoja(luoja); 
        
        Suoritekortti tallennettuKortti = suoritekorttiService.save(korttiData);

        return new ResponseEntity<>(tallennettuKortti, HttpStatus.CREATED);
    }
    
    // ------------------- HAKU (STUDENT & TEACHER) -------------------

    // Hakee kaikki kortit (sopii yleiseen listaukseen)
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<List<Suoritekortti>> findAllCards() {
        List<Suoritekortti> kortit = suoritekorttiService.findAll();
        return ResponseEntity.ok(kortit);
    }
    
    // Hakee kortit tietyn luojan ID:n perusteella
    @GetMapping("/luoja/{luojaId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<List<Suoritekortti>> findCardsByLuojaId(@PathVariable Long luojaId) {
        List<Suoritekortti> kortit = suoritekorttiService.findByLuojaId(luojaId);
        return ResponseEntity.ok(kortit);
    }
    
    // ------------------- POISTO (TEACHER) -------------------
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        Kayttaja nykyinenKayttaja = getAuthenticatedKayttajaOrThrow();

        // 1. TARKISTETAAN ONKO OLEMASSA
        Optional<Suoritekortti> korttiOpt = suoritekorttiService.findById(id);
        if (korttiOpt.isEmpty()) {
             return ResponseEntity.notFound().build();
        }
        
        // 2. VALTUUTUKSEN TARKISTUS (TEACHER)
        if (nykyinenKayttaja.getRole().equals("TEACHER")) {
            // OPTIMOINTI: Käytetään Service-kerroksen metodia, joka tarkistaa omistajuuden
            if (!suoritekorttiService.isKortinLuoja(id, nykyinenKayttaja.getId())) { 
                 return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
            }
        }
        
        // ADMIN rooli pääsee ohi tarkistuksesta PreAuthorize-annotoinnin ansiosta.
        
        suoritekorttiService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}