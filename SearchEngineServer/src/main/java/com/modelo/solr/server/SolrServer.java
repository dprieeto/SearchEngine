package com.modelo.solr.server;

/**
 *
 * @author Prieto
 * @see
 */
public interface SolrServer {

    /**
     * Inicia el servidor de Solr
     */
    public void startSolr();

    /**
     * Muestra el estado del servidor
     */
    public void seeStatus();

    /**
     * Añade el nombre de la coleccion
     *
     * @param nombre nombre de la coleccion,si es null se utiliza la coleccion
     * por defecto.
     * @see com.modelo.solr.Comandos.Constantes
     */
    public void setCoreName(String nombre);

    /**
     * Crea la coleccion
     */
    public void createCore();

    public void stopSolr();

    /**
     * Ejecuta un comando de consola cmd.
     *
     * @param ruta ruta donde se ejecuta
     * @param comando
     */
    public void executeCommand(String ruta, String comando);

    /**
     * Borra una coleccion.
     *
     * @see com.modelo.solr.Comandos.Constantes
     */
    public void deleteCore();

    public CoreConf getCore();

    /**
     * Reinicia Solr
     *
     * @param timeToWait tiempo de espera para el reinicio.
     * @see https://factorpad.com/tech/solr/reference/solr-restart.html
     */
    public void restartSolr(int timeToWait);

}
