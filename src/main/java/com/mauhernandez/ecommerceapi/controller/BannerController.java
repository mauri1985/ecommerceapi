package com.mauhernandez.ecommerceapi.controller;

import com.mauhernandez.ecommerceapi.model.Banner;
import com.mauhernandez.ecommerceapi.service.BannerService;
import com.mauhernandez.ecommerceapi.service.CloudinaryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/banners")
public class BannerController {

    private final BannerService bannerService;
    private final CloudinaryService cloudinaryService;

    public BannerController(BannerService bannerService, CloudinaryService cloudinaryService) {
        this.bannerService = bannerService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public List<Banner> listarActivos() {
        return bannerService.listarActivos();
    }

    @GetMapping("/todos")
    public List<Banner> listarTodos() {
        return bannerService.listarTodos();
    }

    @PostMapping
    public Banner crear(@RequestBody Banner banner) {
        banner.setId(null);
        return bannerService.guardar(banner);
    }

    @PutMapping("/{id}")
    public Banner actualizar(@PathVariable Long id, @RequestBody Banner banner) {
        bannerService.buscarPorId(id); // valida que exista
        banner.setId(id);
        return bannerService.guardar(banner);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        bannerService.eliminar(id);
    }

    @PostMapping("/{id}/imagen")
    public Banner subirImagen(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo) {
        Banner banner = bannerService.buscarPorId(id);
        String url = cloudinaryService.subirImagen(archivo);
        banner.setImagenUrl(url);
        return bannerService.guardar(banner);
    }
}