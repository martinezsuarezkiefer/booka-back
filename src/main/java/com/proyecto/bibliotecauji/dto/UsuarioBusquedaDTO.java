package com.proyecto.bibliotecauji.dto;

public class UsuarioBusquedaDTO {

    private Long id;
    private String nombre;
    private String profileImage;

    private int libros;
    private int seguidores;
    private int siguiendo;

    public UsuarioBusquedaDTO() {}

    public UsuarioBusquedaDTO(
        Long id,
        String nombre,
        String profileImage,
        int libros,
        int seguidores,
        int siguiendo
    ) {
        this.id = id;
        this.nombre = nombre;
        this.profileImage = profileImage;
        this.libros = libros;
        this.seguidores = seguidores;
        this.siguiendo = siguiendo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public int getLibros() { return libros; }
    public void setLibros(int libros) { this.libros = libros; }

    public int getSeguidores() { return seguidores; }
    public void setSeguidores(int seguidores) { this.seguidores = seguidores; }

    public int getSiguiendo() { return siguiendo; }
    public void setSiguiendo(int siguiendo) { this.siguiendo = siguiendo; }
}