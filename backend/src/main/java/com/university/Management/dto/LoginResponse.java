package com.university.Management.dto;

public class LoginResponse {
    
    // TÄMÄ ON KRUUNUN JALOKIVI: Frontend odottaa nimeä "role"
    private final String role;
    private final String token; 
    
    // Konstruktori, joka suorittaa roolin muunnoksen
    public LoginResponse(String token, String backendRooli) {
        this.token = token;
        
        // Muunna backendin ISO KIRJAIN -rooli frontendin pienikirjaimiseen muotoon
        if ("OPETTAJA".equals(backendRooli)) {
            this.role = "teacher";
        } else if ("OPPILAS".equals(backendRooli)) {
            this.role = "student";
        } else {
            // Tuntemattoman roolin käsittely
            this.role = null; 
        }
    }

    // --- Getterit (tarvitaan, jotta Springin Jackson voi muuntaa JSONiksi) ---
    
    public String getRole() { 
        return role;
    }
    
    public String getToken() {
        return token;
    }
}