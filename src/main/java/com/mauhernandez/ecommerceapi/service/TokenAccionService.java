package com.mauhernandez.ecommerceapi.service;

import com.mauhernandez.ecommerceapi.exception.ConflictoDeNegocioException;
import com.mauhernandez.ecommerceapi.model.TokenAccion;
import com.mauhernandez.ecommerceapi.model.Usuario;
import com.mauhernandez.ecommerceapi.repository.TokenAccionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenAccionService {

    private final TokenAccionRepository tokenAccionRepository;

    public TokenAccionService(TokenAccionRepository tokenAccionRepository) {
        this.tokenAccionRepository = tokenAccionRepository;
    }

    public String generar(Usuario usuario, TokenAccion.Tipo tipo) {
        TokenAccion token = new TokenAccion();
        token.setToken(UUID.randomUUID().toString());
        token.setUsuario(usuario);
        token.setTipo(tipo);
        token.setFechaExpiracion(LocalDateTime.now().plusHours(1));
        tokenAccionRepository.save(token);
        return token.getToken();
    }

    public Usuario validarYUsar(String token, TokenAccion.Tipo tipo) {
        TokenAccion tokenAccion = tokenAccionRepository.findByTokenAndTipo(token, tipo)
                .orElseThrow(() -> new ConflictoDeNegocioException("Token inválido"));

        if (tokenAccion.getUsado()) {
            throw new ConflictoDeNegocioException("Este enlace ya fue utilizado");
        }
        if (tokenAccion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new ConflictoDeNegocioException("Este enlace expiró");
        }

        tokenAccion.setUsado(true);
        tokenAccionRepository.save(tokenAccion);
        return tokenAccion.getUsuario();
    }
}