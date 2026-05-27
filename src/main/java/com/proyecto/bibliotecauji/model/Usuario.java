package com.proyecto.bibliotecauji.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "profile_image", length = 300)
    private String profileImage;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(nullable = false)
    private String rol = "ROLE_USER";

    // --- Relaciones ---

    // Biblioteca personal
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Colecciones> librosColeccion = new HashSet<>();


    // Libros deseados
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Deseado> listaDeseados = new HashSet<>();


    // Reseñas realizadas por el usuario
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "usuario-resenas")
    private Set<Resena> resenas = new HashSet<>();

    // Seguimientos de otros usuarios
    @ManyToMany
    @JoinTable(
        name = "seguimientos_usuarios",
        joinColumns = @JoinColumn(name = "seguidor_id"),
        inverseJoinColumns = @JoinColumn(name = "seguido_id")
    )
    @JsonIgnore
    private Set<Usuario> seguidos = new HashSet<>();

    // Seguidores del usuario
    @ManyToMany(mappedBy = "seguidos")
    @JsonIgnore
    private Set<Usuario> seguidores = new HashSet<>();

    // Autores seguidos
    @OneToMany(
        mappedBy = "usuario",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnore
    private Set<SeguimientoAutor> autoresSeguidos =
        new HashSet<>();


    // Categorías favoritas
    @ManyToMany
    @JoinTable(
        name = "usuarios_categorias_favoritas",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categoriasFavoritas = new HashSet<>();

    // Comentarios en perfil
    @OneToMany(mappedBy = "usuarioPerfil", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PerfilComentario> comentariosRecibidos = new HashSet<>();

    // --- Getters y setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public String getRol() { return rol;}
    public void setRol(String rol) { this.rol = rol; }

    public Set<Colecciones> getLibrosColeccion() { return librosColeccion; }
    public void setLibrosColeccion(Set<Colecciones> librosColeccion) { this.librosColeccion = librosColeccion; }

    public Set<Deseado> getListaDeseados() { return listaDeseados; }
    public void setListaDeseados(Set<Deseado> listaDeseados) { this.listaDeseados = listaDeseados;}

    public Set<Resena> getResenas() { return resenas; }
    public void setResenas(Set<Resena> resenas) { this.resenas = resenas; }
    
    public Set<Usuario> getSeguidos() { return seguidos; }
    public void setSeguidos(Set<Usuario> seguidos) { this.seguidos = seguidos; }
    
    public Set<Usuario> getSeguidores() { return seguidores; }
    public void setSeguidores(Set<Usuario> seguidores) { this.seguidores = seguidores; }
    
    public Set<SeguimientoAutor> getAutoresSeguidos() {return autoresSeguidos; }
    public void setAutoresSeguidos(Set<SeguimientoAutor> autoresSeguidos) { this.autoresSeguidos = autoresSeguidos; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public Set<Categoria> getCategoriasFavoritas() { return categoriasFavoritas; }
    public void setCategoriasFavoritas(Set<Categoria> categoriasFavoritas) { this.categoriasFavoritas = categoriasFavoritas; }
    
    public Set<PerfilComentario> getComentariosRecibidos() { return comentariosRecibidos; }
    public void setComentariosRecibidos(Set<PerfilComentario> comentariosRecibidos) { this.comentariosRecibidos = comentariosRecibidos; }

    @PrePersist
    public void prePersist() {
         if (rol == null) rol = "ROLE_USER";
    }

}
