package com.mauhernandez.ecommerceapi.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.mauhernandez.ecommerceapi.exception.ConflictoDeNegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthService {

    @Value("${google.client-id}")
    private String clientId;

    public GoogleIdToken.Payload verificar(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new ConflictoDeNegocioException("Token de Google inválido");
            }
            return idToken.getPayload();
        } catch (Exception e) {
            throw new ConflictoDeNegocioException("No se pudo verificar el token de Google");
        }
    }
}