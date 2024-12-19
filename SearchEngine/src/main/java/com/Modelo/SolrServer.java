package com.Modelo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Esta clase solo tiene un metodo estatico. Verifica si solr server esta 
 * corriendo
 * @author Prieto
 */
public class SolrServer {
    
    /**
     * 
     * @return true si el serivodr de solr esta corriendo, false en caso 
     * contrario.
     */
    public static boolean isSolrServerRunning() {
        try {
            String solrUrl = "http://localhost:8983/solr";
            URL url = new URL(solrUrl); // Endpoint para verificar el estado
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000); // Tiempo de espera para la conexión
            connection.setReadTimeout(2000); // Tiempo de espera para la lectura

            int responseCode = connection.getResponseCode();
            return responseCode == 200; // 200 significa que el servidor está en funcionamiento
        } catch (IOException e) {
            //e.printStackTrace();
            return false; // Si hay una excepción, el servidor no está disponible
        }
    }
}
