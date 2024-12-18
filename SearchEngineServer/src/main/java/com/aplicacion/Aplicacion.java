package com.aplicacion;

import com.controlador.Controlador;

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
        String archivo = "stopwords-enDS.txt";
        ct.updateStopWords(archivo);

        // realizar consultas
        ct.doQuerys();

        // realizar evaluacion
        ct.doEvaluation();

        // parar solr
        ct.stopSolr();

    }
}
