package com.proyecto.bibliotecauji.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBooksResponse {

    private List<Volume> items;
    // getters y setters

    public List<Volume> getItems() {
        return items;
    }

    public void setItems(List<Volume> items) {
        this.items = items;
    }
    
}
