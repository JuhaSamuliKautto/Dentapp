package com.university.Management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtRequestFilter jwtRequestFilter,
            DaoAuthenticationProvider authenticationProvider) throws Exception {

        http
                .cors(cors -> {
                    // CORS-asetukset
                })
                .csrf(csrf -> csrf.disable())

                // Varmistetaan, että käytämme omaa DaoAuthenticationProvideria
                .authenticationProvider(authenticationProvider)

                .authorizeHttpRequests(authorize -> authorize

                        // Salli OPTIONS-pyynnöt KAIKILLE reiteille (CORS esitarkistus)
                        // Tämä on kriittinen korjaus, joka voi aiheuttaa 403-virheen!
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Salli kirjautuminen ja rekisteröinti
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()

                        // Salli juuripolku (/)
                        .requestMatchers(HttpMethod.GET, "/").permitAll()

                        // Salli H2-konsoli
                        .requestMatchers("/h2-console/**").permitAll()

                        // *** LOPULLINEN AUKTORISOINTIKORJAUS: POST /api/kurssi***
                        // Käytetään hasAuthority() ja jokerimerkkiä polun täsmäyksessä
                        .requestMatchers(HttpMethod.POST, "/api/kurssi**").hasAuthority("ROLE_TEACHER")

                        // Kaikki muut pyynnöt vaativat tunnistautumisen
                        .anyRequest().authenticated())

                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}