package com.proyecto.bibliotecauji.model;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(length = 500)
    private String fotoUrl;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    private String pais;

    @ManyToMany(mappedBy = "autores")
    @JsonBackReference
    private Set<Libro> libros = new HashSet<>();

    @OneToMany(mappedBy = "autor")
    @JsonIgnore
    private Set<SeguimientoAutor> seguidores =
        new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Set<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
    }

    //Constructores
    public Autor(String nombre) {
        this.nombre = nombre;
    }

    public Autor() {}
}
