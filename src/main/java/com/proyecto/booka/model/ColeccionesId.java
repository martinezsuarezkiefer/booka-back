package com.proyecto.booka.model;

import java.util.Objects;
import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class ColeccionesId implements Serializable {

    private Long usuarioId;
    private Long libroId;

    public ColeccionesId() {}

    public ColeccionesId(Long usuarioId, Long libroId) {
        this.usuarioId = usuarioId;
        this.libroId = libroId;
    }

    // Getters, setters, equals, hashCode

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getLibroId() { return libroId; }
    public void setLibroId(Long libroId) { this.libroId = libroId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ColeccionesId that)) return false;
        return Objects.equals(usuarioId, that.usuarioId) &&
               Objects.equals(libroId, that.libroId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, libroId);
    }

}
