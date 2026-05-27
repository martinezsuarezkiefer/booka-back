package com.proyecto.booka.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.proyecto.booka.dto.NewsArticleDTO;

@Service
public class NewsService {

    private final RestTemplate restTemplate =
        new RestTemplate();

    private final String API_KEY =
        "9b48f6774405cccc80edbea6a65dfefe";

    @SuppressWarnings({ "unchecked", "null" })
    public List<NewsArticleDTO> getNoticias() {

        String url =
            "https://gnews.io/api/v4/search"
            + "?q=books OR literature OR authors"
            + "&lang=en"
            + "&max=12"
            + "&apikey=" + API_KEY;

        Map<String, Object> response =
            restTemplate.getForObject(
                url,
                Map.class
            );

        List<Map<String, Object>> articles =
            (List<Map<String, Object>>)
                response.get("articles");

        return articles.stream()
            .map(article -> {

                NewsArticleDTO dto =
                    new NewsArticleDTO();

                dto.title =
                    (String) article.get("title");

                dto.description =
                    (String) article.get("description");

                dto.image =
                    (String) article.get("image");

                dto.url =
                    (String) article.get("url");

                dto.publishedAt =
                    (String) article.get("publishedAt");

                Map<String, Object> source =
                    (Map<String, Object>)
                        article.get("source");

                dto.source =
                    source != null
                    ? (String) source.get("name")
                    : null;

                return dto;

            })
            .toList();
    }
}