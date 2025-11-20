package com.university.Management;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Poista CSRF-suojaus käytöstä H2-konsolia varten kehityksessä
            .csrf(csrf -> csrf.disable())

            // Salli H2-konsolin kehystys (tarvitaan, jotta se näkyy selaimessa)
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))

            // Määritä, mitkä polut ovat sallittuja ilman todennusta
            .authorizeHttpRequests(authorize -> authorize
                // Sallii pääsyn H2-konsolin polkuun ja sen alipolkuihin
                .requestMatchers("/h2-console/**").permitAll() 
                // Salli pääsy myös juuripolkuun (riippuen API-kutsun luonteesta)
                .requestMatchers("/").permitAll() 
                
                // KAIKKI MUUT pyynnöt vaativat todentamisen (automaattinen ohjaus /login-sivulle)
                .anyRequest().authenticated()
            )
            
            // Oletusmuotoinen kirjautumiskäytäntö (koska meillä ei ole omaa)
            .formLogin(formLogin -> formLogin.permitAll());

        return http.build();
    }
}