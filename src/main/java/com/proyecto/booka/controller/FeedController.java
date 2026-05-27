package com.proyecto.booka.controller;

import java.util.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.booka.model.*;
import com.proyecto.booka.repository.*;
import com.proyecto.booka.security.UserContextService;
import com.proyecto.booka.service.FeedService;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final UserContextService userContext;
    private final LibroRepository libroRepository;
    private final FeedService feedService;

    public FeedController(
        UserContextService userContext,
        LibroRepository libroRepository,
        FeedService feedService
    ) {
        this.userContext = userContext;
        this.libroRepository = libroRepository;
        this.feedService = feedService;
    }

    @GetMapping
    public ResponseEntity<?> getFeed() {

        try {

            return ResponseEntity.ok(
                feedService.getFeed()
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.badRequest()
                .body(e.getMessage());
        }
    }

    @GetMapping("/recomendados")
    public List<Libro> recomendados() {

        Usuario usuario = userContext.getUsuarioActual();

        Set<Categoria> favoritas =
            usuario.getCategoriasFavoritas();

        if (favoritas.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Libro> recomendaciones =
            new LinkedHashSet<>();

        favoritas.forEach(cat -> {

            recomendaciones.addAll(
                libroRepository.recomendarPorCategoria(
                    cat.getNombre(),
                    PageRequest.of(0, 10)
                )
            );
        });

        return recomendaciones.stream()
            .limit(20)
            .toList();
    }

}