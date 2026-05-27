package com.proyecto.booka.dto;

public class CategoriaExplorarDTO {

    public String slug;

    public String nombre;

    public boolean favorita;

    public CategoriaExplorarDTO(
        String slug,
        String nombre,
        boolean favorita
    ) {
        this.slug = slug;
        this.nombre = nombre;
        this.favorita = favorita;
    }
}