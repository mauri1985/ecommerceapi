package com.mauhernandez.ecommerceapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "banners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String subtitulo;

    @Column(name = "texto_boton")
    private String textoBoton;

    private String link;

    @Column(name = "imagen_url")
    private String imagenUrl;

    private Integer orden = 0;

    private Boolean activo = true;
}
