package com.proyecto.booka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.booka.model.Libro;
import com.proyecto.booka.model.Resena;
import com.proyecto.booka.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface ResenaRepository extends JpaRepository<Resena, Long> {

    List<Resena> findByLibro(Libro libro);

    List<Resena> findByUsuario(Usuario usuario);

    Optional<Resena> findByUsuarioAndLibro(Usuario usuario, Libro libro);
}
