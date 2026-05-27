package com.proyecto.bibliotecauji.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.bibliotecauji.model.Resena;
import com.proyecto.bibliotecauji.model.ResenaLike;
import com.proyecto.bibliotecauji.model.Usuario;

public interface ResenaLikeRepository
    extends JpaRepository<ResenaLike, Long> {

    Optional<ResenaLike> findByUsuarioAndResena(
        Usuario usuario,
        Resena resena
    );

    long countByResena(Resena resena);
}