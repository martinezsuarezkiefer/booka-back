package com.proyecto.booka.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deseados")
public class Deseado {

    @EmbeddedId
    private DeseadoId id = new DeseadoId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("libroId")
    @JoinColumn(name = "libro_id")
    private Libro libro;

    @Column(name = "fecha_agregado", nullable = false)
    private LocalDateTime fechaAgregado;

    @PrePersist
    public void onCreate() {
        if (fechaAgregado == null) fechaAgregado = LocalDateTime.now();
    }

    // getters y setters
    public DeseadoId getId() {
        return id;
    }

    public void setId(DeseadoId id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public LocalDateTime getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(LocalDateTime fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

}
