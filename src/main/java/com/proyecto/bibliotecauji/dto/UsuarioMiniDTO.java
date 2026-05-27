package com.proyecto.bibliotecauji.dto;

public class UsuarioMiniDTO {

    private Long id;
    private String nombre;
    private String profileImage;

    public UsuarioMiniDTO() {}

    public UsuarioMiniDTO(Long id, String nombre, String profileImage) {
        this.id = id;
        this.nombre = nombre;
        this.profileImage = profileImage;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}