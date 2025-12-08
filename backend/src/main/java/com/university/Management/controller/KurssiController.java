package com.university.Management.controller;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Kurssi;
import com.university.Management.service.KurssiService;
import com.university.Management.service.KayttajaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kurssi") // KORJAUS: Muutettu /api/kurssit -> /api/kurssi
@CrossOrigin(origins = "http://localhost:5173")
public class KurssiController {

    private final KurssiService kurssiService;
    private final KayttajaService kayttajaService;

    public KurssiController(KurssiService kurssiService, KayttajaService kayttajaService) {
        this.kurssiService = kurssiService;
        this.kayttajaService = kayttajaService;
    }

    private Kayttaja getAuthenticatedKayttajaOrThrow() {
        return kayttajaService.getAuthenticatedUser()
                .orElseThrow(() -> new IllegalStateException("Autentikoitu käyttäjä ei ole enää löydettävissä."));
    }

    // ------------------- YLEISET HAUT (STUDENT & TEACHER) -------------------

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<List<Kurssi>> findAll() {
        return ResponseEntity.ok(kurssiService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<Kurssi> findById(@PathVariable Long id) {
        return kurssiService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ------------------- OPETTAJAN KURSSIT -------------------

    @GetMapping("/teaching")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<Kurssi>> findCoursesByTeacher() {
        Kayttaja opettaja = getAuthenticatedKayttajaOrThrow();

        // Käytetään ID:tä, kuten KurssiService on määritelty
        List<Kurssi> opettajanKurssit = kurssiService.findByVastuuOpettajaId(opettaja.getId());

        return ResponseEntity.ok(opettajanKurssit);
    }

    // ------------------- LUONTI JA HALLINTA (TEACHER/ADMIN) -------------------

    @PostMapping
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Kurssi> createCourse(@RequestBody Kurssi kurssi) {
        Kayttaja opettaja = getAuthenticatedKayttajaOrThrow();

        kurssi.setVastuuOpettaja(opettaja);

        Kurssi tallennettuKurssi = kurssiService.save(kurssi);
        return ResponseEntity.status(HttpStatus.CREATED).body(tallennettuKurssi);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Kurssi> updateCourse(@PathVariable Long id, @RequestBody Kurssi kurssiDetails) {

        Kayttaja nykyinenKayttaja = getAuthenticatedKayttajaOrThrow();

        // VALTUUTUKSEN TARKISTUS:
        if (nykyinenKayttaja.getRole().equals("TEACHER")
                && !kurssiService.isVastuuOpettaja(id, nykyinenKayttaja.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        return kurssiService.findById(id)
                .map(existingKurssi -> {
                    existingKurssi.setNimi(kurssiDetails.getNimi());
                    existingKurssi.setKuvaus(kurssiDetails.getKuvaus());

                    Kurssi updatedKurssi = kurssiService.save(existingKurssi);
                    return ResponseEntity.ok(updatedKurssi);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {

        Kayttaja nykyinenKayttaja = getAuthenticatedKayttajaOrThrow();

        // VALTUUTUKSEN TARKISTUS:
        if (nykyinenKayttaja.getRole().equals("TEACHER")
                && !kurssiService.isVastuuOpettaja(id, nykyinenKayttaja.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        if (kurssiService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        kurssiService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}