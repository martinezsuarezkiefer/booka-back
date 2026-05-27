package com.proyecto.bibliotecauji.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

            // auth pública
            .requestMatchers("/auth/**").permitAll()
            
            // noticias
            .requestMatchers(HttpMethod.GET,"/noticias/**").permitAll()

            // recursos públicos
            .requestMatchers("/uploads/**", "/images/**", "/css/**", "/js/**", "/favicon.ico").permitAll()

            // libros y búsqueda públicos
            .requestMatchers("/libros/**").permitAll()
            .requestMatchers("/buscar/**").permitAll()

            // categorías públicas
            .requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()

            // favoritas categorías => login requerido
            .requestMatchers(HttpMethod.POST, "/categorias/*/favorita").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/categorias/*/favorita").authenticated()
            .requestMatchers(HttpMethod.GET, "/categorias/*/favorita").authenticated()

            // reseñas
            .requestMatchers(HttpMethod.POST, "/resenas").authenticated()
            .requestMatchers(HttpMethod.PUT, "/resenas/**").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/resenas/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/resenas/**").permitAll()
            .requestMatchers(HttpMethod.POST,"/resenas/*/like").authenticated()
            .requestMatchers(HttpMethod.DELETE,"/resenas/*/like").authenticated()

            // perfil público
            .requestMatchers(HttpMethod.GET, "/api/usuarios/*/perfil").permitAll()

            // seguir / dejar seguir / comprobar siguiendo => requieren login
            .requestMatchers(HttpMethod.GET, "/api/usuarios/*/siguiendo").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/usuarios/*/seguir").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/api/usuarios/*/seguir").authenticated()
            
            // seguir autores => login requerido
            .requestMatchers(HttpMethod.GET, "/autores/*/siguiendo").authenticated()
            .requestMatchers(HttpMethod.GET, "/autores/seguidos").authenticated()

            .requestMatchers(HttpMethod.POST, "/autores/*/seguir").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/autores/*/seguir").authenticated()

            // autores públicos
            .requestMatchers(HttpMethod.GET, "/autores/**").permitAll()

            // comentar perfil => requiere login
            .requestMatchers(HttpMethod.POST, "/api/usuarios/*/comentarios").authenticated()

            // subir avatar
            .requestMatchers(HttpMethod.POST,"/api/usuarios/*/imagen").authenticated()

            // resto GET usuarios públicos (buscar/listados)
            .requestMatchers(HttpMethod.GET, "/api/usuarios/**").permitAll()

            // resto API usuarios protegida
            .requestMatchers("/api/usuarios/**").authenticated()
            .requestMatchers(HttpMethod.PUT,"/api/usuarios/me").authenticated()

            // zona privada user
            .requestMatchers("/user/**").authenticated()

            // feed personal
            .requestMatchers("/api/feed/**").authenticated()

            .anyRequest().authenticated()
        )
            .addFilterBefore(new JwtAuthFilter(jwtUtils, userDetailsService),
                            UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
