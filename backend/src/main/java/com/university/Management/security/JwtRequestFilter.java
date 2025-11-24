package com.university.Management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    private final String HEADER_STRING = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        String header = request.getHeader(HEADER_STRING);
        // Poistettu: String username = null;
        // Poistettu: String authToken = null;

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            String authToken = header.replace(TOKEN_PREFIX, "");
            
            try {
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(jwtTokenService.getSigningKey())
                        .build()
                        .parseClaimsJws(authToken);

                String username = claimsJws.getBody().getSubject();
                String role = (String) claimsJws.getBody().get("role");
                
                // Määritellään roolit (Authorities) Spring Securitylle
                // TÄRKEÄÄ: Lisätään "ROLE_" etuliite, jota Spring Security odottaa!
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role)); 
                
                // TÄRKEÄ KORJAUS/LISÄYS: Varmistetaan, että käyttäjätunnus löytyi tokenista
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = new User(username, "", authorities);
                    
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (SignatureException e) {
                logger.warn("Invalid JWT Signature (403 expected for invalid token)", e);
            } catch (Exception e) {
                logger.warn("JWT processing failed: " + e.getMessage(), e);
            }
        }
        
        chain.doFilter(request, response);
    }
}