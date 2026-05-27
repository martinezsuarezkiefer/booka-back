package com.proyecto.booka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.booka.model.Autor;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findFirstByNombreContainingIgnoreCase(String nombre);
    List<Autor> findByNombreContainingIgnoreCase(String nombre);
    List<Autor> findTop20ByNombreContainingIgnoreCase(String nombre);
    List<Autor> findTop12ByOrderByNombreAsc();
}
