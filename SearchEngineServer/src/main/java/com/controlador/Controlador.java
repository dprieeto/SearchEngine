package com.controlador;

import com.modelo.solr.client.SolrClient;
import com.modelo.solr.client.SolrClientImp;
import com.modelo.solr.server.SolrServer;
import com.modelo.solr.server.SolrServerImp;

/**
 *
 * @author Prieto
 */
public class Controlador {
    
    private SolrClient cliente;
    
    private SolrServer server;

    public Controlador() {
        cliente = new SolrClientImp();
        server = null;
    }

    
    
    /**
     * 
     * @param coreName nombre de la coleccion
     * @param deleteCore Si es true, borrara la coleccion y la volvera a crear,
     * si es false
     * @param deleteDocuments Si es true, borra los documentos y los vuelve a 
     * indexar,  si false no los indexa si existen documentos indexados en la 
     * coleccion. 
     */
    public void buildCoreConf(String coreName, boolean deleteCore, boolean deleteDocuments) {
        server = new SolrServerImp(coreName);
        
        String campo = "indice";
        String campoTipo = "text_general";
        String campo2 = "texto";
        String campoTipo2 = "text_en";
        
        if(deleteCore && server.getCore().isCoreCreated()) {
            server.createCore();
            // añadir los campos a schema
            server.getCore().addSchemaField(campo, campoTipo);
            server.getCore().addSchemaField(campo2, campoTipo2);
        }
    }
}
