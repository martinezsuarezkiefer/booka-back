package com.proyecto.bibliotecauji.dto;

import java.util.List;

public class AutorDetailDTO {

    public Long id;

    public String nombre;

    public long seguidores;

    public boolean seguido;

    public List<LibroShelfDTO> libros;

    public List<String> categoriasPrincipales;

    public Double valoracionMedia;

    public LibroShelfDTO libroDestacado;

    public String fotoUrl;

    public String biografia;
}