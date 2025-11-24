package com.university.Management.security;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // TÄMÄ ON UUSI IMPORTTI
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; 
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@EnableMethodSecurity // OTTAA KÄYTTÖÖN @PreAuthorize
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter; 

    // --- BEAN MÄÄRITYKSET ---

    /**
     * Määrittelee salasanan tiivistämismekanismin (BCrypt)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * PAKOLLINEN: AuthenticationManager-bean tarvitaan, jotta Spring Security voi 
     * prosessoida JWTRequestFilterin asettaman autentikaatiokontekstin.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // --- SUODATINKETJU ---

    /**
     * Asettaa tietoturvasäännöt: tilaton sessio ja JWT-suodatin
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Poistaa CSRF-suojauksen
            .csrf(AbstractHttpConfigurer::disable)
            
            // Asettaa istunnonhallinnan tilattomaksi (STATELESS)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Määritellään pääsyrajat
            .authorizeHttpRequests(auth -> auth
                // Salli rekisteröinti, kirjautuminen ja H2-konsoli ilman autentikointia
                .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
                
                // Kaikki muut pyynnöt vaativat autentikoinnin (JWT-tokenin)
                .anyRequest().authenticated()
            )
            // LISÄTTY: Lisätään oma JWT-suodatin ennen Springin sisäänrakennettua suodatinta
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
            
        // Tarvitaan, jotta H2-konsoli toimii
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}