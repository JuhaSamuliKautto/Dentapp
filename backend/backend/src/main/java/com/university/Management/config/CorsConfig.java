package com.university.Management.config; // Varmista, että paketti on oikein

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") 
                // TÄRKEÄÄ: Määritellään nimenomaisesti Front-endin osoite ja portti
                .allowedOrigins("http://localhost:5173") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") 
                .allowedHeaders("*")
                // KRIITTINEN: Tarvitaan, jotta selaimet sallivat pyynnöt, jotka sisältävät tunnuksia (kuten JWT)
                .allowCredentials(true); 
    }
}