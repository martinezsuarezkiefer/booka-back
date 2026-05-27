package com.proyecto.bibliotecauji.service;
import java.util.*;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.bibliotecauji.dto.ActividadItemDTO;
import com.proyecto.bibliotecauji.dto.CategoriaDTO;
import com.proyecto.bibliotecauji.dto.ComentarioPerfilDTO;
import com.proyecto.bibliotecauji.dto.LibroDTO;
import com.proyecto.bibliotecauji.dto.PerfilUsuarioDTO;
import com.proyecto.bibliotecauji.model.Colecciones;
import com.proyecto.bibliotecauji.model.Deseado;
import com.proyecto.bibliotecauji.model.PerfilComentario;
import com.proyecto.bibliotecauji.model.Usuario;
import com.proyecto.bibliotecauji.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PerfilService {

    @Autowired private UsuarioRepository usuarioRepo;

    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public PerfilUsuarioDTO getPerfil(Long id) {

        Usuario u = usuarioRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PerfilUsuarioDTO dto = new PerfilUsuarioDTO();
        dto.id = u.getId();
        dto.nombre = u.getNombre();
        dto.profileImage = u.getProfileImage();
        dto.fechaRegistro = u.getFechaRegistro();

        dto.numResenas = u.getResenas().size();
        dto.numLibrosColeccion = u.getLibrosColeccion().size();
        dto.numWish = u.getListaDeseados().size();
        dto.seguidores = u.getSeguidores().size();
        dto.siguiendo = u.getSeguidos().size();

        dto.categoriasFavoritas = u.getCategoriasFavoritas().stream()
            .map(c -> {
                CategoriaDTO cDto = new CategoriaDTO();
                cDto.id = c.getId();
                cDto.nombre = c.getNombre();
                return cDto;
            })
            .toList();

        dto.miniColeccion = u.getLibrosColeccion().stream()
            .sorted(Comparator.comparing(Colecciones::getFechaAgregado).reversed())
            .limit(6)
            .map(c -> {
                LibroDTO l = new LibroDTO();
                l.id = c.getLibro().getId();
                l.titulo = c.getLibro().getTitulo();
                l.imagenPortada = c.getLibro().getImagenPortada();
                return l;
            })
            .toList();

        dto.miniWishlist = u.getListaDeseados().stream()
            .sorted(Comparator.comparing(Deseado::getFechaAgregado).reversed())
            .limit(6)
            .map(d -> {
                LibroDTO l = new LibroDTO();
                l.id = d.getLibro().getId();
                l.titulo = d.getLibro().getTitulo();
                l.imagenPortada = d.getLibro().getImagenPortada();
                return l;
            })
            .toList();

        dto.actividad = Stream.concat(
                u.getLibrosColeccion().stream()
                    .map(c -> {
                        ActividadItemDTO a = new ActividadItemDTO();
                        a.tipo = "COLECCION";
                        a.fecha = c.getFechaAgregado().atStartOfDay();
                        a.libro = new LibroDTO();
                        a.libro.id = c.getLibro().getId();
                        a.libro.titulo = c.getLibro().getTitulo();
                        a.libro.imagenPortada = c.getLibro().getImagenPortada();
                        return a;
                    }),
                u.getListaDeseados().stream()
                    .map(d -> {
                        ActividadItemDTO a = new ActividadItemDTO();
                        a.tipo = "WISHLIST";
                        a.fecha = d.getFechaAgregado();
                        a.libro = new LibroDTO();
                        a.libro.id = d.getLibro().getId();
                        a.libro.titulo = d.getLibro().getTitulo();
                        a.libro.imagenPortada = d.getLibro().getImagenPortada();
                        return a;
                    })
        )
        .sorted(Comparator.comparing((ActividadItemDTO a) -> a.fecha).reversed())
        .limit(15)
        .toList();

        dto.comentarios = u.getComentariosRecibidos().stream()
            .sorted(
                Comparator.comparing(
                    PerfilComentario::getFecha
                ).reversed()
            )
            .map(c -> {

                ComentarioPerfilDTO cm =
                    new ComentarioPerfilDTO();

                cm.autorId =
                    c.getUsuarioAutor().getId();

                cm.autorNombre =
                    c.getUsuarioAutor().getNombre();

                cm.autorImagen =
                    c.getUsuarioAutor()
                        .getProfileImage();

                cm.mensaje =
                    c.getMensaje();

                cm.fecha =
                    c.getFecha();

                return cm;
            })
            .toList();

        return dto;
    }
}
