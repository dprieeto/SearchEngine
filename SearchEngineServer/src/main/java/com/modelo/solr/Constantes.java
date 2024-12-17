package com.modelo.solr;

/**
 *
 * @author Prieto
 */
public class Constantes {

    public static final String DOCS_RUTA = "\\src\\corpus\\";

    public static final String URL_SOLR = "http://localhost:8983/solr/";

    public static final String NOMBRE_DEFAULT_COLECCION = "MedicalCollection";

    public static final String URL_DEFAULT_COLLECTION = "http://localhost:8983/solr/" + NOMBRE_DEFAULT_COLECCION;

    public static final String NOMBRE_ARCHIVO_CORPUS = "MED.ALL";

    public static final String NOMBRE_ARCHIVO_CONSULTAS = "MED.QRY";
    
    public static final String SCHEMA_TEXT_FIELD = "texto";
    
    public static final String STOPWORDS_PATH = "C:\\solr\\server\\solr\\MedicalCollection\\conf\\stopwords.txt";
}
