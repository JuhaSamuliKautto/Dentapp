package com.university.Management.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService jwtTokenService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(UserDetailsService jwtTokenService, JwtUtil jwtUtil) {
        this.jwtTokenService = jwtTokenService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("JWT Tokenin luku epäonnistui", e);
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token on vanhentunut", e);
            }
        } else {
            logger.warn("JWT Token ei ala Bearer-etuliitteellä, tai Authorization-otsake puuttuu");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.jwtTokenService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {

                System.out.println("DEBUG: Käyttäjän '" + username + "' roolit luettu tokenista:");
                userDetails.getAuthorities().forEach(a -> System.out.println(" -> Rooli luettu: " + a.getAuthority()));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }
}