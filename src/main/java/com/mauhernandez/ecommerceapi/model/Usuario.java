package com.mauhernandez.ecommerceapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column
    private String telefono;

    @Column(name = "direccion_calle")
    private String direccionCalle;

    @Column
    private String ciudad;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "email_verificado")
    private Boolean emailVerificado = false;

    @Column(name = "proveedor_auth")
    private String proveedorAuth = "LOCAL"; // "LOCAL" o "GOOGLE"

    public enum Rol {
        CLIENTE, ADMIN
    }
}
