package com.proyecto.bibliotecauji.repository;

import com.proyecto.bibliotecauji.model.Deseado;
import com.proyecto.bibliotecauji.model.DeseadoId;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeseadoRepository extends JpaRepository<Deseado, DeseadoId> {

    List<Deseado> findByUsuario_Id(Long usuarioId);

    List<Deseado> findByLibroId(Long libroId);

}