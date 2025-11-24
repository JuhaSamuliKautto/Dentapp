package com.university.Management.security; 

import com.university.Management.model.Kayttaja; 
import com.university.Management.repository.KayttajaRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class JwtTokenService implements UserDetailsService {

    @Autowired
    private KayttajaRepository kayttajaRepository;

    @Override
    public UserDetails loadUserByUsername(String kayttajatunnus) throws UsernameNotFoundException {
        
        Kayttaja kayttaja = kayttajaRepository.findByKayttajatunnus(kayttajatunnus)
            .orElseThrow(() -> new UsernameNotFoundException("Käyttäjää " + kayttajatunnus + " ei löytynyt"));
        
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
            new SimpleGrantedAuthority("ROLE_" + kayttaja.getRooli().name())
        );

        return new org.springframework.security.core.userdetails.User(
                kayttaja.getKayttajatunnus(),
                kayttaja.getSalasana(), 
                authorities
        );
    }
}