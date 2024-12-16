package com.aplicacion;

import com.modelo.solr.client.SolrClient;
import com.modelo.solr.client.SolrClientImp;
import com.modelo.solr.server.SolrServer;
import com.modelo.solr.server.SolrServerImp;
import java.util.Scanner;

/**
 *
 * @author Prieto
 */
public class Aplicacion {

    private static SolrServer server;

    public static void main(String[] args) {
        init();//controlador con facade para client y servidor¿?
        start(); // inicia servidor de solr
        
        SolrClient cliente = new SolrClientImp();
        if(!server.isCoreCreated(null))
            cliente.leerArchivoContenido(null);
        
        //cliente.leerArchivoContenido(null);
        cliente.actualizarPalabrasVacias();
        
        //cliente.leerArchivoConsultas(null);
        
        stop(); // para el servidor al introducir -1 en consola
    }

    private static void init() {
        server = new SolrServerImp();
    }

    private static void start() {
        System.out.println("Iniciando Solr");
        server.startSolr();
        server.seeStatus();
        
        
        /**
        /**
         * Descomentar el siguiente codigo y comentar el siguiente a este 
         * si se desea borrar la coleccion y los documentos del corpus para
         * volver a crearlo e indexarlo en solr cada vez que se ejecute. 
         */
        /*
        if(server.isCoreCreated(null))
            server.deleteCore(null);

        server.createCore(null);
        server.addSchemaField("indice", "text_general");
        server.addSchemaField("texto", "text_en");
        */
        
        /**
         * Si la coleccion no esta creada se crea y se añaden los campos al 
         * schema
         */
        
        if(!server.isCoreCreated(null)) {
            server.createCore(null);
            server.addSchemaField("indice", "text_general");
            server.addSchemaField("texto", "text_en");
        }
        
    }

    /**
     * Introducir -1 por pantalla para parar Solr
     */
    private static void stop() {
        Scanner sc = new Scanner(System.in);
        String opcion = "";
        while (!opcion.equals("-1")) {
            opcion = sc.nextLine();
        }
        server.stopSolr();
    }
}
