package com.controlador;

import com.modelo.solr.client.SolrClient;
import com.modelo.solr.client.SolrClientImp;
import com.modelo.solr.server.SolrServer;
import com.modelo.solr.server.SolrServerImp;
import java.util.Scanner;

/**
 *
 * @author Prieto
 */
public class Controlador {

    private SolrClient cliente;

    private SolrServer server;

    public Controlador() {
        cliente = new SolrClientImp();
        server = new SolrServerImp();
    }
    
    public void startServer(){
        System.out.println("Iniciando Solr");
        server.startSolr();
        server.seeStatus();
    }

    /**
     * Si la coleccion no existe la crea y define los campos para el corpus.
     * Si no hay documentos en la coleccion los indexa. 
     * @param coreName nombre de la coleccion
     * @param deleteCore Si es true, borrara la coleccion y la volvera a crear,
     * false en caso contrario.
     * @param deleteDocuments Si es true, borra los documentos y los vuelve a
     * indexar, false en caso contrario.
     */
    public void buildCoreConf(String coreName, boolean deleteCore, boolean deleteDocuments) {
        server = new SolrServerImp();
        String campo = "indice";
        String campoTipo = "text_general";
        String campo2 = "texto";
        String campoTipo2 = "text_en";
        
        if(!server.getCore().isCoreCreated()) {
            server.createCore(coreName);
            // añadir los campos a schema
            server.getCore().addSchemaField(campo, campoTipo);
            server.getCore().addSchemaField(campo2, campoTipo2);
        }

        if (deleteCore && server.getCore().isCoreCreated()) {
            server.deleteCore();
        }
        if(deleteDocuments && server.getCore().contarDocumentosIndexados() >0) 
            cliente.leerArchivoContenido(null);
        
        if(server.getCore().contarDocumentosIndexados() == 0)
            cliente.leerArchivoContenido(null);
            
    }
    
    public void doQuerys() {
        cliente.leerArchivoConsultas(null);
    }
    
    /**
     * Introducir -1 por pantalla para parar Solr.
     */
    public void stopSolr() {
        Scanner sc = new Scanner(System.in);
        String opcion = "";
        while (!opcion.equals("-1")) {
            opcion = sc.nextLine();
        }
        server.stopSolr();
    }
    
    
}
