package com.proyecto.booka.service;

import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.booka.dto.GoogleBooksResponse;
import com.proyecto.booka.dto.Volume;
import com.proyecto.booka.dto.VolumeInfo;
import com.proyecto.booka.model.*;
import com.proyecto.booka.repository.*;

@Service
public class GoogleBooksService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Importa libros desde la API de Google Books con paginación.
     * 
     * @param consulta      Tema o palabra clave a buscar.
     * @param maxLibros     Número máximo total de libros a importar.
     * @param maxPorPagina  Número máximo por página (máximo permitido: 40).
     */
    @Transactional
    @SuppressWarnings("null")
    public Map<String, Object> importarLibrosPorTema(String consulta, int maxLibros, int maxPorPagina) {
        int importados = 0;
        int yaExistian = 0;
        int startIndex = 0;
        int yearMin = 2015;
        Set<String> idsVistos = new HashSet<>();

        System.out.println("=== Iniciando importación para tema: " + consulta + " ===");

        while (importados < maxLibros) {
            String queryEncoded =
            URLEncoder.encode(
                consulta,
                StandardCharsets.UTF_8
            );

            String order =
            startIndex > 80
            ? "newest"
            : "relevance";
            
            String url = String.format(

                "https://www.googleapis.com/books/v1/volumes?q=%s&maxResults=%d&startIndex=%d&orderBy=%s&key=AIzaSyCIvwkRQzOmGL4Ijy0uC_r0co9l-k090XY",

                queryEncoded,
                maxPorPagina,
                startIndex,
                order
            );

            System.out.println(url);

            GoogleBooksResponse response = restTemplate.getForObject(url, GoogleBooksResponse.class);

            if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
                System.out.println("No hay más resultados. Fin de la importación.");
                break;
            }

            for (Volume item : response.getItems()) {
                if (importados >= maxLibros) break;

                VolumeInfo volumeInfo = item.getVolumeInfo();
                if (volumeInfo == null || volumeInfo.getTitle() == null) continue;
                if (volumeInfo.getImageLinks() == null) continue;

                String fecha = volumeInfo.getPublishedDate();
                if (fecha != null) {
                    try {
                        int year = Integer.parseInt(fecha.substring(0, 4));
                        if (year < yearMin) continue; // descartar libro
                    } catch (Exception e) {
                        // si no se puede parsear, se descarta
                        continue;
                    }
                }

                if (idsVistos.contains(item.getId())) {
                    continue;
                }

                idsVistos.add(item.getId());

                // Evitar duplicados: comprobar si ya existe por GoogleBooksId y aumento los libros que ya tenía en BD
                if (libroRepository.findByGoogleBooksId(item.getId()).isPresent()) {
                    yaExistian++;
                    continue;
                }

                Libro libro = new Libro();
                libro.setGoogleBooksId(item.getId());
                libro.setTitulo(volumeInfo.getTitle());
                libro.setSubtitulo(volumeInfo.getSubtitle());
                libro.setDescripcion(volumeInfo.getDescription());
                libro.setEditorial(volumeInfo.getPublisher());
                libro.setFechaPublicacion(volumeInfo.getPublishedDate());
                libro.setNumeroPaginas(volumeInfo.getPageCount());
                libro.setIdioma(volumeInfo.getLanguage());

                // ISBNs
                if (volumeInfo.getIndustryIdentifiers() != null) {
                    volumeInfo.getIndustryIdentifiers().forEach(identifier -> {
                        if ("ISBN_10".equals(identifier.getType())) {
                            libro.setIsbn10(identifier.getIdentifier());
                        } else if ("ISBN_13".equals(identifier.getType())) {
                            libro.setIsbn13(identifier.getIdentifier());
                        }
                    });
                }

                // Imagen
                if (volumeInfo.getImageLinks() != null) {
                    libro.setImagenPortada(volumeInfo.getImageLinks().getThumbnail());
                }

                // Valoraciones
                libro.setValoracionMedia(volumeInfo.getAverageRating());
                libro.setNumeroValoraciones(volumeInfo.getRatingsCount());

                // === AUTORES ===
                if (volumeInfo.getAuthors() != null) {
                    Set<Autor> autores = new HashSet<>();
                    for (String nombreAutor : volumeInfo.getAuthors()) {
                        Optional<Autor> opt = autorRepository.findFirstByNombreContainingIgnoreCase(nombreAutor);
                        //  TODO: Revisar por qué hay ponerlo en un bloque lambda explícito para que no de error de CrudRepository por el tipo en .save
                        Autor autor = opt.orElseGet(() -> {
                            Autor nuevo = new Autor();
                            nuevo.setNombre(nombreAutor);
                            return autorRepository.save(nuevo);
                        });
                        autores.add(autor);
                    }
                    libro.setAutores(autores);
                }

                // === CATEGORÍAS ===
                if (volumeInfo.getCategories() != null) {
                    Set<Categoria> categorias = new HashSet<>();
                    for (String nombreCategoria : volumeInfo.getCategories()) {
                        Optional<Categoria> opt = categoriaRepository.findFirstByNombreIgnoreCase(nombreCategoria);
                        // Aquí lo mismo que con autores, el bloque lambda explícito funciona mejor para evitar errores en el save()
                        Categoria categoria = opt.orElseGet(() -> {
                            Categoria nueva = new Categoria();
                            nueva.setNombre(nombreCategoria);
                            return categoriaRepository.save(nueva);
                        });
                        categorias.add(categoria);
                    }
                    libro.setCategorias(categorias);
                }

                libroRepository.save(libro);
                importados++;
            }

            startIndex += 20;
            System.out.println("Progreso: " + importados + " nuevos, " + yaExistian + " existentes...");
        }

        System.out.println("=== Importación completada ===");
        System.out.println("Total nuevos: " + importados + ", ya existentes: " + yaExistian);

        return Map.of(
            "importados", importados,
            "yaExistian", yaExistian
        );
    }
}
