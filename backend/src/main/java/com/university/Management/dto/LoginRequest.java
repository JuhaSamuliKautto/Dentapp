package com.university.Management.dto;

/**
 * Data Transfer Object (DTO) sisäänkirjautumispyynnön (POST /api/auth/login) tietojen
 * (käyttäjätunnus ja salasana) vastaanottamiseen backendissä.
 */
public class LoginRequest {
    
    private String kayttajatunnus;
    private String salasana;

    // Tarvitaan Jacksonin (JSON-muunnin) toimintaa varten
    public LoginRequest() {} 
    
    // --- Getterit ja Setterit (tarvitaan Controllerissa) ---
    
    public String getKayttajatunnus() {
        return kayttajatunnus;
    }

    public void setKayttajatunnus(String kayttajatunnus) {
        this.kayttajatunnus = kayttajatunnus;
    }

    public String getSalasana() {
        return salasana;
    }

    public void setSalasana(String salasana) {
        this.salasana = salasana;
    }
}