package com.university.Management; 

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered; 
import org.springframework.core.annotation.Order; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // KÄYTETÄÄN TÄHÄN VIIMEISEEN TESTIIN VARTEN TÄYSIN PERMISSIIVISTÄ KOODIA:
    
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE) // Pakottaa tämän konfiguraation menemään ensin
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Poistaa CSRF-suojauksen (POST-pyynnöt)
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // Sallii H2-konsolin
            
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll() // SALLII PÄÄSYN AIVAN KAIKKEEN ILMAN TODENNUKSIA
            )
            ; 
            
        return http.build();
    }
}