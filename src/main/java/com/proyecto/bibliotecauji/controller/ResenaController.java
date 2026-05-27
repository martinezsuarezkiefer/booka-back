package com.proyecto.bibliotecauji.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import com.proyecto.bibliotecauji.dto.CreateResenaDTO;
import com.proyecto.bibliotecauji.model.Libro;
import com.proyecto.bibliotecauji.model.Resena;
import com.proyecto.bibliotecauji.model.Usuario;
import com.proyecto.bibliotecauji.repository.LibroRepository;
import com.proyecto.bibliotecauji.repository.ResenaRepository;
import com.proyecto.bibliotecauji.repository.UsuarioRepository;
import com.proyecto.bibliotecauji.security.UserContextService;
import com.proyecto.bibliotecauji.repository.ResenaLikeRepository;
import com.proyecto.bibliotecauji.dto.ResenaDTO;
import com.proyecto.bibliotecauji.model.ResenaLike;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    private ResenaRepository resenaRepository;
    private LibroRepository libroRepository;
    private UsuarioRepository usuarioRepository;
    private final UserContextService userContext;
    private final ResenaLikeRepository resenaLikeRepository;

    public ResenaController(ResenaRepository resenaRepository, LibroRepository libroRepository,
            UsuarioRepository usuarioRepository, UserContextService userContext, ResenaLikeRepository resenaLikeRepository) {
        this.resenaRepository = resenaRepository;
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
        this.userContext = userContext;
        this.resenaLikeRepository = resenaLikeRepository;
    }

    private ResenaDTO toDTO(
        Resena r,
        Usuario actual
    ) {

        ResenaDTO dto = new ResenaDTO();

        dto.id = r.getId();

        dto.puntuacion = r.getPuntuacion();

        dto.comentario = r.getComentario();

        dto.fecha = r.getFecha();

        dto.usuarioId = r.getUsuario().getId();

        dto.usuarioNombre = r.getUsuario().getNombre();

        dto.usuarioImagen =
            r.getUsuario().getProfileImage();

        dto.likes =
            resenaLikeRepository.countByResena(r);

        dto.likedByMe =
            resenaLikeRepository
                .findByUsuarioAndResena(actual, r)
                .isPresent();

        return dto;
    }

    // Obtener todas las reseñas
    @GetMapping
    public ResponseEntity<List<Resena>> getAllResenas() {
        return ResponseEntity.ok(resenaRepository.findAll());
    }

    // Obtener reseñas de un libro (por su ID interno)
    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<ResenaDTO>>
    getResenasByLibro(
        @PathVariable @NonNull Long libroId
    ) {

        Optional<Libro> libro =
            libroRepository.findById(libroId);

        if (libro.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final Usuario actual;

        try {

            actual = userContext.getUsuarioActual();

        } catch (Exception e) {

            return ResponseEntity.ok(
                resenaRepository.findByLibro(libro.get())
                    .stream()
                    .map(r -> toDTO(r, null))
                    .toList()
            );
        }

        List<ResenaDTO> resenas =
        resenaRepository.findByLibro(libro.get())
            .stream()
            .map(r -> toDTO(r, actual))
            .toList();

        return ResponseEntity.ok(resenas);
    }

    // Obtener reseñas de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resena>> getResenasByUsuario(@PathVariable @NonNull Long usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Resena> resenas = resenaRepository.findByUsuario(usuario.get());
        return ResponseEntity.ok(resenas);
    }

    // Crear una nueva reseña
    @SuppressWarnings("null")
    @PostMapping
    public ResponseEntity<?> createResena(
        @RequestBody CreateResenaDTO dto
    ) {

        Usuario usuario = userContext.getUsuarioActual();

        Libro libro = libroRepository.findById(dto.libroId)
            .orElseThrow(() ->
                new RuntimeException("Libro no encontrado")
            );

        Optional<Resena> existente =
            resenaRepository.findByUsuarioAndLibro(
                usuario,
                libro
            );

        Resena resena;

        if (existente.isPresent()) {

            resena = existente.get();

        } else {

            resena = new Resena();

            resena.setUsuario(usuario);
            resena.setLibro(libro);
        }

        resena.setPuntuacion(dto.puntuacion);
        resena.setComentario(dto.comentario);

        return ResponseEntity.ok(
            resenaRepository.save(resena)
        );
    }

    // Actualizar una reseña existente
    @PutMapping("/{id}")
    public ResponseEntity<Resena> updateResena(@PathVariable @NonNull Long id, @RequestBody Resena updatedResena) {
        Optional<Resena> existing = resenaRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Resena resena = existing.get();
        resena.setComentario(updatedResena.getComentario());
        resena.setPuntuacion(updatedResena.getPuntuacion());
        return ResponseEntity.ok(resenaRepository.save(resena));
    }

    // Eliminar una reseña
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResena(@PathVariable @NonNull Long id) {
        if (!resenaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        resenaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Dar like a reseña
    @SuppressWarnings("null")
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeResena(
        @PathVariable Long id
    ) {

        Usuario usuario =
            userContext.getUsuarioActual();

        Resena resena =
            resenaRepository.findById(id)
                .orElseThrow();

        boolean existe =
            resenaLikeRepository
                .findByUsuarioAndResena(
                    usuario,
                    resena
                )
                .isPresent();

        if (!existe) {

            ResenaLike like =
                new ResenaLike();

            like.setUsuario(usuario);

            like.setResena(resena);

            resenaLikeRepository.save(like);
        }

        return ResponseEntity.ok().build();
    }

    // Quitar like a reseña
    @DeleteMapping("/{id}/like")
    public ResponseEntity<?> unlikeResena(
        @PathVariable Long id
    ) {

        Usuario usuario =
            userContext.getUsuarioActual();

        @SuppressWarnings("null")
        Resena resena =
            resenaRepository.findById(id)
                .orElseThrow();

        resenaLikeRepository
            .findByUsuarioAndResena(
                usuario,
                resena
            )
            .ifPresent(resenaLikeRepository::delete);

        return ResponseEntity.ok().build();
    }

}
