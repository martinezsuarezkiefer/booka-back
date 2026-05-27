package com.proyecto.bibliotecauji.service;

import com.proyecto.bibliotecauji.dto.FeedItemDTO;
import com.proyecto.bibliotecauji.model.Colecciones;
import com.proyecto.bibliotecauji.model.Deseado;
import com.proyecto.bibliotecauji.model.Usuario;
import com.proyecto.bibliotecauji.model.Resena;
import com.proyecto.bibliotecauji.model.SeguimientoAutor;
import com.proyecto.bibliotecauji.repository.ColeccionesRepository;
import com.proyecto.bibliotecauji.repository.DeseadoRepository;
import com.proyecto.bibliotecauji.repository.ResenaLikeRepository;
import com.proyecto.bibliotecauji.repository.ResenaRepository;
import com.proyecto.bibliotecauji.repository.SeguimientoAutorRepository;
import com.proyecto.bibliotecauji.repository.UsuarioRepository;
import com.proyecto.bibliotecauji.security.UserContextService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FeedService {

    private final UserContextService userContext;
    private final ColeccionesRepository coleccionesRepository;
    private final DeseadoRepository deseadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ResenaRepository resenaRepository;
    private final ResenaLikeRepository resenaLikeRepository;
    private final SeguimientoAutorRepository seguimientoAutorRepository;

    public FeedService(
        UserContextService userContext,
        ColeccionesRepository coleccionesRepository,
        DeseadoRepository deseadoRepository,
        UsuarioRepository usuarioRepository,
        ResenaRepository resenaRepository,
        ResenaLikeRepository resenaLikeRepository,
        SeguimientoAutorRepository seguimientoAutorRepository
    ) {
        this.userContext = userContext;
        this.coleccionesRepository = coleccionesRepository;
        this.deseadoRepository = deseadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.resenaRepository = resenaRepository;
        this.resenaLikeRepository = resenaLikeRepository;
        this.seguimientoAutorRepository = seguimientoAutorRepository;
    }

    @Transactional(readOnly = true)
    public List<FeedItemDTO> getFeed() {

        Usuario auth = userContext.getUsuarioActual();

        Usuario actual = usuarioRepository
            .findByIdWithSeguidos(auth.getId())
            .orElseThrow();

        List<FeedItemDTO> items = new ArrayList<>();

        for (Usuario seguido : actual.getSeguidos()) {

            Long id = seguido.getId();

            // COLECCION
            List<Colecciones> colecciones =
                coleccionesRepository.findByUsuarioId(id);

            for (Colecciones c : colecciones) {

                FeedItemDTO dto = new FeedItemDTO();

                dto.tipo = "COLECCION";
                dto.usuarioId = seguido.getId();
                dto.usuarioNombre = seguido.getNombre();
                dto.usuarioImagen = seguido.getProfileImage();

                dto.libroId = c.getLibro().getId();
                dto.libroTitulo = c.getLibro().getTitulo();
                dto.libroImagen = c.getLibro().getImagenPortada();

                switch (c.getEstadoLectura()) {

                    case PENDIENTE ->
                        dto.texto = "quiere leer";

                    case LEYENDO ->
                        dto.texto = "empezó a leer";

                    case LEIDO ->
                        dto.texto = "terminó de leer";

                    default ->
                        dto.texto = "actualizó su biblioteca";
                }

                dto.fecha = c.getFechaAgregado()
                    .atStartOfDay();

                items.add(dto);
            }

            // WISHLIST
            List<Deseado> deseados =
                deseadoRepository.findByUsuario_Id(id);

            for (Deseado d : deseados) {

                FeedItemDTO dto = new FeedItemDTO();

                dto.tipo = "WISHLIST";
                dto.usuarioId = seguido.getId();
                dto.usuarioNombre = seguido.getNombre();
                dto.usuarioImagen = seguido.getProfileImage();

                dto.libroId = d.getLibro().getId();
                dto.libroTitulo = d.getLibro().getTitulo();
                dto.libroImagen = d.getLibro().getImagenPortada();

                dto.texto = "añadió a wishlist";

                dto.fecha = d.getFechaAgregado();

                items.add(dto);
            }

            // RESEÑAS
            List<Resena> resenas =
                resenaRepository.findByUsuario(seguido);

            for (Resena r : resenas) {

                FeedItemDTO dto = new FeedItemDTO();

                dto.tipo = "RESENA";
                dto.resenaId = r.getId();
                dto.likes =
                    resenaLikeRepository.countByResena(r);

                dto.likedByMe =
                    resenaLikeRepository
                        .findByUsuarioAndResena(actual, r)
                        .isPresent();

                dto.usuarioId = seguido.getId();
                dto.usuarioNombre = seguido.getNombre();
                dto.usuarioImagen = seguido.getProfileImage();

                dto.libroId = r.getLibro().getId();
                dto.libroTitulo = r.getLibro().getTitulo();
                dto.libroImagen = r.getLibro().getImagenPortada();

                dto.puntuacion = r.getPuntuacion();

                dto.comentario = r.getComentario();


                dto.texto =
                    "valoró con "
                    + r.getPuntuacion()
                    + " estrellas";

                dto.fecha = r.getFecha();

                items.add(dto);
            }

            // AUTORES
            List<SeguimientoAutor> autoresSeguidos =
                seguimientoAutorRepository
                    .findByUsuario(seguido);

            for (SeguimientoAutor s : autoresSeguidos) {

                FeedItemDTO dto =
                    new FeedItemDTO();

                dto.tipo = "AUTOR";

                dto.usuarioId =
                    seguido.getId();

                dto.usuarioNombre =
                    seguido.getNombre();

                dto.usuarioImagen =
                    seguido.getProfileImage();

                dto.autorId =
                    s.getAutor().getId();

                dto.autorNombre =
                    s.getAutor().getNombre();

                dto.autorFoto =
                    s.getAutor().getFotoUrl();

                dto.texto =
                    "empezó a seguir a";

                dto.fecha =
                    s.getFecha();

                items.add(dto);
            }
        }

        items.sort((a, b) -> b.fecha.compareTo(a.fecha));

        return items.stream()
            .limit(30)
            .toList();
    }
}