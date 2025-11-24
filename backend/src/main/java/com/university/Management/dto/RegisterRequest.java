package com.university.Management.dto;

import com.university.Management.model.Rooli;
// TÄRKEÄÄ: Varmista, että Rooli-enum on tuotu oikein

public class RegisterRequest {
    private String kayttajatunnus;
    private String salasana;
    private Rooli rooli;

    // Getters and setters (kriittiset Jacksonin toiminnalle)
    public String getKayttajatunnus() { return kayttajatunnus; }
    public String getSalasana() { return salasana; }
    public Rooli getRooli() { return rooli; }
    public void setKayttajatunnus(String kayttajatunnus) { this.kayttajatunnus = kayttajatunnus; }
    public void setSalasana(String salasana) { this.salasana = salasana; }
    public void setRooli(Rooli rooli) { this.rooli = rooli; }
}