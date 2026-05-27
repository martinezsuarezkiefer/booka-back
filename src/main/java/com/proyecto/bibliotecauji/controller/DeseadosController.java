package com.proyecto.bibliotecauji.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.proyecto.bibliotecauji.model.Libro;
import com.proyecto.bibliotecauji.model.Usuario;
import com.proyecto.bibliotecauji.dto.WishlistItemDTO;
import com.proyecto.bibliotecauji.model.Deseado;
import com.proyecto.bibliotecauji.model.DeseadoId;
import com.proyecto.bibliotecauji.repository.LibroRepository;
import com.proyecto.bibliotecauji.repository.UsuarioRepository;
import com.proyecto.bibliotecauji.security.UserContextService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/wishlist")
public class DeseadosController {

    @Autowired private LibroRepository libroRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private UserContextService userContext;

    @SuppressWarnings("null")
    @PostMapping("/{idLibro}")
    public ResponseEntity<?> addToWishlist(@PathVariable Long idLibro) {

        Usuario usuario = userContext.getUsuarioActual();
        Libro libro = libroRepository.findById(idLibro)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        boolean existe = usuario.getListaDeseados().stream()
            .anyMatch(d -> d.getLibro().getId().equals(idLibro));

        if (existe) return ResponseEntity.badRequest().body("El libro ya está en tu wishlist");

        Deseado nuevo = new Deseado();
        nuevo.setUsuario(usuario);
        nuevo.setLibro(libro);

        DeseadoId id = new DeseadoId(usuario.getId(), libro.getId());
        nuevo.setId(id);

        usuario.getListaDeseados().add(nuevo);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("mensaje", "Añadido a wishlist"));
    }

    @GetMapping
    public ResponseEntity<?> getWishlist() {
        Usuario usuario = userContext.getUsuarioActual();

        List<WishlistItemDTO> lista = usuario.getListaDeseados().stream()
            .map(l -> new WishlistItemDTO(l.getLibro().getId(), 
                                            l.getLibro().getTitulo(), 
                                            l.getLibro().getImagenPortada(),
                                            l.getFechaAgregado()))
            .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{idLibro}")
    public ResponseEntity<?> isInWishlist(@PathVariable Long idLibro) {

        Usuario usuario = userContext.getUsuarioActual();

        return usuario.getListaDeseados().stream()
            .filter(d -> d.getLibro().getId().equals(idLibro))
            .findFirst()
            .map(d -> Map.of("enWishlist", true, "fechaAgregado", d.getFechaAgregado()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.ok(Map.of("enWishlist", false)));
    }

    @DeleteMapping("/{idLibro}")
    public ResponseEntity<?> remove(@PathVariable Long idLibro) {

        Usuario usuario = userContext.getUsuarioActual();

        Deseado item = usuario.getListaDeseados().stream()
            .filter(d -> d.getLibro().getId().equals(idLibro))
            .findFirst()
            .orElse(null);

        if (item == null)
            return ResponseEntity.badRequest().body("No está en la wishlist");

        usuario.getListaDeseados().remove(item);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("mensaje", "Eliminado"));
    }
}
