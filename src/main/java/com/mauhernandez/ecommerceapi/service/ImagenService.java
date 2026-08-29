package com.mauhernandez.ecommerceapi.service;

import com.mauhernandez.ecommerceapi.model.Imagen;
import com.mauhernandez.ecommerceapi.repository.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImagenService {

    private final ImagenRepository imagenRepository;

    @Autowired
    public ImagenService(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    public List<Imagen> listarPorProducto(Long productoId) {
        return imagenRepository.findByProductoIdOrderByOrdenAsc(productoId);
    }

    public Imagen guardar(Imagen imagen) {
        return imagenRepository.save(imagen);
    }

    public void eliminar(Long id) {
        imagenRepository.deleteById(id);
    }

    @Transactional
    public void marcarComoPortada(Long productoId, Long imagenId) {
        List<Imagen> imagenes = imagenRepository.findByProductoIdOrderByOrdenAsc(productoId);

        for (Imagen img : imagenes) {
            if (img.getId().equals(imagenId)) {
                img.setOrden(0);
            } else if (img.getOrden() == 0) {
                img.setOrden(1);
            }
            imagenRepository.save(img);
        }
    }
}
