package com.proyecto.bibliotecauji.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.bibliotecauji.model.Categoria;
import java.util.Optional;
import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findTop20ByNombreContainingIgnoreCase(String q);

    Optional<Categoria> findFirstByNombreIgnoreCase(String nombre);
    Optional<Categoria> findFirstByNombreContainingIgnoreCase(String nombre);
}
