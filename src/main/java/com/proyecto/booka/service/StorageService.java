package com.proyecto.booka.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class StorageService {

    private final Cloudinary cloudinary;

    public StorageService(
        Cloudinary cloudinary
    ) {
        this.cloudinary = cloudinary;
    }

    public String saveProfileImage(
        MultipartFile file,
        Long userId
    ) {

        try {

            @SuppressWarnings("rawtypes")
            Map uploadResult =
                cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                        "folder",
                        "booka/profiles",
                        "public_id",
                        "user_" + userId
                    )
                );

            return uploadResult
                .get("secure_url")
                .toString();

        } catch (IOException e) {

            throw new RuntimeException(
                "Error subiendo imagen",
                e
            );
        }
    }
}