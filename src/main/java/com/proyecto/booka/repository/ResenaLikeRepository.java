package com.proyecto.booka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.booka.model.Resena;
import com.proyecto.booka.model.ResenaLike;
import com.proyecto.booka.model.Usuario;

public interface ResenaLikeRepository
    extends JpaRepository<ResenaLike, Long> {

    Optional<ResenaLike> findByUsuarioAndResena(
        Usuario usuario,
        Resena resena
    );

    long countByResena(Resena resena);
}