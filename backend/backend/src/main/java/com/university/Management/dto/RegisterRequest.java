package com.university.Management.dto;

import com.university.Management.model.Rooli;
// TÄRKEÄÄ: Varmista, että Rooli-enum on tuotu oikein

/**
 * Data Transfer Object (DTO) rekisteröintipyynnön (POST /api/auth/register)
 * tietojen vastaanottamiseen backendissä.
 * HUOM: Kenttien nimet muutettu englanninkielisiksi vastaamaan Front-endin
 * odotuksia
 * ja AuthControllerin uusia gettereitä.
 */
public class RegisterRequest {
    // KORJAUS: Muutettu suomenkieliset kentät englanninkielisiksi
    private String username;
    private String password;
    private Rooli role;

    // Tarvitaan Jacksonin (JSON-muunnin) toimintaa varten
    public RegisterRequest() {
    }

    // --- Getterit ja Setterit (KORJATTU) ---

    // Nimi muutettu: getKayttajatunnus() -> getUsername()
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Nimi muutettu: getSalasana() -> getPassword()
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Nimi muutettu: getRooli() -> getRole()
    public Rooli getRole() {
        return role;
    }

    public void setRole(Rooli role) {
        this.role = role;
    }
}