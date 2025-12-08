package com.university.Management.dto;

public class LoginRequest {

    // VAIHDA KAYTTÄMÄÄN YLEISIÄ STANDARDINIMIÄ
    private String username;
    private String password;

    public LoginRequest() {
    }

    // --- Getterit ja Setterit ---

    public String getUsername() { // HUOM: Nimi muuttui
        return username;
    }

    public void setUsername(String username) { // HUOM: Nimi muuttui
        this.username = username;
    }

    public String getPassword() { // HUOM: Nimi muuttui
        return password;
    }

    public void setPassword(String password) { // HUOM: Nimi muuttui
        this.password = password;
    }
}