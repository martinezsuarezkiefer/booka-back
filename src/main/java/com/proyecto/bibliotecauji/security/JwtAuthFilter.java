package com.proyecto.bibliotecauji.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, UserDetailsService uds) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = uds;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {

        System.out.println("→ JWT FILTER EJECUTADO");
        System.out.println("HEADER Authorization = " + request.getHeader("Authorization"));

        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token != null) {

        System.out.println("TOKEN RECIBIDO: " + token);
        System.out.println("VALIDANDO TOKEN...");

        boolean valido = jwtUtils.validateJwtToken(token);
        System.out.println("¿VALIDO?: " + valido);

        if (valido) {
            String username = jwtUtils.getUsernameFromJwt(token);
            System.out.println("USERNAME EN EL TOKEN = " + username);

            UserDetails userDetails = null;
            try {
                userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("USERDETAILS CARGADO: " + userDetails.getUsername());
            } catch (Exception e) {
                System.out.println("ERROR loadUserByUsername: " + e.getMessage());
            }

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            System.out.println("AUTH SET: " + SecurityContextHolder.getContext().getAuthentication());
        }
    }


        filterChain.doFilter(request, response);
        
    }
}
