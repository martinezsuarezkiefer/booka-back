package com.proyecto.booka.dto;

public class ColeccionDTO {

    public Long id;
    public String titulo;
    public String imagenPortada;
    public String estado;
    public String fechaAgregado;

    public ColeccionDTO(Long id, String titulo, String imagenPortada,
                        String estado, String fechaAgregado) {
        this.id = id;
        this.titulo = titulo;
        this.imagenPortada = imagenPortada;
        this.estado = estado;
        this.fechaAgregado = fechaAgregado;
    }

}
