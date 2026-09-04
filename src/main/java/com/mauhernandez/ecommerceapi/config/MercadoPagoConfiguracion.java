package com.mauhernandez.ecommerceapi.config;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MercadoPagoConfiguracion {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostConstruct
    public void inicializar() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }
}