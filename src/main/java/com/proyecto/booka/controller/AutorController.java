package com.proyecto.booka.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Comparator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.booka.dto.AutorDetailDTO;
import com.proyecto.booka.dto.AutorMiniDTO;
import com.proyecto.booka.dto.LibroShelfDTO;
import com.proyecto.booka.model.Autor;
import com.proyecto.booka.model.Categoria;
import com.proyecto.booka.model.Libro;
import com.proyecto.booka.model.SeguimientoAutor;
import com.proyecto.booka.model.Usuario;
import com.proyecto.booka.repository.AutorRepository;
import com.proyecto.booka.repository.LibroRepository;
import com.proyecto.booka.repository.SeguimientoAutorRepository;
import com.proyecto.booka.security.UserContextService;
import com.proyecto.booka.service.AuthorMetadataService;;

@RestController
@RequestMapping("/autores")
public class AutorController {

    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;
    private final UserContextService userContext;
    private final AuthorMetadataService authorMetadataService;
    private final SeguimientoAutorRepository seguimientoAutorRepository;

    public AutorController(
        AutorRepository autorRepository,
        UserContextService userContext,
        LibroRepository libroRepository,
        AuthorMetadataService authorMetadataService,
        SeguimientoAutorRepository seguimientoAutorRepository
    ) {
        this.autorRepository = autorRepository;
        this.userContext = userContext;
        this.libroRepository = libroRepository;
        this.authorMetadataService = authorMetadataService;
        this.seguimientoAutorRepository = seguimientoAutorRepository;
    }

    @SuppressWarnings("null")
    @GetMapping("/{id}")
    public AutorDetailDTO detail(
        @PathVariable Long id
    ) {

        Autor autor = autorRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Autor no encontrado")
            );

        Usuario actual = null;

        try {
            actual = userContext.getUsuarioActual();
        } catch (Exception e) {}

        if (
            autor.getFotoUrl() == null
            || autor.getFotoUrl().isBlank()
        ) {

            System.out.println(
                "BUSCANDO FOTO PARA: "
                + autor.getNombre()
            );

            String foto =
                authorMetadataService
                    .buscarFotoAutor(
                        autor.getNombre()
                    );

            System.out.println(
                "FOTO RECIBIDA: "
                + foto
            );

            if (foto != null) {

                autor.setFotoUrl(foto);

            } else {

                System.out.println(
                    "NO SE ENCONTRO FOTO"
                );

                autor.setFotoUrl("NO_PHOTO");
            }

            autorRepository.save(autor);
        }

        AutorDetailDTO dto =
            new AutorDetailDTO();

        dto.id = autor.getId();
        dto.nombre = autor.getNombre();
        dto.fotoUrl = autor.getFotoUrl();
        dto.biografia = autor.getBiografia();

        dto.seguidores =
            seguimientoAutorRepository
                .countByAutor(autor);

        dto.seguido =
            actual != null
            && actual.getAutoresSeguidos()
                .stream()
                .map(SeguimientoAutor::getAutor)
                .anyMatch(a ->
                    a.getId().equals(
                        autor.getId()
                    )
                );

        dto.libros = autor.getLibros()
            .stream()
            .map(libro -> {

                LibroShelfDTO l =
                    new LibroShelfDTO();

                l.id = libro.getId();
                l.titulo = libro.getTitulo();
                l.imagenPortada =
                    libro.getImagenPortada();

                return l;
            })
            .toList();

        Map<String, Integer> categorias =
            new HashMap<>();

        for (Libro libro : autor.getLibros()) {

            for (Categoria categoria :
                libro.getCategorias()) {

                categorias.put(
                    categoria.getNombre(),
                    categorias.getOrDefault(
                        categoria.getNombre(),
                        0
                    ) + 1
                );
            }
        }

        dto.categoriasPrincipales =
            categorias.entrySet()
                .stream()
                .sorted((a, b) ->
                    b.getValue()
                    .compareTo(a.getValue())
                )
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        double media =
            autor.getLibros()
                .stream()
                .filter(l ->
                    l.getValoracionMedia() != null
                )
                .mapToDouble(
                    Libro::getValoracionMedia
                )
                .average()
                .orElse(0);

        dto.valoracionMedia = media;

        Libro destacado =
            autor.getLibros()
                .stream()
                .filter(l ->
                    l.getNumeroValoraciones() != null
                )
                .max(
                    Comparator.comparing(
                        Libro::getNumeroValoraciones
                    )
                )
                .orElse(null);

        if (destacado != null) {

            LibroShelfDTO top =
                new LibroShelfDTO();

            top.id = destacado.getId();
            top.titulo = destacado.getTitulo();
            top.imagenPortada =
                destacado.getImagenPortada();

            dto.libroDestacado = top;
        }

