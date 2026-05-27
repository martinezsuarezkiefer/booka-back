package com.proyecto.bibliotecauji.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "perfil_comentarios")
public class PerfilComentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_perfil_id", nullable = false)
    private Usuario usuarioPerfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_autor_id", nullable = false)
    private Usuario usuarioAutor;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Column
    private LocalDateTime fecha = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuarioPerfil() {
        return usuarioPerfil;
    }

    public void setUsuarioPerfil(Usuario usuarioPerfil) {
        this.usuarioPerfil = usuarioPerfil;
    }

    public Usuario getUsuarioAutor() {
        return usuarioAutor;
    }

    public void setUsuarioAutor(Usuario usuarioAutor) {
        this.usuarioAutor = usuarioAutor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    // getters y setters
    
}
