package com.proyecto.bibliotecauji.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "resenas_likes",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"usuario_id", "resena_id"}
    )
)
public class ResenaLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resena_id")
    private Resena resena;

    private LocalDateTime fecha =
        LocalDateTime.now();

    // getters setters

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Resena getResena() {
        return resena;
    }

    public void setResena(Resena resena) {
        this.resena = resena;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}