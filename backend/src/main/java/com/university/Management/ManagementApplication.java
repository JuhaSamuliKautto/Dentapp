package com.university.Management;

import com.university.Management.model.Kayttaja;
import com.university.Management.model.Rooli;
import com.university.Management.repository.KayttajaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class, args);
    }
    
    /**
     * Tämä metodi luo testikäyttäjät tietokantaan sovelluksen käynnistyessä.
     */
    @Bean
    public CommandLineRunner initData(KayttajaRepository kayttajaRepository) {
        return args -> {
            System.out.println("--- Tietokannan alustaminen ---");
            
            // 1. Luo testiopettaja (teacher/teacher)
            if (kayttajaRepository.findByKayttajatunnus("teacher").isEmpty()) {
                Kayttaja opettaja = new Kayttaja();
                opettaja.setKayttajatunnus("teacher");
                opettaja.setSalasanaHash("teacher"); 
                opettaja.setRooli(Rooli.OPETTAJA);
                kayttajaRepository.save(opettaja);
                System.out.println("Luotu testiopettaja: teacher/teacher");
            }

            // 2. Luo testioppilas (student/student)
            if (kayttajaRepository.findByKayttajatunnus("student").isEmpty()) {
                Kayttaja oppilas = new Kayttaja();
                oppilas.setKayttajatunnus("student");
                oppilas.setSalasanaHash("student"); 
                oppilas.setRooli(Rooli.OPPILAS);
                kayttajaRepository.save(oppilas);
                System.out.println("Luotu testioppilas: student/student");
            }
            System.out.println("--- Alustus valmis ---");
        };
    }
}