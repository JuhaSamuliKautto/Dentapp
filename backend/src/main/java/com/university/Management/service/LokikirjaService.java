package com.university.Management.service;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Lokikirja;
import com.university.Management.repository.LokikirjaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LokikirjaService {

    private final LokikirjaRepository lokikirjaRepository;

    public LokikirjaService(LokikirjaRepository lokikirjaRepository) {
        this.lokikirjaRepository = lokikirjaRepository;
    }

    public Lokikirja save(Lokikirja lokikirja) {
        return lokikirjaRepository.save(lokikirja);
    }

    public Optional<Lokikirja> findById(Long id) {
        return lokikirjaRepository.findById(id);
    }

    public List<Lokikirja> findByKayttaja(Kayttaja kayttaja) {
        return lokikirjaRepository.findByKayttaja(kayttaja);
    }

    public void deleteById(Long id) {
        lokikirjaRepository.deleteById(id);
    }

    /**
     * TÄRKEÄ METODI: Tarkistaa, onko annettu käyttäjä (ID) kyseisen lokikirjan omistaja.
     * Käytetään LokikirjaControllerin ja SuoritusControllerin valtuutukseen.
     */
    public boolean isLokikirjanOmistaja(Long lokikirjaId, Long kayttajaId) {
        return lokikirjaRepository.existsByIdAndKayttajaId(lokikirjaId, kayttajaId);
    }
}