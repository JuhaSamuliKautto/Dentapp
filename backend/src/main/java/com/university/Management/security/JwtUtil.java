package com.university.Management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey; // Varmista, että tämä on javax.crypto
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // HUOM: JWT_TOKEN_VALIDITY on nyt long, kuten application.properties määrittelee
    private final long JWT_TOKEN_VALIDITY; 
    private final SecretKey key;

    // VAIHDA KOKO KONSTRUKTORI TÄHÄN:
    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
        this.JWT_TOKEN_VALIDITY = expiration;
        // TÄRKEÄÄ: Keys.hmacShaKeyFor palauttaa SecretKey:n
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); 
    }

    // Hakee käyttäjänimen tokenista
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Hakee vanhentumispäivämäärän tokenista
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // VAIHDA KOKO FUNKTIO TÄHÄN:
    private Claims getAllClaimsFromToken(String token) {
        // Käytetään Jwts.parser() ja modernia verifyWith(SecretKey)
        return Jwts.parser()
            .verifyWith(key) // TÄMÄ ON NYT SecretKey, joka ratkaisee ongelman
            .build()
            .parseSignedClaims(token)
            .getPayload();
}

    // Tarkistaa, onko token vanhentunut
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Luo token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // VAIHDA KOKO FUNKTIO TÄHÄN:
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .claims(claims) // Moderni tapa
            .subject(subject) // Moderni tapa
            .issuedAt(new Date(System.currentTimeMillis())) // Moderni tapa
            .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)) // Moderni tapa
            .signWith(key) // Moderni tapa, käyttää SecretKey:tä
            .compact();
    }

    // Validoi token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}