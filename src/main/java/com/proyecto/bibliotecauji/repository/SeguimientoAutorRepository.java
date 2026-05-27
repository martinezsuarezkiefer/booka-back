package com.proyecto.bibliotecauji.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.bibliotecauji.model.Autor;
import com.proyecto.bibliotecauji.model.SeguimientoAutor;
import com.proyecto.bibliotecauji.model.Usuario;

public interface SeguimientoAutorRepository
    extends JpaRepository<SeguimientoAutor, Long> {

    List<SeguimientoAutor> findByUsuario(
        Usuario usuario
    );

    boolean existsByUsuarioAndAutor(
        Usuario usuario,
        Autor autor
    );

    void deleteByUsuarioAndAutor(
        Usuario usuario,
        Autor autor
    );

    long countByAutor(
        Autor autor
    );
}