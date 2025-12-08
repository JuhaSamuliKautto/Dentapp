package com.university.Management.repository;

import com.university.Management.model.Kayttaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KayttajaRepository extends JpaRepository<Kayttaja, Long> {

    /**
     * Hakee käyttäjän Kayttaja-olion käyttäjätunnuksen (username) perusteella.
     * Tarvitaan tunnistautuneen käyttäjän hakemiseen SecurityContextHolderista.
     */
    Optional<Kayttaja> findByUsername(String username); 
    
}