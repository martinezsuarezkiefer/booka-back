package com.proyecto.booka.dto;
import java.time.LocalDateTime;

public class ActividadItemDTO {
    public String tipo;  // COLECCION | WISHLIST
    public LibroDTO libro;
    public LocalDateTime fecha;
}