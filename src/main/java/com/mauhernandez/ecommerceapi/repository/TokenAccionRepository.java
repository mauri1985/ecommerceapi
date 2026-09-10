package com.mauhernandez.ecommerceapi.repository;

import com.mauhernandez.ecommerceapi.model.TokenAccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenAccionRepository extends JpaRepository<TokenAccion, Long> {
    Optional<TokenAccion> findByTokenAndTipo(String token, TokenAccion.Tipo tipo);
}