package com.proyecto.booka.repository;

import com.proyecto.booka.model.Deseado;
import com.proyecto.booka.model.DeseadoId;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeseadoRepository extends JpaRepository<Deseado, DeseadoId> {

    List<Deseado> findByUsuario_Id(Long usuarioId);

    List<Deseado> findByLibroId(Long libroId);

}