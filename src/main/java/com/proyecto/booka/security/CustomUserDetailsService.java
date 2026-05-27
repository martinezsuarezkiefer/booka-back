package com.proyecto.booka.security;

import com.proyecto.booka.model.Usuario;
import com.proyecto.booka.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        return org.springframework.security.core.userdetails.User
            .withUsername(usuario.getEmail())
            .password(usuario.getPassword())
            .authorities(usuario.getRol())
            .build();
    }
}
