package com.proyecto.bibliotecauji.model;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DeseadoId implements Serializable {

    private Long usuarioId;
    private Long libroId;

    public DeseadoId() {}

    public DeseadoId(Long usuarioId, Long libroId) {
        this.usuarioId = usuarioId;
        this.libroId = libroId;
    }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getLibroId() { return libroId; }
    public void setLibroId(Long libroId) { this.libroId = libroId; }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, libroId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeseadoId other)) return false;
        return Objects.equals(usuarioId, other.usuarioId)
            && Objects.equals(libroId, other.libroId);
    }
}
