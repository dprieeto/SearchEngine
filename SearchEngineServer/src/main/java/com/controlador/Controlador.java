package com.controlador;

import com.modelo.evaluacion.Evaluacion;
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

    private String stopWords;

    public Controlador() {
        cliente = null;
        server = new SolrServerImp();
    }

    public void startServer() {
        System.out.println("Iniciando Solr");
        server.startSolr();
        server.seeStatus();
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
        }

        if (deleteDocuments) {
            cliente.leerArchivoContenido(null);
        }
        /**
         * if (!server.getCore().isCoreCreated()) { server.createCore(); //
         * añadir los campos a schema server.getCore().addSchemaField(campo,
         * campoTipo); server.getCore().addSchemaField(campo2, campoTipo2); }
         *
         * if (deleteCore && server.getCore().isCoreCreated()) {
         * server.deleteCore(); }
         *
         * // si la coleccion existe if (deleteDocuments &&
         * server.getCore().contarDocumentosIndexados() > 0 &&
         * server.getCore().isCoreCreated()) {
         * cliente.leerArchivoContenido(null); }
         *
         * // si la coleccion existe y no hay documentos, se indexan if
         * (server.getCore().contarDocumentosIndexados() == 0 &&
         * server.getCore().isCoreCreated()) {
         * cliente.leerArchivoContenido(null); }
         *
         */

    }

    public void doEvaluation() {
        Evaluacion ev = new Evaluacion(stopWords);
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
    public void updateStopWords(String fileName) {
        stopWords = fileName;
        cliente.actualizarPalabrasVacias(fileName);

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
