package com.proyecto.booka.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "google_books_id", unique = true)
    private String googleBooksId;

    @Column(length = 500)
    private String titulo;
    @Column(length = 500)
    private String subtitulo;

    @Column(columnDefinition = "TEXT", length = 2000)
    private String descripcion;

    private String editorial;

    @Column(name = "fecha_publicacion")
    private String fechaPublicacion;

    @Column(name = "numero_paginas")
    private Integer numeroPaginas;

    @Column(name = "isbn_10")
    private String isbn10;

    @Column(name = "isbn_13")
    private String isbn13;

    @Column(name = "imagen_portada", length = 2000)
    private String imagenPortada;

    @Column(name = "valoracion_media")
    private Double valoracionMedia;

    @Column(name = "numero_valoraciones")
    private Integer numeroValoraciones;

    private String idioma;

    // Relación con autores
    @ManyToMany
    @JoinTable(
        name = "libro_autor",
        joinColumns = @JoinColumn(name = "libro_id"),
        inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    @JsonManagedReference
    private Set<Autor> autores = new HashSet<>();

    // Relación con categorías
    @ManyToMany
    @JoinTable(
        name = "libro_categoria",
        joinColumns = @JoinColumn(name = "libro_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();

    // Reseñas asociadas al libro
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resena> resenas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoogleBooksId() {
        return googleBooksId;
    }

    public void setGoogleBooksId(String googleBooksId) {
        this.googleBooksId = googleBooksId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Integer getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(Integer numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(String imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public Double getValoracionMedia() {
        return valoracionMedia;
    }

    public void setValoracionMedia(Double valoracionMedia) {
        this.valoracionMedia = valoracionMedia;
    }

    public Integer getNumeroValoraciones() {
        return numeroValoraciones;
    }

    public void setNumeroValoraciones(Integer numeroValoraciones) {
        this.numeroValoraciones = numeroValoraciones;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Set<Resena> getResenas() { 
        return resenas; 
    }
    
    public void setResenas(Set<Resena> resenas) { 
        this.resenas = resenas; 
    }

}
