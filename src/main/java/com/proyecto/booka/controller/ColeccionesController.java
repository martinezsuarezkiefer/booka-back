package com.proyecto.booka.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.proyecto.booka.model.Usuario;
import com.proyecto.booka.dto.ColeccionDTO;
import com.proyecto.booka.model.Colecciones;
import com.proyecto.booka.model.ColeccionesId;
import com.proyecto.booka.model.EstadoLectura;
import com.proyecto.booka.model.Libro;
import com.proyecto.booka.repository.LibroRepository;
import com.proyecto.booka.repository.UsuarioRepository;
import com.proyecto.booka.security.UserContextService;

@RestController
@RequestMapping("/user")
public class ColeccionesController {

    @Autowired private LibroRepository libroRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private UserContextService userContext;

    // Añadir a colección
    @SuppressWarnings("null")
    @PostMapping("/coleccion/{idLibro}")
    public ResponseEntity<?> addToCollection(@PathVariable Long idLibro) {

        Usuario usuario = userContext.getUsuarioActual();
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        // Comprobar si ya existe en la colección
        boolean yaExiste = usuario.getLibrosColeccion().stream()
            .anyMatch(c -> c.getLibro().getId().equals(idLibro));

        if (yaExiste) {
            return ResponseEntity.badRequest().body("El libro ya está en tu colección");
        }

        // Crear entidad de relación
        Colecciones nuevo = new Colecciones();
        nuevo.setUsuario(usuario);
        nuevo.setLibro(libro);
        // PrePersist pondrá fecha y estado

        // Asignar ID compuesto
        ColeccionesId id = new ColeccionesId();
        id.setUsuarioId(usuario.getId());
        id.setLibroId(libro.getId());
        nuevo.setId(id);

        usuario.getLibrosColeccion().add(nuevo);

        usuarioRepository.save(usuario);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Libro añadido a tu colección");
        return ResponseEntity.ok(response);
    }

    @SuppressWarnings("null")
    @GetMapping("/coleccion/{idLibro}")
    public ResponseEntity<?> isInCollection(@PathVariable Long idLibro) {

        Usuario authUser = userContext.getUsuarioActual();
        Usuario usuario = usuarioRepository.findById(authUser.getId()).orElseThrow();

        return usuario.getLibrosColeccion().stream()
                .filter(c -> c.getLibro().getId().equals(idLibro))
                .findFirst()
                .map(c -> {
                    Map<String, Object> body = new HashMap<>();
                    body.put("enColeccion", true);
                    body.put("estadoLectura", c.getEstadoLectura());
                    body.put("comentario", c.getComentarioPersonal()); //Como esto puede ser null uso hashmap de momento y evito nullpointer con map
                    body.put("fechaAgregado", c.getFechaAgregado());

                    return ResponseEntity.ok(body);
                })
                .orElse(ResponseEntity.ok(
                        Map.of("enColeccion", false)
                ));
    }



