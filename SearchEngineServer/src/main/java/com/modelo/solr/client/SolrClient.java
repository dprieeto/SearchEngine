package com.modelo.solr.client;

import com.modelo.evaluacion.DocumentoRecuperado;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Prieto
 */
public interface SolrClient {

    public void leerArchivoContenido(String nombreArchivo);
    
    /**
     * 
     * @param indice numero del documento
     * @param texto  cuerpo del documento
     */
    public void indexaDocumento(String indice, String texto);

    public void borrarTodosLosDocumentos();

    public void leerArchivoConsultas(String nombreArchivo);

    /**
     * 
     * @param indice numero de consulta
     * @param consulta cuerpo de la consulta
     */
    public void hacerConsulta(String indice, String consulta);
    
    public void actualizarPalabrasVacias(String nombreArchivo);
    
    public Map<Integer, List<DocumentoRecuperado>> getConsultasResultados();
}
