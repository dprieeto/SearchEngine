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
        init();
        start(); // inicia servidor de solr
        SolrClient cliente = new SolrClientImp();
        cliente.leerArchivoDelCorpus(null);
        stop(); // para el servidor al introducir -1 en consola
    }

    private static void init() {
        server = new SolrServerImp();
    }

    private static void start() {
        System.out.println("Iniciando Solr");
        server.startSolr();
        server.seeStatus();
        server.createCore();
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
