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

        // actualizar palabras vacias: da mejores resultados en5
        String stopWordsFileName = "stopwords-en5.txt"; 
        ct.setStopWordsFile(stopWordsFileName);
        
        // configurar coleccion
        ct.buildCoreConf(null, true, true);
        //ct.buildCoreConf(null, false, false);
        
        // realizar consultas
        ct.doQuerys();

        // realizar evaluacion
        ct.doEvaluation();

        // parar solr
        ct.stopSolr();

    }
}
