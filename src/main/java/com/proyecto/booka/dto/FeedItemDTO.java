package com.proyecto.booka.dto;

import java.time.LocalDateTime;

public class FeedItemDTO {

    public String tipo;

    public Long usuarioId;
    public String usuarioNombre;
    public String usuarioImagen;
    public Long libroId;
    public String libroTitulo;
    public String libroImagen;
    public String texto;
    public Integer puntuacion;
    public String comentario;
    public LocalDateTime fecha;
    public Long likes;
    public Boolean likedByMe;
    public Long resenaId;
    public Long autorId;
    public String autorNombre;
    public String autorFoto;
}