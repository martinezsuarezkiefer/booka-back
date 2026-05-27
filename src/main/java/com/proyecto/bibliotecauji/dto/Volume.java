package com.proyecto.bibliotecauji.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Volume {

    private String id;
    private VolumeInfo volumeInfo;
    // getters y setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }
    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
    

}
