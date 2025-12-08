package com.university.Management.repository;

import com.university.Management.model.Kurssi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KurssiRepository extends JpaRepository<Kurssi, Long> {

    /**
     * Hakee kaikki kurssit, joissa annettu Kayttaja-ID (VastuuOpettaja) on merkitty opettajaksi.
     */
    List<Kurssi> findByVastuuOpettajaId(Long vastuuOpettajaId);

    /**
     * UUSI METODI: Tarkistaa, onko tietty kurssi (ID) annetun opettajan (ID) luoma.
     * Käytetään KurssiService.isVastuuOpettaja-metodissa.
     * @param kurssiId Kurssin ID
     * @param vastuuOpettajaId Opettajan ID
     * @return true, jos omistajuus täsmää.
     */
    boolean existsByIdAndVastuuOpettajaId(Long kurssiId, Long vastuuOpettajaId);
}