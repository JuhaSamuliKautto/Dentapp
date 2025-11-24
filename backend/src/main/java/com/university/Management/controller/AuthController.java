package com.university.Management.controller; // HUOM: Korjattu paketin nimi!

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Rooli;
import com.university.Management.repository.KayttajaRepository;
// TÄMÄ ON PAKOLLINEN: Tuo uusi turvallisuuspalvelu sisään
import com.university.Management.security.JwtTokenService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// TÄMÄ ON PAKOLLINEN: Tuo PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private KayttajaRepository kayttajaRepository;

    // TÄMÄ ON PAKOLLINEN: Autentikointi ilman tätä on tietoturvariski
    @Autowired
    private PasswordEncoder passwordEncoder;

    // TÄMÄ ON PAKOLLINEN: Tokenin luomiseen
    @Autowired
    private JwtTokenService jwtTokenService; 

    // --- DTO:t ---
    
    // Yhdistetään pyyntö-oliot yhdeksi selkeämmäksi DTO:ksi
    public static class AuthRequest {
        public String kayttajatunnus;
        public String salasana;
        public Rooli rooli;
    }
    
    // KORJATTU VASTAUS-OLIO: Sisältää JWT-tokenin!
    public static class LoginResponse {
        private String token; // Token on pakollinen
        private Long userId;
        private Rooli rooli;

        public LoginResponse(String token, Long userId, Rooli rooli) {
            this.token = token;
            this.userId = userId;
            this.rooli = rooli;
        }

        // TÄMÄ ON KRIITTINEN serialisoinnin kannalta (edellinen virhe)
        public String getToken() { return token; }
        public Long getUserId() { return userId; }
        public Rooli getRooli() { return rooli; }
    }

    /**
     * POST /api/auth/register - Rekisteröi käyttäjän TAA JUOKEE SALASANAN
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (kayttajaRepository.findByKayttajatunnus(request.kayttajatunnus).isPresent()) {
            return new ResponseEntity<>("Käyttäjätunnus on jo varattu!", HttpStatus.BAD_REQUEST);
        }

        Kayttaja kayttaja = new Kayttaja();
        kayttaja.setKayttajatunnus(request.kayttajatunnus);
        
        // TIIVISTÄ SALASANA: TÄMÄ ON KRIITTINEN!
        kayttaja.setSalasanaHash(passwordEncoder.encode(request.salasana)); 
        
        // Aseta rooli
        if (request.rooli == null) {
            kayttaja.setRooli(Rooli.OPPILAS);
        } else {
            kayttaja.setRooli(request.rooli);
        }

        Kayttaja uusiKayttaja = kayttajaRepository.save(kayttaja);
        
        // Poistetaan tiiviste vastauksesta
        uusiKayttaja.setSalasanaHash(null); 
        return new ResponseEntity<>(uusiKayttaja, HttpStatus.CREATED);
    }
    
    /**
     * POST /api/auth/login - Kirjaa sisään ja palauttaa JWT-tokenin
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<Kayttaja> kayttajaOpt = kayttajaRepository.findByKayttajatunnus(request.kayttajatunnus);

        if (kayttajaOpt.isEmpty()) {
            return new ResponseEntity<>("Virheellinen käyttäjätunnus tai salasana.", HttpStatus.UNAUTHORIZED);
        }

        Kayttaja kayttaja = kayttajaOpt.get();

        // TARKISTA TIIVISTE: Vertaillaan selkokielistä salasanaa tiivisteeseen
        if (!passwordEncoder.matches(request.salasana, kayttaja.getSalasanaHash())) {
            return new ResponseEntity<>("Virheellinen käyttäjätunnus tai salasana.", HttpStatus.UNAUTHORIZED);
        }

        // LUO JWT TOKEN
        String token = jwtTokenService.generateToken(kayttaja.getId(), kayttaja.getKayttajatunnus(), kayttaja.getRooli().name());

        // PALAUTA TOKEN JA TIEDOT
        return ResponseEntity.ok(new LoginResponse(token, kayttaja.getId(), kayttaja.getRooli()));
    }
}