    @PutMapping("/coleccion/{idLibro}/estado")
    public ResponseEntity<?> updateEstado(@PathVariable Long idLibro,
                                        @RequestParam EstadoLectura estado) {

        Usuario usuario = userContext.getUsuarioActual();

        Colecciones entry = usuario.getLibrosColeccion().stream()
                .filter(c -> c.getLibro().getId().equals(idLibro))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Libro no está en tu colección"));

        entry.setEstadoLectura(estado);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("mensaje", "Estado actualizado"));
    }

    @GetMapping("/coleccion")
    public ResponseEntity<?> getColeccionUsuario() {

        Usuario usuario = userContext.getUsuarioActual();

        List<ColeccionDTO> respuesta = usuario.getLibrosColeccion().stream()
                .map(c -> new ColeccionDTO(
                        c.getLibro().getId(),
                        c.getLibro().getTitulo(),
                        c.getLibro().getImagenPortada(),
                        c.getEstadoLectura().name(),
                        c.getFechaAgregado().toString()
                ))
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @SuppressWarnings("null")
    @DeleteMapping("/coleccion/{idLibro}")
    public ResponseEntity<?> removeFromCollection(@PathVariable Long idLibro) {

        Usuario authUser = userContext.getUsuarioActual();

        // recargar usuario desde la base de datos porque con el context no cargo relaciones (lazyloading)
        Usuario usuario = usuarioRepository.findById(authUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Colecciones entry = usuario.getLibrosColeccion().stream()
                .filter(c -> c.getLibro().getId().equals(idLibro))
                .findFirst()
                .orElse(null);

        if (entry == null) {
            return ResponseEntity.badRequest().body("El libro no está en tu colección");
        }

        usuario.getLibrosColeccion().remove(entry);
        usuarioRepository.save(usuario);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Eliminado de la colección");
        return ResponseEntity.ok(response);
    }


    // @SuppressWarnings("null")
    // @PostMapping("/wishlist/{idLibro}")
    // public ResponseEntity<?> addToWishlist(@PathVariable Long idLibro) {

    //     Usuario usuario = userContext.getUsuarioActual();
    //     Libro libro = libroRepository.findById(idLibro)
    //             .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

    //     // Comprobar si ya existe en la wishlist
    //     boolean yaExiste = usuario.getListaDeseados().stream()
    //         .anyMatch(c -> c.getId().equals(idLibro));

    //     if (yaExiste) {
    //         return ResponseEntity.badRequest().body("El libro ya está en tu lista de deseados");
    //     }

    //     usuario.getListaDeseados().add(libro);

    //     usuarioRepository.save(usuario);

    //     Map<String, String> response = new HashMap<>();
    //     response.put("mensaje", "Libro añadido a tu wishlist");
    //     return ResponseEntity.ok(response);
    // }

    // @SuppressWarnings("null")
    // @GetMapping("/wishlist/{idLibro}")
    // public ResponseEntity<?> isInWishlist(@PathVariable Long idLibro) {

    //     Usuario authUser = userContext.getUsuarioActual();
    //     Usuario usuario = usuarioRepository.findById(authUser.getId()).orElseThrow();

    //     return usuario.getListaDeseados().stream()
    //             .filter(c -> c.getId().equals(idLibro))
    //             .findFirst()
    //             .map(c -> {
    //                 Map<String, Object> body = new HashMap<>();
    //                 body.put("enWishlist", true);
    //                 return ResponseEntity.ok(body);
    //             })
    //             .orElse(ResponseEntity.ok(
    //                     Map.of("enWishlist", false)
    //             ));
    // }

    // @GetMapping("/wishlist")
    // public ResponseEntity<List<WishlistItemDTO>> getWishlistUsuario() {
    //     Usuario usuario = userContext.getUsuarioActual();

    //     List<WishlistItemDTO> lista = usuario.getListaDeseados().stream()
    //         .map(l -> new WishlistItemDTO(l.getId(), 
    //                                         l.getTitulo(), 
    //                                         l.getImagenPortada()))
    //         .toList();

    //     return ResponseEntity.ok(lista);
    // }

    // @SuppressWarnings("null")
    // @DeleteMapping("/wishlist/{idLibro}")
    // public ResponseEntity<?> removeFromWishlist(@PathVariable Long idLibro) {

    //     Usuario authUser = userContext.getUsuarioActual();

    //     // recargar usuario desde la base de datos porque con el context no cargo relaciones (lazyloading)
    //     Usuario usuario = usuarioRepository.findById(authUser.getId())
    //             .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    //     Libro libro = usuario.getListaDeseados().stream()
    //             .filter(c -> c.getId().equals(idLibro))
    //             .findFirst()
    //             .orElse(null);

    //     if (libro == null) {
    //         return ResponseEntity.badRequest().body("El libro no está en tu wishlist");
    //     }

    //     usuario.getListaDeseados().remove(libro);
    //     usuarioRepository.save(usuario);

    //     Map<String, String> response = new HashMap<>();
    //     response.put("mensaje", "Eliminado de la wishlist");
    //     return ResponseEntity.ok(response);
    // }
}
