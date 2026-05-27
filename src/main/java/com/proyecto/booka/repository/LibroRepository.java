package com.proyecto.booka.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto.booka.model.Libro;
import java.util.Optional;
import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("""
    SELECT DISTINCT l
    FROM Libro l
    JOIN l.categorias c
    WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :tema, '%'))
    """)
    Page<Libro> findByCategoriaTema(
        @Param("tema") String tema,
        Pageable pageable
    );

    @Query("""
    SELECT DISTINCT l
    FROM Libro l
    JOIN l.categorias c
    WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :categoria, '%'))
    ORDER BY l.numeroValoraciones DESC
    """)
    List<Libro> recomendarPorCategoria(
        @Param("categoria") String categoria,
        Pageable pageable
    );

    Optional<Libro> findByGoogleBooksId(String googleBooksId);

    List<Libro> findByTituloContainingIgnoreCase(String titulo);
    Page<Libro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);


    List<Libro> findByIdioma(String idioma);

    boolean existsByGoogleBooksId(String googleBooksId);
    
}
