package com.proyecto.booka.controller;

import com.proyecto.booka.model.Usuario;
import com.proyecto.booka.repository.UsuarioRepository;
import com.proyecto.booka.security.UserContextService;
import com.proyecto.booka.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioMediaController {

    @Autowired private StorageService storageService;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private UserContextService userContext;

    @PostMapping("/{id}/imagen")
    public ResponseEntity<?> uploadProfileImage(@PathVariable Long id,
                                                @RequestParam("file") MultipartFile file) {

        Usuario actual = userContext.getUsuarioActual();

        if (!actual.getId().equals(id)) {
            return ResponseEntity.status(403).body("No puedes modificar el perfil de otro usuario");
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }

        String publicUrl = storageService.saveProfileImage(file, id);

        actual.setProfileImage(publicUrl);
        usuarioRepository.save(actual);

        return ResponseEntity.ok(
                new UploadResponse(publicUrl)
        );
    }

    // DTO interno
    record UploadResponse(String url) {}
}
