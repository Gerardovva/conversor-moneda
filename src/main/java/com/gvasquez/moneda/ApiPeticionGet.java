package com.gvasquez.moneda;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiPeticionGet {

    public String respuestaApi(String url) {
        // Verificar si la URL es nula o vacía
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("La URL no puede ser nula o vacía");
        }

        // Crear un cliente HTTP
        HttpClient httpClient = HttpClient.newHttpClient();

        // Crear la solicitud HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // Enviar la solicitud y obtener la respuesta
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body(); // Devolver el cuerpo de la respuesta como String
        } catch (IOException | InterruptedException e) {
            // Manejar cualquier excepción lanzada durante el envío de la solicitud
            throw new RuntimeException("Error al enviar la solicitud HTTP: " + e.getMessage(), e);
        }
    }



}
