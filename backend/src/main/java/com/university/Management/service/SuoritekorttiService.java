package com.university.Management.service;

import com.university.Management.model.Suoritekortti;
import com.university.Management.repository.SuoritekorttiRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuoritekorttiService {

    private final SuoritekorttiRepository korttiRepository;

    public SuoritekorttiService(SuoritekorttiRepository korttiRepository) {
        this.korttiRepository = korttiRepository;
    }

    public Suoritekortti save(Suoritekortti kortti) {
        return korttiRepository.save(kortti);
    }

    public Optional<Suoritekortti> findById(Long id) {
        return korttiRepository.findById(id);
    }
    
    public List<Suoritekortti> findAll() {
        return korttiRepository.findAll();
    }
    
    public List<Suoritekortti> findByLuojaId(Long luojaId) {
        return korttiRepository.findByLuojaId(luojaId);
    }

    public void deleteById(Long id) {
        korttiRepository.deleteById(id);
    }
    
    /**
     * Tarkistaa, onko annettu käyttäjä (opettaja) kortin luoja.
     * TÄMÄ ON TARPEEN, jos korttien muokkaus/poisto sallitaan vain luojalle.
     */
    public boolean isKortinLuoja(Long korttiId, Long luojaId) {
        // Huom: Koska emme määrittäneet tätä metodia Repositoryyn, meidän täytyy tarkistaa se tässä.
        // Optimaalisempi tapa olisi käyttää Repositoryn existsByIdAndLuojaId(id, luojaId) -metodia.
        // Ladataan kortti ja tarkistetaan omistaja:
        return korttiRepository.findById(korttiId)
            .map(kortti -> kortti.getLuoja().getId().equals(luojaId))
            .orElse(false);
    }
}