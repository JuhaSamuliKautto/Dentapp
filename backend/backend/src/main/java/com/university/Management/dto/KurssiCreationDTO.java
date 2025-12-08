package com.university.Management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class KurssiCreationDTO {

    @NotBlank(message = "Kurssin nimi on pakollinen")
    @Size(min = 3, max = 100, message = "Nimen pituus 3-100 merkkiä")
    private String nimi;

    private String kuvaus;

    @NotBlank(message = "Vastuuopettajan tunnus on pakollinen")
    private String vastuuopettajaUsername; // Käyttäjätunnus (esim. sähköposti)

    // Getterit ja Setterit
    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public String getVastuuopettajaUsername() {
        return vastuuopettajaUsername;
    }

    public void setVastuuopettajaUsername(String vastuuopettajaUsername) {
        this.vastuuopettajaUsername = vastuuopettajaUsername;
    }
}