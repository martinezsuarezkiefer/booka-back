package com.proyecto.bibliotecauji.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.bibliotecauji.model.Libro;
import com.proyecto.bibliotecauji.model.Resena;
import com.proyecto.bibliotecauji.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface ResenaRepository extends JpaRepository<Resena, Long> {

    List<Resena> findByLibro(Libro libro);

    List<Resena> findByUsuario(Usuario usuario);

    Optional<Resena> findByUsuarioAndLibro(Usuario usuario, Libro libro);
}
