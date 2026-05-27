package com.proyecto.booka.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.proyecto.booka.model.Usuario;
import com.proyecto.booka.repository.UsuarioRepository;

@Service
public class UserContextService {

    private final UsuarioRepository usuarioRepository;

    public UserContextService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario getUsuarioActual() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
