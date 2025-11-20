package com.university.Management.model;

public enum SuoritusTila {
    // Lisätty KESKEN-tila alustusta varten
    KESKEN,
    
    // Oppilas on kuitannut kortin valmiiksi, odottaa opettajan arviota
    KUITATTU, 
    
    // Opettaja on hyväksynyt suorituksen
    HYVAKSYTTY, 
    
    // Opettaja on hylännyt suorituksen, vaatii korjausta
    HYLATTY
}