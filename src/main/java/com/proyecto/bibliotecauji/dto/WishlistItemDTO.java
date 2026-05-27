package com.proyecto.bibliotecauji.dto;

import java.time.LocalDateTime;

public class WishlistItemDTO {

    private Long id;
    private String titulo;
    private String imagenPortada;
    private LocalDateTime fechaAgregado;

    public WishlistItemDTO() {}
    public WishlistItemDTO(Long id, String titulo, String imagenPortada, LocalDateTime fechaAgregado) {
        this.id = id;
        this.titulo = titulo;
        this.imagenPortada = imagenPortada;
        this.fechaAgregado = fechaAgregado;
    }

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getImagenPortada() { return imagenPortada; }
    public void setImagenPortada(String imagenPortada) { this.imagenPortada = imagenPortada; }
    
    public LocalDateTime getFechaAgregado() { return fechaAgregado; }
    public void setFechaAgregado(LocalDateTime fechaAgregado) { this.fechaAgregado = fechaAgregado; }

}