        return dto;
    }

    @PostMapping("/{id}/seguir")
    public ResponseEntity<?> seguirAutor(
        @PathVariable Long id
    ) {

        Usuario usuario = userContext.getUsuarioActual();

        @SuppressWarnings("null")
        Autor autor = autorRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Autor no encontrado")
            );

        if (
            seguimientoAutorRepository
                .existsByUsuarioAndAutor(
                    usuario,
                    autor
                )
        ) {
            return ResponseEntity.ok().build();
        }

        SeguimientoAutor seguimiento =
            new SeguimientoAutor();

        seguimiento.setUsuario(usuario);
        seguimiento.setAutor(autor);

        seguimientoAutorRepository
            .save(seguimiento);

        return ResponseEntity.ok().build();
    }

    @SuppressWarnings("null")
    @DeleteMapping("/{id}/seguir")
    public ResponseEntity<?> dejarSeguirAutor(
        @PathVariable Long id
    ) {

        Usuario usuario =
            userContext.getUsuarioActual();

        Autor autor =
            autorRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Autor no encontrado"
                    )
                );

        seguimientoAutorRepository
            .deleteByUsuarioAndAutor(
                usuario,
                autor
            );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/siguiendo")
    public Map<String, Boolean> siguiendoAutor(
        @PathVariable Long id
    ) {

        Usuario usuario = userContext.getUsuarioActual();

        boolean siguiendo = usuario
        .getAutoresSeguidos()
        .stream()
        .map(SeguimientoAutor::getAutor)
        .anyMatch(a -> a.getId().equals(id));

        return Map.of(
            "siguiendo",
            siguiendo
        );
    }

    @GetMapping("/seguidos")
    public List<AutorMiniDTO> autoresSeguidos() {

        Usuario usuario =
            userContext.getUsuarioActual();

        return usuario.getAutoresSeguidos()
            .stream()
            .map(seguimiento -> {

                Autor a =
                    seguimiento.getAutor();

                AutorMiniDTO dto =
                    new AutorMiniDTO();

                dto.id = a.getId();
                dto.nombre = a.getNombre();
                dto.fotoUrl = a.getFotoUrl();

                dto.libros =
                    a.getLibros() != null
                    ? a.getLibros().size()
                    : 0;

                return dto;
            })
            .collect(Collectors.toList());
    }

    @GetMapping("/tendencia")
    public List<AutorMiniDTO> tendencia() {

        return autorRepository
            .findTop12ByOrderByNombreAsc()
            .stream()
            .map(a -> {

                AutorMiniDTO dto =
                    new AutorMiniDTO();

                dto.id = a.getId();
                dto.nombre = a.getNombre();
                dto.fotoUrl = a.getFotoUrl();

                dto.libros =
                    a.getLibros() != null
                    ? a.getLibros().size()
                    : 0;

                return dto;

            })
            .toList();
    }

    @GetMapping("/buscar")
    public List<AutorMiniDTO> buscar(
        @RequestParam String q
    ) {

        return autorRepository
            .findByNombreContainingIgnoreCase(q)
            .stream()
            .limit(12)
            .map(a -> {

                AutorMiniDTO dto =
                    new AutorMiniDTO();

                dto.id = a.getId();
                dto.nombre = a.getNombre();
                dto.fotoUrl = a.getFotoUrl();

                dto.libros =
                    a.getLibros() != null
                    ? a.getLibros().size()
                    : 0;

                return dto;

            })
            .toList();
    }

    @GetMapping("/recomendados")
    public List<AutorMiniDTO> recomendados() {

        Usuario usuario =
            userContext.getUsuarioActual();

        Set<String> categoriasFavoritas =
            usuario.getCategoriasFavoritas()
                .stream()
                .map(c -> c.getNombre().toLowerCase())
                .collect(Collectors.toSet());

        Set<Long> autoresSeguidos =
            usuario.getAutoresSeguidos()
                .stream()
                .map(s -> s.getAutor().getId())
                .collect(Collectors.toSet());

        Map<Autor, Integer> score =
            new HashMap<>();

        List<Libro> libros =
            libroRepository.findAll();

        for (Libro libro : libros) {

            boolean coincide =
                libro.getCategorias()
                    .stream()
                    .anyMatch(c ->
                        categoriasFavoritas.contains(
                            c.getNombre().toLowerCase()
                        )
                    );

            if (!coincide) continue;

            for (Autor autor : libro.getAutores()) {

                if (autoresSeguidos.contains(
                    autor.getId()
                )) {
                    continue;
                }

                score.put(
                    autor,
                    score.getOrDefault(autor, 0) + 1
                );
            }
        }

        return score.entrySet()
            .stream()
            .sorted((a, b) ->
                b.getValue()
                .compareTo(a.getValue())
            )
            .limit(12)
            .map(entry -> {

                Autor autor = entry.getKey();

                AutorMiniDTO dto =
                    new AutorMiniDTO();

                dto.id = autor.getId();
                dto.nombre = autor.getNombre();
                dto.fotoUrl = autor.getFotoUrl();

                dto.libros =
                    autor.getLibros() != null
                    ? autor.getLibros().size()
                    : 0;

                return dto;
            })
            .toList();
    }
}