package com.proyecto.booka.service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


@Service
public class AuthorMetadataService {

    private final RestTemplate rest =
        new RestTemplate();

    public String buscarFotoAutor(
        String nombre
    ) {

        try {

            String query =
                URLEncoder.encode(
                    nombre,
                    StandardCharsets.UTF_8
                );

            String url =
                "https://openlibrary.org/search.json?author="
                + query;

            System.out.println(
                "URL OPENLIBRARY: "
                + url
            );

            Map<?, ?> response =
                rest.getForObject(
                    url,
                    Map.class
                );

            if (response == null) {
                return null;
            }

            List<?> docs =
                (List<?>) response.get("docs");

            if (
                docs == null
                || docs.isEmpty()
            ) {

                System.out.println(
                    "SIN RESULTADOS"
                );

                return null;
            }

            Map<?, ?> first =
                (Map<?, ?>) docs.get(0);

            Object authorKey =
                first.get("author_key");

            if (!(authorKey instanceof List<?> keys)
                || keys.isEmpty()) {

                System.out.println(
                    "SIN AUTHOR KEY"
                );

                return null;
            }

            String olid =
                keys.get(0).toString();

            System.out.println(
                "OLID: "
                + olid
            );

            String coverUrl =
                            "https://covers.openlibrary.org/a/olid/"
                            + olid
                            + "-L.jpg";

                        BufferedImage image =
                ImageIO.read(
                    new URL(coverUrl)
                );

            if (
                image == null
                || image.getWidth() <= 2
                || image.getHeight() <= 2
            ) {

                System.out.println(
                    "IMAGEN PLACEHOLDER"
                );

                return null;
            }

            System.out.println(
                "COVER URL: "
                + coverUrl
            );

            return coverUrl;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}