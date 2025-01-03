package com.modelo.solr;

/**
 *
 * @author Prieto
 */
public final class Comandos {

    public final class Constantes {

        public static final String SOLR_RUTA = "C:\\solr\\bin";

        public static final String NOMBRE_DEFAULT_COLECCION = "MedicalCollection";

        public static final String SCRIPT_NAME = "solr.cmd";

        public static final String POST_TOOL = "post.jar";

        public static final String DOCS_RUTA = "";

        public static final String URL_DEFAULT_COLLECTION = "http://localhost:8983/solr/" + NOMBRE_DEFAULT_COLECCION;

        public static final String URL = "http://localhost:8983/solr/";

    }

    public static final String START = Constantes.SCRIPT_NAME + " " + "start";

    public static final String STATUS = Constantes.SCRIPT_NAME + " " + "status";

    public static final String STOP = Constantes.SCRIPT_NAME + " " + "stop -all";
    
    public static final String CREATE_CORE = Constantes.SCRIPT_NAME + " "
            + "create -c ";// + Constantes.NOMBRE_COLECCION;
    
    public static final String DELETE_CORE = Constantes.SCRIPT_NAME + " "
            + "delete -c ";// + Constantes.NOMBRE_COLECCION;

    public static final String POST_TOOL = "java -Dc=" + Constantes.NOMBRE_DEFAULT_COLECCION
            + " -Dauto -jar " + Constantes.POST_TOOL + " " + Constantes.DOCS_RUTA + "\\";
    
    public static final String RESTART_SOLR = Constantes.SCRIPT_NAME + " restart -p 8983";
    
}
