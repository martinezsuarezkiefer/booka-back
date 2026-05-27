package com.proyecto.bibliotecauji.dto;

import java.time.LocalDateTime;

public class ResenaDTO {

    public Long id;

    public int puntuacion;

    public String comentario;

    public LocalDateTime fecha;

    public Long usuarioId;

    public String usuarioNombre;

    public String usuarioImagen;

    public long likes;

    public boolean likedByMe;
}