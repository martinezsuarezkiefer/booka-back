package com.proyecto.booka.controller;

import com.proyecto.booka.dto.CategoriaExplorarDTO;
import com.proyecto.booka.dto.LibroShelfDTO;
import com.proyecto.booka.dto.ShelfDTO;
import com.proyecto.booka.model.Categoria;
import com.proyecto.booka.model.Libro;
import com.proyecto.booka.model.Usuario;
import com.proyecto.booka.repository.CategoriaRepository;
import com.proyecto.booka.repository.LibroRepository;
import com.proyecto.booka.repository.UsuarioRepository;
import com.proyecto.booka.security.UserContextService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final LibroRepository libroRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final UserContextService userContext;

    public CategoriaController(
        LibroRepository libroRepository,
        CategoriaRepository categoriaRepository,
        UsuarioRepository usuarioRepository,
        UserContextService userContext
    ) {
        this.libroRepository = libroRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.userContext = userContext;
    }

    // Explorar categorías
    @GetMapping("/explorar")
    public List<CategoriaExplorarDTO>
    explorarCategorias() {

        Usuario usuario = null;

        try {
            usuario = userContext.getUsuarioActual();
        } catch (Exception ignored) {}

        final Usuario actual = usuario;

        List<Map<String, String>> base = List.of(

            Map.of("slug", "fantasy", "nombre", "Fantasy"),

            Map.of("slug", "fiction", "nombre", "Ficción"),

            Map.of("slug", "horror", "nombre", "Horror"),

            Map.of("slug", "romance", "nombre", "Romance"),

            Map.of("slug", "mystery", "nombre", "Misterio"),

            Map.of("slug", "history", "nombre", "Historia"),

            Map.of("slug", "philosophy", "nombre", "Filosofía"),

            Map.of("slug", "psychology", "nombre", "Psicología"),

            Map.of("slug", "computers", "nombre", "Computers"),

            Map.of("slug", "manga", "nombre", "Manga")
        );

        return base.stream()
            .map(cat -> {

                boolean favorita = false;

                if (actual != null) {

                    favorita = actual
                        .getCategoriasFavoritas()
                        .stream()
                        .anyMatch(c ->
                            c.getNombre()
                                .equalsIgnoreCase(
                                    cat.get("slug")
                                )
                        );
                }

                return new CategoriaExplorarDTO(
                    cat.get("slug"),
                    cat.get("nombre"),
                    favorita
                );

            })
            .toList();
    }

    @GetMapping("/{slug}/libros")
    public Page<Libro> getLibrosPorCategoria(
        @PathVariable String slug,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {

        return libroRepository.findByCategoriaTema(
            slug,
            PageRequest.of(page, size)
        );
    }

    @GetMapping("/buscar")
    public List<Categoria> buscarCategorias(
        @RequestParam String q
    ) {

        return categoriaRepository
            .findTop20ByNombreContainingIgnoreCase(q);
    }

    @PostMapping("/{slug}/favorita")
    public ResponseEntity<?> addFavorita(
        @PathVariable String slug
    ) {

        Usuario usuario = userContext.getUsuarioActual();

        Categoria categoria = categoriaRepository
            .findFirstByNombreContainingIgnoreCase(slug)
            .orElseGet(() -> {

                Categoria nueva = new Categoria();
                nueva.setNombre(slug);

                return categoriaRepository.save(nueva);
            });

        usuario.getCategoriasFavoritas()
            .add(categoria);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{slug}/favorita")
    public ResponseEntity<?> removeFavorita(
        @PathVariable String slug
    ) {

        Usuario usuario = userContext.getUsuarioActual();

        usuario.getCategoriasFavoritas()
            .removeIf(c ->
                c.getNombre().equalsIgnoreCase(slug)
            );

        usuarioRepository.save(usuario);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{slug}/favorita")
    public Map<String, Boolean> isFavorita(
        @PathVariable String slug
    ) {

        Usuario usuario = userContext.getUsuarioActual();

        boolean favorita = usuario
            .getCategoriasFavoritas()
            .stream()
            .anyMatch(c ->
                c.getNombre().equalsIgnoreCase(slug)
            );

        return Map.of("favorita", favorita);
    }

    // Carruseles categorías
    @GetMapping("/shelves")
    public List<ShelfDTO> shelves() {

        List<String> categorias = List.of(
            "philosophy",
            "history",
            "psychology",
            "fiction",
            "computers"
        );

        return categorias.stream()
            .map(cat -> {

                ShelfDTO dto = new ShelfDTO();

                dto.nombre =
                    Character.toUpperCase(cat.charAt(0))
                    + cat.substring(1);

                dto.slug = cat;

                dto.libros =
                    libroRepository.findByCategoriaTema(
                        cat,
                        PageRequest.of(0, 6)
                    )
                    .getContent()
                    .stream()
                    .map(libro -> {

                        LibroShelfDTO l =
                            new LibroShelfDTO();

                        l.id = libro.getId();
                        l.titulo = libro.getTitulo();
                        l.imagenPortada = libro.getImagenPortada();
                        l.autor =
                        libro.getAutores() != null
                        && !libro.getAutores().isEmpty()
                        ? libro.getAutores()
                            .iterator()
                            .next()
                            .getNombre()
                        : "Autor desconocido";

                        return l;
                    })
                    .toList();

                return dto;

            })
            .toList();
    }
}