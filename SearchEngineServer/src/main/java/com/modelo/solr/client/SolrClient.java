package com.modelo.solr.client;

import java.util.List;

/**
 *
 * @author Prieto
 */
public interface SolrClient {

    public void leerArchivoContenido(String nombreArchivo);
    
    /**
     * 
     * @param indice numero de documento
     * @param texto 
     */
    public void indexaDocumento(String indice, String texto);

    public void borrarTodosLosDocumentos();

    public void leerArchivoConsultas(String nombreArchivo);

    /**
     * 
     * @param indice numero de consulta
     * @param consulta 
     */
    public void hacerConsulta(String indice, String consulta);
    
    public void actualizarPalabrasVacias();
}
