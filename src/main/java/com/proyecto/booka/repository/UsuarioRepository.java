package com.proyecto.booka.repository;

import com.proyecto.booka.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("""
    SELECT u
    FROM Usuario u
    LEFT JOIN FETCH u.seguidos
    WHERE u.id = :id
    """)
    Optional<Usuario> findByIdWithSeguidos(
        @Param("id") Long id
    );

    // Buscar usuario por email
    Optional<Usuario> findByEmail(String email);
    
    // Comprobar si un email ya existe
    boolean existsByEmail(String email);

    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

}
