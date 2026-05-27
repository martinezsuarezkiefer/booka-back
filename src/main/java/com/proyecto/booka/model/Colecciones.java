package com.proyecto.booka.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "colecciones")
public class Colecciones implements Serializable {

    @EmbeddedId
    private ColeccionesId id = new ColeccionesId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("libroId")
    @JoinColumn(name = "libro_id")
    @JsonIgnore
    private Libro libro;

    @Column(name = "fecha_agregado", nullable = false)
    private LocalDate fechaAgregado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_lectura", length = 20)
    private EstadoLectura estadoLectura = EstadoLectura.PENDIENTE;

    @Column(name = "comentario_personal", length = 500)
    private String comentarioPersonal;

    

    public ColeccionesId getId() {
        return id;
    }



    public void setId(ColeccionesId id) {
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



    public LocalDate getFechaAgregado() {
        return fechaAgregado;
    }



    public void setFechaAgregado(LocalDate fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }



    public EstadoLectura getEstadoLectura() {
        return estadoLectura;
    }



    public void setEstadoLectura(EstadoLectura estadoLectura) {
        this.estadoLectura = estadoLectura;
    }



    public String getComentarioPersonal() {
        return comentarioPersonal;
    }



    public void setComentarioPersonal(String comentarioPersonal) {
        this.comentarioPersonal = comentarioPersonal;
    }



    @PrePersist
    public void prePersist() {
        if (fechaAgregado == null)
            fechaAgregado = LocalDate.now();

        if (estadoLectura == null)
            estadoLectura = EstadoLectura.PENDIENTE;
    }
}

