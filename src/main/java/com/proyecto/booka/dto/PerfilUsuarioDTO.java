package com.proyecto.booka.dto;
import java.time.LocalDateTime;
import java.util.List;

public class PerfilUsuarioDTO {

    public Long id;
    public String nombre;
    public String profileImage;
    public LocalDateTime fechaRegistro;

    // Resumen
    public int numResenas;
    public int numLibrosColeccion;
    public int numWish;
    public int siguiendo;
    public int seguidores;

    // Categorías favoritas
    public List<CategoriaDTO> categoriasFavoritas;

    // Mini vistas
    public List<LibroDTO> miniColeccion;
    public List<LibroDTO> miniWishlist;

    // Actividad
    public List<ActividadItemDTO> actividad;

    // Comentarios recibidos
    public List<ComentarioPerfilDTO> comentarios;
}
