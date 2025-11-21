package com.university.Management.repository;

import com.university.Management.model.Lokikirja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LokikirjaRepository extends JpaRepository<Lokikirja, Long> {

    List<Lokikirja> findByKayttajaId(Long kayttajaId);
}