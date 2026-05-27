package com.proyecto.booka.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class StorageService {

    @Value("${upload.profile.folder}")
    private String uploadFolder;

    public String saveProfileImage(MultipartFile file, Long userId) {

        String fileName = "user_" + userId + "_" + System.currentTimeMillis() + ".png";

        File folder = new File(uploadFolder);
        if (!folder.exists()) folder.mkdirs();

        File destination = new File(folder, fileName);

        try {
            file.transferTo(destination);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }

        return "/uploads/profiles/" + fileName;
    }
}
