package com.mauhernandez.ecommerceapi.repository;

import com.mauhernandez.ecommerceapi.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByActivoTrueOrderByOrdenAsc();
}