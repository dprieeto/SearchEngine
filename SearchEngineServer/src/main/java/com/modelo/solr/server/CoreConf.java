package com.modelo.solr.server;

import com.modelo.solr.Comandos;
import com.modelo.solr.Constantes;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CoreAdminParams;

/**
 *
 * @author Prieto
 */
public class CoreConf {

    private final String coreName;

    public CoreConf(String nombre) {
        if (nombre == null) {
            coreName = Constantes.NOMBRE_DEFAULT_COLECCION;
        } else {
            coreName = nombre;
        }
    }

    public String getCoreName() {
        return coreName;
    }

    /**
     * Verifica si existe la coleccion pasada por parametro. Si el valor es null
     * se verifica la coleccion por defecto.
     *
     * @return true si la coleccion existe.
     */
    public boolean isCoreCreated() {
        String nombre = getCoreName();
        boolean encontrado = false;
        if (nombre == null) {
            nombre = Comandos.Constantes.NOMBRE_DEFAULT_COLECCION;
        }
        try (SolrClient solrClient = new HttpSolrClient.Builder(Comandos.Constantes.URL).build()) {
            // realiza una consulta para obtener la lista de colecciones
            CoreAdminRequest request = new CoreAdminRequest();
            request.setAction(CoreAdminParams.CoreAdminAction.STATUS);
            CoreAdminResponse cores = request.process(solrClient);

            if (cores.getCoreStatus().get(nombre) != null) {
                encontrado = true;
            }
            //System.out.println(cores.getCoreStatus().get(nombre));           
        } catch (SolrServerException | IOException e) {
            System.err.println("\nSe ha producido un error al verificar el core." + e.getMessage());
            return false;
        }
        return encontrado;
    }

    /**
     * Añade un campo a la coleccion por defecto mediante SchemaRequest.
     *
     * @param nombre nombre del campo
     * @param tipo tipo del campo. p.ej: 'text_en'
     */
    public void addSchemaField(String nombre, String tipo) {
        String url = Constantes.URL_DEFAULT_COLLECTION;
        try {
            SolrClient solrClient = new HttpSolrClient.Builder(url).build();

            Map<String, Object> propiedadesCampo = Field.getDefaultField(nombre, tipo);

            // Crear una solicitud para agregar un nuevo campo
            SchemaRequest.AddField addFieldRequest = new SchemaRequest.AddField(propiedadesCampo);
            solrClient.request(addFieldRequest);

            System.out.println("\nSe ha agregado el campo " + nombre + " correctamente");
        } catch (SolrServerException | IOException ex) {
            System.err.println("\nError al agregar un nuevo campo al Schema.\n" + ex.getMessage());
        }
    }

    public int contarDocumentosIndexados() {
        int documentos = -1;
        try {

            HttpSolrClient solr = new HttpSolrClient.Builder(Constantes.URL_SOLR + Constantes.NOMBRE_DEFAULT_COLECCION).build();

            SolrQuery query = new SolrQuery();
            query.setQuery("*:*");
            query.setFields("indice");

            QueryResponse rsp = solr.query(query);

            SolrDocumentList docs = rsp.getResults();
            long encontrados = rsp.getResults().getNumFound();
            documentos = (int) encontrados;
        } catch (SolrServerException | IOException ex) {
            System.err.println("Error al contar documentos indexados en la coleccion.\n"
                    + ex.getMessage());
        }
        return documentos;
    }
}
