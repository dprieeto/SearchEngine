package com.mb.modelo.solr.server;

/**
 *
 * @author Prieto
 */
public interface SolrServer {

    public void startSolr();

    public void seeStatus();

    public void createCore();

    public void stopSolr();

    public void executeCommand(String ruta, String comando);

}
