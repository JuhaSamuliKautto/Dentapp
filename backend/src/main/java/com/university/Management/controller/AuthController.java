// AuthController.java - TÄMÄN PITÄISI OLLA AINOA SISÄLTÖ
package com.university.Management.controller;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Rooli;
import com.university.Management.repository.KayttajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// Sallii pyynnöt Front-Endistä (esim. React)
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth") 
public class AuthController {

    @Autowired
    private KayttajaRepository kayttajaRepository;

    // Data Transfer Object kirjautumista varten
    public static class LoginRequest {
        public String kayttajatunnus;
        public String salasana;
    }

    // Rajapinta kirjautumista varten: POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        
        // 1. Etsi käyttäjä tietokannasta käyttäjätunnuksen perusteella
        Optional<Kayttaja> kayttajaOpt = kayttajaRepository.findByKayttajatunnus(loginRequest.kayttajatunnus);

        if (kayttajaOpt.isEmpty()) {
            return new ResponseEntity<>("Käyttäjää ei löydy.", HttpStatus.UNAUTHORIZED);
        }

        Kayttaja kayttaja = kayttajaOpt.get();

        // 2. TARKISTA SALASANA (TÄMÄ ON YKSINKERTAISTETTU, oikeasti hashattu!)
        if (!kayttaja.getSalasanaHash().equals(loginRequest.salasana)) {
            return new ResponseEntity<>("Virheellinen salasana.", HttpStatus.UNAUTHORIZED);
        }

        // 3. Kirjautuminen onnistui, palauta rooli ja ID
        return ResponseEntity.ok(new AuthResponse(kayttaja.getId(), kayttaja.getRooli()));
    }
    
    // Vastausobjekti (palautetaan Reactille)
    public static class AuthResponse {
        public Long userId;
        public Rooli rooli;

        public AuthResponse(Long userId, Rooli rooli) {
            this.userId = userId;
            this.rooli = rooli;
        }
    }
}