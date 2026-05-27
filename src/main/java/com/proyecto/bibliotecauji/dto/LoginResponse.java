package com.proyecto.bibliotecauji.dto;

public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";
    private String email;
    private Long id;
    private String nombre;
    // constructor, getters
    public LoginResponse(String token, String tokenType, String email, Long id, String nombre) {
        this.token = token;
        this.tokenType = tokenType;
        this.email = email;
        this.id = id;
        this.nombre = nombre;
    }
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getTokenType() {
        return tokenType;
    }
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
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
    
}
