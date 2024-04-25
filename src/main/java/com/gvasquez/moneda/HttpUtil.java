package com.gvasquez.moneda;

import javax.json.*;
import java.io.StringReader;
import java.util.Map;
import java.util.Scanner;

public class HttpUtil {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ApiPeticionGet api = new ApiPeticionGet();

        // Obtener la respuesta JSON de la API
        String jsonResponse = api.respuestaApi("https://v6.exchangerate-api.com/v6/edf3b82692db7f4f4d8661e3/latest/USD");

        // Parsear la respuesta JSON y obtener las tasas de cambio
        JsonObject jsonObject;
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonResponse))) {
            jsonObject = jsonReader.readObject();
        }

        // Verificar si se obtuvieron las tasas de cambio correctamente
        if (jsonObject != null && jsonObject.containsKey("conversion_rates")) {
            JsonObject conversionRatesObject = jsonObject.getJsonObject("conversion_rates");

            // Imprimir las tasas de cambio disponibles
            System.out.println("Tasas de cambio disponibles:");
            for (Map.Entry<String, JsonValue> entry : conversionRatesObject.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            // Imprimir las monedas disponibles
            System.out.println("\nMonedas disponibles:");
            for (String moneda : conversionRatesObject.keySet()) {
                System.out.println(moneda);
            }

            // Pedir al usuario que seleccione las monedas de origen y destino
            System.out.print("\nSeleccione la moneda de origen: ");
            String monedaOrigen = sc.next();
            System.out.print("Seleccione la moneda de destino: ");
            String monedaDestino = sc.next();

            // Verificar si las monedas seleccionadas existen en la lista de tasas de cambio
            if (!conversionRatesObject.containsKey(monedaOrigen) || !conversionRatesObject.containsKey(monedaDestino)) {
                System.out.println("\nUna o ambas monedas seleccionadas no están disponibles en la lista de tasas de cambio.");
                return; // Salir del programa si las monedas seleccionadas no están disponibles
            }

            // Pedir al usuario que ingrese la cantidad
            System.out.print("Ingrese la cantidad: ");
            double cantidad = sc.nextDouble();

            // Calcular la conversión
            JsonValue monedaDestinoValue = conversionRatesObject.get(monedaDestino);
            JsonValue monedaOrigenValue = conversionRatesObject.get(monedaOrigen);

            double cantidadConvertida = 0;
            if (monedaDestinoValue != null && monedaOrigenValue != null && monedaDestinoValue instanceof JsonNumber && monedaOrigenValue instanceof JsonNumber) {
                double tasaDeCambio = ((JsonNumber) monedaDestinoValue).doubleValue() / ((JsonNumber) monedaOrigenValue).doubleValue();
                cantidadConvertida = cantidad * tasaDeCambio;
                // Imprimir el resultado de la conversión
                System.out.println("\n" + cantidad + " " + monedaOrigen + " equivalen a " + cantidadConvertida + " " + monedaDestino);
            } else {
                System.out.println("\nLas tasas de cambio para las monedas especificadas no están disponibles.");
            }
        } else {
            System.out.println("No se pudieron obtener las tasas de cambio.");
        }
    }
}
