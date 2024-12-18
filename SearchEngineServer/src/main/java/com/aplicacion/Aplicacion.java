package com.aplicacion;

import com.controlador.Controlador;
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

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        Controlador ct = new Controlador();
        // iniciar solr
        ct.startServer();

        // configurar coleccion
        //ct.buildCoreConf(null, true, true);
        ct.buildCoreConf(null, false, false);

        // actualizar palabras vacias:
        String archivo = "stopwords-filter-en.txt";
        ct.updateStopWords(archivo);

        // realizar consultas
        ct.doQuerys();
        // parar solr
        ct.stopSolr();

    }
}
