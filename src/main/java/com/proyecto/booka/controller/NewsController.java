package com.proyecto.booka.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.proyecto.booka.dto.NewsArticleDTO;
import com.proyecto.booka.service.NewsService;

@RestController
@RequestMapping("/noticias")
public class NewsController {

    private final NewsService newsService;

    public NewsController(
        NewsService newsService
    ) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<NewsArticleDTO> noticias() {

        return newsService.getNoticias();
    }
}