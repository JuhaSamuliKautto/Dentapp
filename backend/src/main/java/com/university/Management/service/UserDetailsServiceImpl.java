package com.university.Management.service;

import com.university.Management.model.Kayttaja;
import com.university.Management.repository.KayttajaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final KayttajaRepository kayttajaRepository;

    public UserDetailsServiceImpl(KayttajaRepository kayttajaRepository) {
        this.kayttajaRepository = kayttajaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Etsitään käyttäjä tietokannasta annetulla käyttäjätunnuksella
        // (username/email).
        Optional<Kayttaja> kayttajaOpt = kayttajaRepository.findByUsername(username);

        // 2. Tarkistetaan löytyikö käyttäjä. Jos ei, heitetään Spring Securityn vaatima
        // poikkeus.
        if (!kayttajaOpt.isPresent()) {
            throw new UsernameNotFoundException("Käyttäjää " + username + " ei löytynyt.");
        }

        Kayttaja kayttaja = kayttajaOpt.get();

        String rooli = kayttaja.getRole();

        // 3. Varmistetaan, että käyttäjällä on rooli.
        if (rooli == null) {
            throw new IllegalStateException("Käyttäjällä ei ole määriteltyä roolia.");
        }

        // 4. Muodostetaan auktoriteetit/roolit (esim. "STUDENT" -> "ROLE_STUDENT").
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + rooli.toUpperCase()));

        // 5. Palautetaan Spring Securityn User-olio, joka sisältää tunnukset ja
        // auktoriteetit.
        // Salasana (getSalasana()) on hashattu, ja se vertaillaan automaattisesti.
        return new org.springframework.security.core.userdetails.User(
                kayttaja.getUsername(),
                kayttaja.getSalasana(),
                authorities);
    }
}