package com.proyecto.bibliotecauji.repository;

import com.proyecto.bibliotecauji.model.Colecciones;
import com.proyecto.bibliotecauji.model.ColeccionesId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ColeccionesRepository extends JpaRepository<Colecciones, ColeccionesId> {

    List<Colecciones> findByUsuarioId(Long usuarioId);
    List<Colecciones> findByLibroId(Long libroId);

}
