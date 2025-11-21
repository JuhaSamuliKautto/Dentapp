package com.university.Management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Huom: TÄSSÄ KOHTAA EI OLE ENÄÄ @ComponentScan -IMPORTTIA
// eikä @ComponentScan -annotaatiota

@SpringBootApplication
public class ManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class, args);
    }
}