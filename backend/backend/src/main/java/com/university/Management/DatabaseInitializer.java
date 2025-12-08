package com.university.Management;

import com.university.Management.model.*;
import com.university.Management.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initializeDatabase(
            KayttajaRepository kayttajaRepository,
            KurssiRepository kurssiRepository,
            SuoritekorttiRepository suoritekorttiRepository,
            LokikirjaRepository lokikirjaRepository,
            SuoritusRepository suoritusRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // Estetään turha luominen, jos käyttäjiä on jo
            if (kayttajaRepository.findByUsername("opettaja@uni.fi").isPresent()) {
                System.out.println("--- Tietokanta jo alustettu, ohitetaan DatabaseInitializer ---");
                return;
            }

            System.out.println("--- Alustetaan tietokanta testidatalla ---");

            // 1. KÄYTTÄJIEN LUOMINEN
            String encryptedPassword = passwordEncoder.encode("password");

            Kayttaja opettaja = new Kayttaja();
            opettaja.setUsername("opettaja@uni.fi");
            opettaja.setSalasana(encryptedPassword);
            opettaja.setNimi("Opettaja Pätevä");
            opettaja.setEmail("opettaja@uni.fi");
            opettaja.setRole("TEACHER"); // <-- Opettajan rooli asetettu!
            kayttajaRepository.save(opettaja);

            Kayttaja opiskelija = new Kayttaja();
            opiskelija.setUsername("oppilas@uni.fi");
            opiskelija.setSalasana(encryptedPassword);
            opiskelija.setNimi("Oppilas Innokas");
            opiskelija.setEmail("oppilas@uni.fi");
            opiskelija.setRole("STUDENT");
            kayttajaRepository.save(opiskelija);

            // 2. KURSSIN LUOMINEN
            Kurssi kurssi = new Kurssi();
            kurssi.setNimi("Digitaalinen Erityisosaaminen");
            kurssi.setKuvaus("Kurssin testikuvaus");
            kurssi.setVastuuOpettaja(opettaja);
            kurssiRepository.save(kurssi);

            // 3. SUORITEKORTTIEN LUOMINEN
            Suoritekortti kortti1 = new Suoritekortti();
            kortti1.setNimi("Perusteet hallussa");
            kortti1.setSisaltoJson("{\"info\":\"Sovelluksen asennus ja peruskäyttö.\" }");
            kortti1.setLuoja(opettaja);
            suoritekorttiRepository.save(kortti1);

            Suoritekortti kortti2 = new Suoritekortti();
            kortti2.setNimi("Edistynyt tietoturva");
            kortti2.setSisaltoJson("{\"info\":\"Kaksivaiheinen tunnistautuminen ja salaus.\" }");
            kortti2.setLuoja(opettaja);
            suoritekorttiRepository.save(kortti2);

            // 4. LOKIKIRJAN LUOMINEN
            Lokikirja lokikirja = new Lokikirja();
            lokikirja.setKayttaja(opiskelija);
            lokikirja.setKurssi(kurssi);
            lokikirja.setSisalto("Tämä on opiskelijan alustuslokikirja.");
            lokikirjaRepository.save(lokikirja);

            // 5. SUORITUKSEN LUOMINEN
            Suoritus suoritus1 = new Suoritus();
            suoritus1.setLokikirja(lokikirja);
            suoritus1.setSuoritekortti(kortti1);
            suoritus1.setTila(SuoritusTila.ODOTTAA_HYVAKSYNTAA);
            suoritus1.setLuontiAika(LocalDateTime.now().minusDays(1));
            suoritusRepository.save(suoritus1);

            System.out.println("--- Tietokanta alustettu onnistuneesti! ---");
            System.out.println("Opettaja: opettaja@uni.fi / password");
            System.out.println("Oppilas: oppilas@uni.fi / password");
        };
    }
}