package com.proyecto.booka.dto;

import java.time.LocalDateTime;

public class ReviewCardDTO {

    public Long id;

    public Long libroId;

    public String libroTitulo;

    public String libroImagen;

    public Integer puntuacion;

    public String comentario;

    public long likes;

    public LocalDateTime fecha;

    public Long usuarioId;

    public String usuarioNombre;

    public String usuarioImagen;

    public boolean likedByMe;
}