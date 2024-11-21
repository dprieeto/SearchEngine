package com.mb.modelo.solr.client;

/**
 *
 * @author Prieto
 */
public interface SolrClient {
    
    public void leerArchivoDelCorpus(String nombreArchivo);
    
    public void indexaDocumento(String indice, String texto);
    
    public void borrarTodosLosDocumentos();
}
