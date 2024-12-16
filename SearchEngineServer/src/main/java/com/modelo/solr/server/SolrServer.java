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
     * Crea una coleccion
     *
     * @param nombre Nombre de la coleccion, si nombre=null se creara con el
     * nombre por defecto.
     * @see com.modelo.solr.Comandos.Constantes
     */
    public void createCore(String nombre);

    public void stopSolr();
    
    /**
     * Ejecuta un comando de consola cmd.
     * @param ruta ruta donde se ejecuta
     * @param comando
     */
    public void executeCommand(String ruta, String comando);

    /**
     * Borra una coleccion.
     *
     * @param nombre Nombre de la coleccion, si nombre=null se creara con el
     * nombre por defecto.
     * @see com.modelo.solr.Comandos.Constantes
     */
    public void deleteCore(String nombre);
    
    /**
     * Verifica si existe la coleccion pasada por parametro. Si el valor es null
     * se verifica la coleccion por defecto.
     * @param nombre
     * @return true si la coleccion existe.
     */
    public boolean isCoreCreated(String nombre);
    
    /**
     *  Añade un campo a la coleccion por defecto mediante SchemaRequest.
     * @param nombre nombre del campo
     * @param tipo tipo del campo. p.ej: 'text_en'
     */
    public void addSchemaField(String nombre, String tipo);

}
