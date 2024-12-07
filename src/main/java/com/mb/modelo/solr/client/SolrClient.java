package com.mb.modelo.solr.client;

/**
 *
 * @author Prieto
 */
public interface SolrClient {

    public void leerArchivoContenido(String nombreArchivo);

    public void indexaDocumento(String indice, String texto);

    public void borrarTodosLosDocumentos();

    public void leerArchivoConsultas(String nombreArchivo);

    public void hacerConsulta(String indice, String consulta);
}
