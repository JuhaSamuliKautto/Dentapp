package com.university.Management.repository;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Rooli;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// JpaRepository tarjoaa automaattisesti CRUD-metodit (Create, Read, Update, Delete)
public interface KayttajaRepository extends JpaRepository<Kayttaja, Long> {
    
    // Spring Data JPA luo toteutuksen tälle metodille automaattisesti
    Optional<Kayttaja> findByKayttajatunnus(String kayttajatunnus);
    
    List<Kayttaja> findByRooli(Rooli rooli);
}
