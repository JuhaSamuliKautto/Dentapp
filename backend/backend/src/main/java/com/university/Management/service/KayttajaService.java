package com.university.Management.service;

import com.university.Management.model.Kayttaja;
import com.university.Management.repository.KayttajaRepository; // TÄRKEÄ IMPORT
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KayttajaService {

    // 1. LISÄTTY REPOSITORY
    private final KayttajaRepository kayttajaRepository;

    // Konstruktori-injektio
    public KayttajaService(KayttajaRepository kayttajaRepository) {
        this.kayttajaRepository = kayttajaRepository;
    }

    // --- CRUD-toiminnot (AuthController kutsuu tätä) ---
    
    public Kayttaja save(Kayttaja kayttaja) {
        return kayttajaRepository.save(kayttaja);
    }
    
    public Optional<Kayttaja> findByUsername(String username) {
        return kayttajaRepository.findByUsername(username);
    }
    
    // ----------------------------------------------------

    /**
     * Hakee autentikoidun käyttäjän olion Spring Security Contextista ja tietokannasta.
     * Tätä kutsuvat KAIKKI Controller-luokat.
     */
    public Optional<Kayttaja> getAuthenticatedUser() {
        // Hakee autentikaatio-olion
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal != null) {
            // Jos kyseessä on vain käyttäjätunnus (esim. tokenin lukemisen jälkeen)
            username = principal.toString();
        }

        if (username != null) {
            // 2. KÄYTETÄÄN REPOSITORYA: Haetaan Kayttaja tietokannasta tunnuksen perusteella
            return kayttajaRepository.findByUsername(username); 
        }

        return Optional.empty();
    }
}