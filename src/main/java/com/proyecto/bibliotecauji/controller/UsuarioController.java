package com.proyecto.bibliotecauji.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.proyecto.bibliotecauji.dto.ReviewCardDTO;
import com.proyecto.bibliotecauji.dto.UpdateProfileDTO;

import com.proyecto.bibliotecauji.model.PerfilComentario;
import com.proyecto.bibliotecauji.model.Resena;
import com.proyecto.bibliotecauji.model.Usuario;
import com.proyecto.bibliotecauji.repository.UsuarioRepository;
import com.proyecto.bibliotecauji.repository.ResenaLikeRepository;
import com.proyecto.bibliotecauji.security.UserContextService;
import com.proyecto.bibliotecauji.service.PerfilService;
import com.proyecto.bibliotecauji.dto.UsuarioMiniDTO;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PerfilService perfilService;
    @Autowired private UserContextService userContext;
    private final PasswordEncoder passwordEncoder;
    private final ResenaLikeRepository resenaLikeRepository;

    public UsuarioController(UsuarioRepository usuarioRepository, PerfilService perfilService,
            UserContextService userContext, PasswordEncoder passwordEncoder, ResenaLikeRepository resenaLikeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilService = perfilService;
        this.userContext = userContext;
        this.passwordEncoder = passwordEncoder;
        this.resenaLikeRepository = resenaLikeRepository;
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable @NonNull Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @SuppressWarnings("null")
    @PutMapping("/me")
    public ResponseEntity<?> updateMe(
        @RequestBody UpdateProfileDTO dto
    ) {

        Usuario usuario =
            userContext.getUsuarioActual();

        // nombre
        if (
            dto.nombre != null
            && !dto.nombre.isBlank()
        ) {
            usuario.setNombre(dto.nombre);
        }

        // email
        if (
            dto.email != null
            && !dto.email.isBlank()
        ) {
            usuario.setEmail(dto.email);
        }

        // avatar
        if (dto.profileImage != null) {

            usuario.setProfileImage(
                dto.profileImage
            );
        }

        // password
        if (
            dto.newPassword != null
            && !dto.newPassword.isBlank()
        ) {

            boolean correcta =
                passwordEncoder.matches(
                    dto.currentPassword,
                    usuario.getPassword()
                );

            if (!correcta) {

                return ResponseEntity
                    .badRequest()
                    .body("Contraseña actual incorrecta");
            }

            usuario.setPassword(
                passwordEncoder.encode(
                    dto.newPassword
                )
            );
        }

        usuarioRepository.save(usuario);

        return ResponseEntity.ok().build();
    }

    // Obtener perfil de usuario
    @GetMapping("/{id}/perfil")
    public ResponseEntity<?> getPerfil(@PathVariable Long id) {
        return ResponseEntity.ok(perfilService.getPerfil(id));
    }

    @SuppressWarnings("null")
    @PostMapping("/{id}/comentarios")
    public ResponseEntity<?> comentarPerfil(@PathVariable Long id,
                                            @RequestBody Map<String, String> body) {

        Usuario autor = userContext.getUsuarioActual();

        if (autor.getId().equals(id)) {
            return ResponseEntity.badRequest().body("No puedes comentarte a ti mismo");
        }

        Usuario perfil = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String mensaje = body.get("mensaje");

        if (mensaje == null || mensaje.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El mensaje no puede estar vacío");
        }

        PerfilComentario comentario = new PerfilComentario();
        comentario.setUsuarioAutor(autor);
        comentario.setUsuarioPerfil(perfil);
        comentario.setMensaje(mensaje);

        perfil.getComentariosRecibidos().add(comentario);

        usuarioRepository.save(perfil);

        return ResponseEntity.ok().build();
    }

    // Obtener usuario por email
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> getUsuarioByEmail(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear nuevo usuario
    @PostMapping
    public Usuario createUsuario(@RequestBody @NonNull Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable @NonNull Long id, @RequestBody Usuario detallesUsuario) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = optionalUsuario.get();
        usuario.setNombre(detallesUsuario.getNombre());
        usuario.setEmail(detallesUsuario.getEmail());
        usuario.setPassword(detallesUsuario.getPassword());

        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable @NonNull Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Seguir usuario
    @SuppressWarnings("null")
    @PostMapping("/{id}/seguir")
    public ResponseEntity<?> seguirUsuario(@PathVariable Long id) {

        Usuario actual = userContext.getUsuarioActual();

        if (actual.getId().equals(id)) {
            return ResponseEntity.badRequest().body("No puedes seguirte a ti mismo");
        }

        Usuario objetivo = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        actual.getSeguidos().add(objetivo);

        usuarioRepository.save(actual);

        return ResponseEntity.ok().build();
    }

    // Dejar de seguir usuario
    @SuppressWarnings("null")
    @DeleteMapping("/{id}/seguir")
    public ResponseEntity<?> dejarDeSeguir(@PathVariable Long id) {

        Usuario actual = userContext.getUsuarioActual();

        Usuario objetivo = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        actual.getSeguidos().remove(objetivo);

        usuarioRepository.save(actual);

        return ResponseEntity.ok().build();
    }

    // Saber si estás siguiendo (para el botón)
    @GetMapping("/{id}/siguiendo")
    public ResponseEntity<?> siguiendo(@PathVariable Long id) {

        Usuario actual = userContext.getUsuarioActual();

        boolean sigue = actual.getSeguidos()
            .stream()
            .anyMatch(u -> u.getId().equals(id));

        return ResponseEntity.ok(Map.of("siguiendo", sigue));
    }

    // Seguidores para mostrar donde sea y en el modal
    @SuppressWarnings("null")
    @GetMapping("/{id}/seguidores")
    public ResponseEntity<?> seguidores(@PathVariable Long id) {

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<UsuarioMiniDTO> lista = usuario.getSeguidores()
            .stream()
            .map(u -> new UsuarioMiniDTO(
                u.getId(),
                u.getNombre(),
                u.getProfileImage()
            ))
            .toList();

        return ResponseEntity.ok(lista);
    }

    // Siguiendo para mostrar donde sea y en el modal(?)
    @SuppressWarnings("null")
    @GetMapping("/{id}/siguiendo-lista")
    public ResponseEntity<?> siguiendoLista(@PathVariable Long id) {

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<UsuarioMiniDTO> lista = usuario.getSeguidos()
            .stream()
            .map(u -> new UsuarioMiniDTO(
                u.getId(),
                u.getNombre(),
                u.getProfileImage()
            ))
            .toList();

        return ResponseEntity.ok(lista);
    }

    // Reseñas
    @SuppressWarnings("null")
    @GetMapping("/{id}/resenas")
    public List<ReviewCardDTO> resenasUsuario(
        @PathVariable Long id
    ) {

        Usuario usuario =
            usuarioRepository.findById(id)
                .orElseThrow();

        return usuario.getResenas()
            .stream()
            .sorted(
                Comparator.comparing(
                    Resena::getFecha
                ).reversed()
            )
            .map(r -> {

                Usuario actual = null;

                    try {

                        actual =
                            userContext.getUsuarioActual();

                    } catch (Exception e) {}

                ReviewCardDTO dto =
                    new ReviewCardDTO();

                dto.id = r.getId();

                dto.libroId =
                    r.getLibro().getId();

                dto.libroTitulo =
                    r.getLibro().getTitulo();

                dto.libroImagen =
                    r.getLibro()
                        .getImagenPortada();

                dto.puntuacion =
                    r.getPuntuacion();

                dto.comentario =
                    r.getComentario();

                dto.likes =
                    resenaLikeRepository
                        .countByResena(r);

                dto.fecha =
                    r.getFecha();

                dto.usuarioId =
                    usuario.getId();

                dto.usuarioNombre =
                    usuario.getNombre();

                dto.usuarioImagen =
                    usuario.getProfileImage();

                dto.likedByMe =
                    actual != null
                    && resenaLikeRepository
                        .findByUsuarioAndResena(
                            actual,
                            r
                        )
                        .isPresent();

                return dto;
            })
            .toList();
    }

}
