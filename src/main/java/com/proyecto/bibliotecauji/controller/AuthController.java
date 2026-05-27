package com.proyecto.bibliotecauji.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.proyecto.bibliotecauji.model.Usuario;
import com.proyecto.bibliotecauji.repository.UsuarioRepository;
import com.proyecto.bibliotecauji.security.JwtUtils;
import com.proyecto.bibliotecauji.dto.*;;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getPerfil(@RequestHeader("Authorization") String auth) {

        System.out.println("AUTH HEADER RECIBIDO EN /auth/me: " + auth);

        String token = auth.replace("Bearer ", "");
        String username = jwtUtils.getUsernameFromJwt(token);

        System.out.println("USERNAME EN EL TOKEN: " + username);

        return ResponseEntity.ok(Map.of("username", username));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtils.generateToken(userDetails.getUsername());

            Usuario u = usuarioRepository.findByEmail(userDetails.getUsername()).get();

            LoginResponse resp = new LoginResponse(token, "Bearer", u.getEmail(), u.getId(), u.getNombre());
            return ResponseEntity.ok(resp);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email ya registrado"));
        }
        Usuario u = new Usuario();
        u.setNombre(req.getNombre());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        usuarioRepository.save(u);
        return ResponseEntity.ok(Map.of("message", "Usuario creado"));
    }
}
