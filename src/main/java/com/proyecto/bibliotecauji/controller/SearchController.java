package com.proyecto.bibliotecauji.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.proyecto.bibliotecauji.repository.*;
import com.proyecto.bibliotecauji.model.*;
import com.proyecto.bibliotecauji.dto.UsuarioBusquedaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

@RestController
@RequestMapping("/buscar")
public class SearchController {

    @Autowired private LibroRepository libroRepository;
    @Autowired private AutorRepository autorRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    @GetMapping
    public Map<String, Object> buscar(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        // Libros paginados
        Page<Libro> librosPage = libroRepository.findByTituloContainingIgnoreCase(q, pageable);

        // Autores
        List<Autor> autores = autorRepository.findByNombreContainingIgnoreCase(q);

        // Usuarios
        List<UsuarioBusquedaDTO> usuarios = usuarioRepository
        .findByNombreContainingIgnoreCase(q)
        .stream()
        .map(u -> new UsuarioBusquedaDTO(
            u.getId(),
            u.getNombre(),
            u.getProfileImage(),
            u.getLibrosColeccion().size(),
            u.getSeguidores().size(),
            u.getSeguidos().size()
        ))
        .toList();

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("libros", librosPage);
        respuesta.put("autores", autores);
        respuesta.put("usuarios", usuarios);

        return respuesta;
    }
}
