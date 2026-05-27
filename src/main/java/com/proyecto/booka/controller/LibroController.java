package com.proyecto.booka.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import com.proyecto.booka.repository.LibroRepository;
import com.proyecto.booka.service.GoogleBooksService;

import java.util.List;
import java.util.Map;

import com.proyecto.booka.model.Libro;


@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private GoogleBooksService googleBooksService;

    // Importar libros de la api de Google Books
   @PostMapping("/importar")
    public ResponseEntity<Map<String, Object>> importarLibros(@RequestParam String tema, @RequestParam(defaultValue = "200") int maxLibros, @RequestParam(defaultValue = "40") int maxPorPagina) {
        Map<String, Object> resultado = googleBooksService.importarLibrosPorTema(tema, maxLibros, maxPorPagina);
        return ResponseEntity.ok(resultado);
    }

    // Obtener todos los libros
    @GetMapping
    public ResponseEntity<?> getLibrosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Libro> result = libroRepository.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    // Buscar libro por ID interno (Long)
    @GetMapping("/{id}")
    public ResponseEntity<Libro> getLibroById(@PathVariable @NonNull Long id) {
        Optional<Libro> libro = libroRepository.findById(id);
        return libro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar libro por ID de Google Books (String)
    @GetMapping("/google/{googleId}")
    public ResponseEntity<Libro> getLibroByGoogleBooksId(@PathVariable String googleId) {
        Optional<Libro> libro = libroRepository.findByGoogleBooksId(googleId);
        return libro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Buscar libros por título
    @GetMapping("/buscar")
    public List<Libro> searchLibrosByTitulo(@RequestParam String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    // Crear nuevo libro
    @PostMapping
    public Libro createLibro(@RequestBody @NonNull Libro libro) {
        return libroRepository.save(libro);
    }

    // Actualizar libro existente
    @PutMapping("/{id}")
    public ResponseEntity<Libro> updateLibro(@PathVariable @NonNull Long id, @RequestBody Libro detallesLibro) {
        Optional<Libro> optionalLibro = libroRepository.findById(id);
        if (optionalLibro.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Libro libro = optionalLibro.get();
        libro.setTitulo(detallesLibro.getTitulo());
        libro.setSubtitulo(detallesLibro.getSubtitulo());
        libro.setDescripcion(detallesLibro.getDescripcion());
        libro.setEditorial(detallesLibro.getEditorial());
        libro.setFechaPublicacion(detallesLibro.getFechaPublicacion());
        libro.setNumeroPaginas(detallesLibro.getNumeroPaginas());
        libro.setIdioma(detallesLibro.getIdioma());

        return ResponseEntity.ok(libroRepository.save(libro));
    }

    // Eliminar libro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable @NonNull Long id) {
        if (!libroRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        libroRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
