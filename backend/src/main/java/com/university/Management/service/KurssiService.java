package com.university.Management.service;

import com.university.Management.model.Kurssi;
import com.university.Management.repository.KurssiRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KurssiService {

    // Riippuvuus Repositoryyn
    private final KurssiRepository kurssiRepository;

    public KurssiService(KurssiRepository kurssiRepository) {
        this.kurssiRepository = kurssiRepository;
    }

    // --- CRUD-metodit ---

    public Kurssi save(Kurssi kurssi) {
        return kurssiRepository.save(kurssi);
    }

    public Optional<Kurssi> findById(Long id) {
        return kurssiRepository.findById(id);
    }
    
    public List<Kurssi> findAll() {
        return kurssiRepository.findAll();
    }
    
    public void deleteById(Long id) {
        kurssiRepository.deleteById(id);
    }

    // --- Controllerien tarvitsemat erikoishakumetodit ---

    /**
     * Hakee kaikki kurssit, joissa tietty käyttäjä (opettaja) on merkitty vastuuopettajaksi.
     */
    public List<Kurssi> findByVastuuOpettajaId(Long vastuuOpettajaId) {
        return kurssiRepository.findByVastuuOpettajaId(vastuuOpettajaId);
    }
    
    /**
     * TÄRKEÄ LOGIIKKA: Tarkistaa, onko annettu käyttäjä (ID) kurssin (ID) vastuuopettaja.
     * Käytetään KurssiControllerin PUT/DELETE-rajapinnoissa valtuutukseen.
     */
    public boolean isVastuuOpettaja(Long kurssiId, Long kayttajaId) {
        return kurssiRepository.existsByIdAndVastuuOpettajaId(kurssiId, kayttajaId);
    }
}