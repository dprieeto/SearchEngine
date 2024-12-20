package com.controlador;

import com.modelo.evaluacion.Evaluacion;
import com.modelo.solr.client.SolrClient;
import com.modelo.solr.client.SolrClientImp;
import com.modelo.solr.server.SolrServer;
import com.modelo.solr.server.SolrServerImp;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Prieto
 */
public class Controlador {

    private SolrClient cliente;

    private SolrServer server;

    private String stopWordsNameFile;

    public Controlador() {
        cliente = null;
        server = new SolrServerImp();
    }

    public void startServer() {
        System.out.println("Iniciando Solr");
        server.startSolr();
        server.seeStatus();
    }

    public void setStopWordsFile(String nameFile) {
        stopWordsNameFile = nameFile;
    }

    /**
     * Crea la coleccion indexa los documentos dependiendo de los parametros.
     *
     * @param coreName nombre de la coleccion
     * @param deleteCore Si es true, borrara la coleccion y la volvera a crear,
     * false en caso contrario.
     * @param deleteDocuments Si es true, borra los documentos y los vuelve a
     * indexar, false en caso contrario.
     */
    public void buildCoreConf(String coreName, boolean deleteCore, boolean deleteDocuments) {
        server = new SolrServerImp();
        server.setCoreName(coreName);
        cliente = new SolrClientImp(server.getCore());

        // Schema fields
        String campo = "indice";
        String campoTipo = "string";
        String campo2 = "texto";
        String campoTipo2 = "text_general";

        if (deleteCore) {
            server.deleteCore();
            server.createCore();
            // añadir los campos a schema
            server.getCore().addSchemaField(campo, campoTipo);
            server.getCore().addSchemaField(campo2, campoTipo2);

            updateStopWords(stopWordsNameFile);
            // reiniciar el servidor
            System.out.println("\nReiniciando servidor, espere");
            int tiempoEsperaReinicio = 30000;
            server.restartSolr(tiempoEsperaReinicio);

        }

        if (deleteDocuments) {
            cliente.leerArchivoContenido(null);
        }

    }

    public void doEvaluation() {
        Evaluacion ev = new Evaluacion(stopWordsNameFile);
        ev.crearDocumentoEvaluacion(cliente.getConsultasResultados());
        ev.evaluar();
    }

    /**
     * Ejecuta las consultas
     */
    public void doQuerys() {
        cliente.leerArchivoConsultas(null);
    }

    /**
     *
     * @param fileName fileName=null, se utiliza las stopwords que tiene solr en
     * ingles (stopwords_en.txt).
     * @see com.modelo.solr.Constantes#STOPWORDS_FILES_PATH
     */
    private void updateStopWords(String fileName) {
        stopWordsNameFile = fileName;
        cliente.actualizarPalabrasVacias(fileName);
        //cliente.leerArchivoContenido(null);
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
