package com.mb.aplicacion;

import com.mb.modelo.solr.client.*;
import com.mb.modelo.solr.server.SolrServer;
import com.mb.modelo.solr.server.SolrServerImp;
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
        cliente.leerArchivoContenido(null);
        
        cliente.leerArchivoConsultas(null);
        
        stop(); // para el servidor al introducir -1 en consola
    }

    private static void init() {
        server = new SolrServerImp();
    }

    private static void start() {
        System.out.println("Iniciando Solr");
        server.startSolr();
        server.seeStatus();
        if(server.isCoreCreated(null))
            server.deleteCore(null);
        server.createCore(null);
        
        server.addSchemaField("indice", "text_general");
        server.addSchemaField("texto", "text_en");
    }

    private static void stop() {
        Scanner sc = new Scanner(System.in);
        String opcion = "";
        while (!opcion.equals("-1")) {
            opcion = sc.nextLine();
        }
        server.stopSolr();
    }
}
