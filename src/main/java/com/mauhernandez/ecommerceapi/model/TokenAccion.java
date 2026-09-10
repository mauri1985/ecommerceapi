package com.mauhernandez.ecommerceapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tokens_accion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenAccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;

    private Boolean usado = false;

    public enum Tipo {
        VERIFICACION_EMAIL, RESET_PASSWORD
    }
}
