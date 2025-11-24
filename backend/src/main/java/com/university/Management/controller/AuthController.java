package com.university.Management.controller;

import com.university.Management.dto.LoginRequest; 
import com.university.Management.dto.LoginResponse; 
import com.university.Management.dto.RegisterRequest; // UUSI DTO
import com.university.Management.model.Kayttaja;
import com.university.Management.model.Rooli;
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
// HUOM: Jos käytät tätä annotaatiota, sinun ei ehkä tarvitse määritellä CORSia SecurityConfigissa
@CrossOrigin(origins = "http://localhost:5173") 
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final KayttajaRepository kayttajaRepository;
    private final PasswordEncoder passwordEncoder;

    // Konstruktori-injektio
    public AuthController(
        AuthenticationManager authenticationManager, 
        JwtUtil jwtUtil,
        KayttajaRepository kayttajaRepository,
        PasswordEncoder passwordEncoder) 
    {
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
        // Tarkista, onko käyttäjätunnus jo varattu
        if (kayttajaRepository.findByKayttajatunnus(request.getKayttajatunnus()).isPresent()) {
            return new ResponseEntity<>("Käyttäjätunnus on jo varattu!", HttpStatus.BAD_REQUEST);
        }

        Kayttaja kayttaja = new Kayttaja();
        kayttaja.setKayttajatunnus(request.getKayttajatunnus());
        
        // TIIVISTÄ SALASANA
        kayttaja.setSalasana(passwordEncoder.encode(request.getSalasana())); 
        
        // Aseta rooli (käytä OPPILAS, jos roolia ei määritelty pyynnössä)
        Rooli rooli = request.getRooli() == null ? Rooli.OPPILAS : request.getRooli();
        kayttaja.setRooli(rooli);

        Kayttaja uusiKayttaja = kayttajaRepository.save(kayttaja);

        // Palautetaan pelkkä ilmoitus onnistumisesta, ei koko käyttäjäoliota turvallisuussyistä
        return new ResponseEntity<>("Käyttäjä rekisteröity onnistuneesti tunnuksella: " + uusiKayttaja.getKayttajatunnus(), HttpStatus.CREATED);
    }

    // -------------------------------------------------------------------------
    // KIRJAUTUMINEN (POST /api/auth/login)
    // -------------------------------------------------------------------------

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) {
        
        // 1. Suorita autentikointi Spring Securityn AuthenticationManagerin avulla
        // TÄMÄ KÄYTTÄÄ TAUSTALLA PasswordEncoderia ja JwtTokenServiceä (UserDetailsService)
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getKayttajatunnus(),
                        authenticationRequest.getSalasana()
                )
        );
        
        // 2. Hae UserDetails autentikoidusta oliosta
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // 3. Hae koko Kayttaja-olio tietokannasta roolin saamiseksi
        Kayttaja kayttaja = kayttajaRepository.findByKayttajatunnus(userDetails.getUsername())
                            // Jos autentikointi onnistui, käyttäjä löytyy varmasti
                            .orElseThrow(() -> new RuntimeException("Autentikoitu käyttäjä ei löytynyt tietokannasta."));

        // 4. Luo token
        final String token = jwtUtil.generateToken(userDetails);

        // 5. Palauta LoginResponse-olio, joka muuntaa roolin (OPETTAJA -> teacher)
        // Huom: Käytämme tässä Kayttaja-olion Rooli-enumia LoginResponsen tarvitseman String-roolin sijaan
        return ResponseEntity.ok(new LoginResponse(
            token, 
            kayttaja.getRooli().name() // Lähetä Rooli-enumin nimi (esim. "OPETTAJA") DTO:lle
        ));
    }
}