package com.university.Management;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Lokikirja;
import com.university.Management.repository.KayttajaRepository;
import com.university.Management.repository.LokikirjaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional; 

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/lokikirja") // TÄMÄ MÄÄRITTÄÄ POLUN
public class LokikirjaController {

    @Autowired
    private LokikirjaRepository lokikirjaRepository;

    @Autowired
    private KayttajaRepository kayttajaRepository;

    // DTO, joka vastaa Postmanissa lähetettävää JSONia
    public static class LokikirjaRequest {
        public Long kayttajaId; 
        public String sisalto;
    }

    @PostMapping // Käsittelee POST-pyynnöt osoitteessa /api/lokikirja
    public ResponseEntity<?> createLogEntry(@RequestBody LokikirjaRequest request) {
        
        // 1. Haetaan Kayttaja
        Optional<Kayttaja> kayttajaOpt = kayttajaRepository.findById(request.kayttajaId);
        
        if (kayttajaOpt.isEmpty()) {
            return new ResponseEntity<>("Käyttäjää ei löydy annetulla ID:llä.", HttpStatus.NOT_FOUND);
        }
        
        Kayttaja kayttaja = kayttajaOpt.get();

        // 2. Luodaan Lokikirja-merkintä
        Lokikirja uusiMerkinta = new Lokikirja();
        uusiMerkinta.setSisalto(request.sisalto);
        uusiMerkinta.setKayttaja(kayttaja);

        // 3. Tallennetaan
        Lokikirja tallennettu = lokikirjaRepository.save(uusiMerkinta);

        return new ResponseEntity<>(tallennettu, HttpStatus.CREATED);
    }
}