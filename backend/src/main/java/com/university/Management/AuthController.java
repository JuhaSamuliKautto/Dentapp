package com.university.Management;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Rooli;
import com.university.Management.repository.KayttajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth") 
public class AuthController {

    @Autowired
    private KayttajaRepository kayttajaRepository;

    // --- LOGAUS (VANHA) ---
    public static class LoginRequest {
        public String kayttajatunnus;
        public String salasana;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Kayttaja> kayttajaOpt = kayttajaRepository.findByKayttajatunnus(loginRequest.kayttajatunnus);
        if (kayttajaOpt.isEmpty()) {
            return new ResponseEntity<>("Käyttäjää ei löydy.", HttpStatus.UNAUTHORIZED);
        }
        Kayttaja kayttaja = kayttajaOpt.get();
        if (!kayttaja.getSalasanaHash().equals(loginRequest.salasana)) {
            return new ResponseEntity<>("Virheellinen salasana.", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(new AuthResponse(kayttaja.getId(), kayttaja.getRooli()));
    }

    // --- UUSI OSA: REKISTERÖINTI --- 

    // Pyyntö-olio rekisteröintiä varten (tarvitsee myös roolin)
    public static class RegisterRequest {
        public String kayttajatunnus;
        public String salasana;
        public Rooli rooli; // Odottaa arvoa "OPETTAJA" tai "OPPILAS"
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // 1. Tarkista onko tunnus jo olemassa
        if (kayttajaRepository.findByKayttajatunnus(request.kayttajatunnus).isPresent()) {
            return new ResponseEntity<>("Käyttäjätunnus on jo varattu!", HttpStatus.BAD_REQUEST);
        }

        // 2. Luo uusi käyttäjä
        Kayttaja uusiKayttaja = new Kayttaja();
        uusiKayttaja.setKayttajatunnus(request.kayttajatunnus);
        // HUOM: Oikeassa sovelluksessa tässä kohtaa salasana kryptattaisiin (esim. BCrypt)
        // Nyt tallennamme sen selkokielisenä, jotta nykyinen login-logiikkasi toimii.
        uusiKayttaja.setSalasanaHash(request.salasana); 
        uusiKayttaja.setRooli(request.rooli);

        // 3. Tallenna kantaan
        kayttajaRepository.save(uusiKayttaja);

        return ResponseEntity.ok("Käyttäjä luotu onnistuneesti!");
    }

    // --- VASTAUS ---
    public static class AuthResponse {
        public Long userId;
        public Rooli rooli;

        public AuthResponse(Long userId, Rooli rooli) {
            this.userId = userId;
            this.rooli = rooli;
        }
    }
}