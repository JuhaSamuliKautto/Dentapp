package com.university.Management.controller;

import com.university.Management.dto.LoginRequest;
import com.university.Management.dto.LoginResponse;
import com.university.Management.dto.RegisterRequest;
import com.university.Management.model.Kayttaja;
import com.university.Management.repository.KayttajaRepository;
import com.university.Management.security.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final KayttajaRepository kayttajaRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            KayttajaRepository kayttajaRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.kayttajaRepository = kayttajaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // -------------------------------------------------------------------------
    // REKISTERÖITYMINEN (POST /api/auth/register)
    // -------------------------------------------------------------------------

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        // KORJATTU: Käyttää nyt getUsername()
        if (kayttajaRepository.findByUsername(request.getUsername()).isPresent()) {
            return new ResponseEntity<>("Käyttäjätunnus on jo varattu!", HttpStatus.BAD_REQUEST);
        }

        Kayttaja kayttaja = new Kayttaja();

        // KORJATTU: Käyttää nyt getUsername()
        kayttaja.setUsername(request.getUsername());
        // KORJATTU: Käyttää nyt getPassword()
        kayttaja.setSalasana(passwordEncoder.encode(request.getPassword()));

        String rooli;

        // KORJATTU: Oletetaan, että getterin nimi on getRole() RegisterRequestissa
        if (request.getRole() != null) {
            // Käytetään .name() muuntamaan Rooli Stringiksi (esim. Rooli.TEACHER ->
            // "TEACHER")
            rooli = request.getRole().name();
        } else {
            rooli = "STUDENT"; // Oletusrooli
        }

        kayttaja.setRole(rooli);

        Kayttaja uusiKayttaja = kayttajaRepository.save(kayttaja);

        return new ResponseEntity<>("Käyttäjä rekisteröity onnistuneesti tunnuksella: " + uusiKayttaja.getUsername(),
                HttpStatus.CREATED);
    }

    // -------------------------------------------------------------------------
    // KIRJAUTUMINEN (POST /api/auth/login)
    // -------------------------------------------------------------------------

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) {

        // 1. Suorita autentikointi
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        // KORJATTU: KÄYTÄ NYT getUsername()
                        authenticationRequest.getUsername(),
                        // KORJATTU: KÄYTÄ NYT getPassword()
                        authenticationRequest.getPassword()));

        // 2. Hae UserDetails
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Hae Kayttaja-olio tietokannasta roolin saamiseksi
        Kayttaja kayttaja = kayttajaRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Autentikoitu käyttäjä ei löytynyt tietokannasta."));

        // 4. Luo token
        final String token = jwtUtil.generateToken(userDetails);

        // 5. Palauta LoginResponse-olio (sisältää roolin ja tokenin)
        return ResponseEntity.ok(new LoginResponse(
                token,
                kayttaja.getRole()));
    }
}