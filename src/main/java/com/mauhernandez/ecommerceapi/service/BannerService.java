package com.mauhernandez.ecommerceapi.service;

import com.mauhernandez.ecommerceapi.exception.RecursoNoEncontradoException;
import com.mauhernandez.ecommerceapi.model.Banner;
import com.mauhernandez.ecommerceapi.repository.BannerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BannerService {

    private final BannerRepository bannerRepository;

    public BannerService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public List<Banner> listarActivos() {
        return bannerRepository.findByActivoTrueOrderByOrdenAsc();
    }

    public List<Banner> listarTodos() {
        return bannerRepository.findAll();
    }

    public Banner buscarPorId(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Banner no encontrado con id: " + id));
    }

    public Banner guardar(Banner banner) {
        return bannerRepository.save(banner);
    }

    public void eliminar(Long id) {
        bannerRepository.deleteById(id);
    }
}
