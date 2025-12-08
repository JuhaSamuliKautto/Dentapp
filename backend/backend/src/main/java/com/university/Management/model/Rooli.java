package com.university.Management.model;

/**
 * Rooli-enum, joka määrittää käyttäjien roolit järjestelmässä.
 * Vakiot nimetty isoin kirjaimin (TEACHER, STUDENT) vastaamaan Spring Securityn 
 * hasRole() -metodin oletuskonventiota.
 */
public enum Rooli {
    /**
     * Opettajan rooli, jolla on oikeus luoda, muokata ja poistaa kursseja.
     */
    TEACHER,
    
    /**
     * Oppilaan rooli, jolla on perusoikeudet tarkastella kursseja.
     */
    STUDENT
}