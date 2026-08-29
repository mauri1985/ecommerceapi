package com.mauhernandez.ecommerceapi.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mauhernandez.ecommerceapi.exception.ConflictoDeNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final long TAMANIO_MAXIMO_BYTES = 5 * 1024 * 1024; // 5 MB
    private static final List<String> FORMATOS_PERMITIDOS = List.of("image/jpeg", "image/jpg", "image/gif", "image/png");

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String subirImagen(MultipartFile archivo) {
        validar(archivo);

        try {
            Map<?, ?> resultado = cloudinary.uploader().upload(
                    archivo.getBytes(),
                    ObjectUtils.asMap("folder", "ecommerce-productos")
            );
            return (String) resultado.get("secure_url");
        } catch (IOException e) {
            throw new ConflictoDeNegocioException("Error al subir la imagen. Intentá de nuevo.");
        }
    }

    private void validar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ConflictoDeNegocioException("El archivo está vacío");
        }
        if (archivo.getSize() > TAMANIO_MAXIMO_BYTES) {
            throw new ConflictoDeNegocioException("La imagen no puede superar los 5 MB");
        }
        String tipo = archivo.getContentType();
        if (tipo == null || !FORMATOS_PERMITIDOS.contains(tipo.toLowerCase())) {
            throw new ConflictoDeNegocioException("Formato no permitido. Solo se aceptan JPG, JPEG, GIF o PNG");
        }
    }
}