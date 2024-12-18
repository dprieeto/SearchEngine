package com.modelo.solr.client;

import com.modelo.solr.Constantes;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Prieto
 */
public class StopWords {

    /**
     * Nombre del fichero stopwords que se va a utilizar
     */
    private final String nombreArchivo;

    public StopWords(String nombreArchivo) {
        if (nombreArchivo == null) {
            nombreArchivo = Constantes.STOPWORDS_DEFAULT_FILE; // fichero por defecto
        }
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /*
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
     */
    /**
     * Actualiza las stopwords de la colecccion
     * https://github.com/stopwords-iso/stopwords-en/tree/master/raw
     */
    public void actualizarPalabrasVacias() {
        Set<String> stopWords = getStopWords();
        System.out.println("Stopwords a anadir: " + stopWords.size());
        
        try {
            String rutaSolr = Constantes.STOPWORDS_SOLR__FILE_PATH;
            // Actualizar las stop words en el archivo
            //Files.write(Paths.get(rutaSolr), stopWords, StandardOpenOption.APPEND);
            // reemplazar las stopwords existentes
            Files.write(Paths.get(rutaSolr), stopWords);
            System.out.println("Stop words añadidas correctamente al archivo.");
        } catch (IOException e) {
            System.err.println("Error al actualizar las palabras vacias");
        }
        System.out.println("Palabras vacias actualizadas");
    }

    /**
     * Lee el fichero stopwords seleccionado y lo devuelve
     * @return 
     */
    private Set<String> getStopWords() {
        BufferedReader br = null;
        Set<String> stopWords = new HashSet<>();
        try {
            String carpAct = System.getProperty("user.dir");
            String ruta = Constantes.STOPWORDS_FILES_PATH + getNombreArchivo();
            String fileName = carpAct + ruta;
            String linea;
            br = new BufferedReader(new FileReader(fileName));
            while ((linea = br.readLine()) != null) {
                String palabra = linea.trim(); // Eliminar espacios en blanco
                if (!palabra.isEmpty()) { // Asegurarse de que no esté vacía
                    stopWords.add(palabra);
                }

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StopWords.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StopWords.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(StopWords.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return stopWords;
    }

}
