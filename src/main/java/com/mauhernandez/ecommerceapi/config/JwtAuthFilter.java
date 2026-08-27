package com.mauhernandez.ecommerceapi.config;

import com.mauhernandez.ecommerceapi.service.UsuarioService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @Autowired
    public JwtAuthFilter(JwtService jwtService, UsuarioService usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String email = jwtService.extraerEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                usuarioService.buscarPorEmail(email).ifPresent(usuario -> {
                    if (jwtService.esTokenValido(token, email)) {
                        var authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());

                        var authToken = new UsernamePasswordAuthenticationToken(
                                usuario.getEmail(), null, List.of(authority)
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                });
            }
        } catch (JwtException | IllegalArgumentException ex) {
            // Token vencido, malformado, o firma inválida: lo tratamos como request anónima.
            // No autenticamos, pero dejamos que la cadena de filtros siga su curso normal.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}