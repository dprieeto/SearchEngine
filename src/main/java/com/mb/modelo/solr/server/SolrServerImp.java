package com.mb.modelo.solr.server;

import com.mb.modelo.solr.Comandos;
import com.mb.modelo.solr.Constantes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.request.schema.SchemaRequest.AddField;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;

/**
 *
 * @author Prieto
 */
public class SolrServerImp implements SolrServer {

    private Process process;

    public SolrServerImp() {
        process = null;
    }

    @Override
    public void startSolr() {
        if (process != null && process.isAlive()) {
            System.out.println("El servidor Solr ya está en ejecución.");
            return; // No iniciar un nuevo proceso si ya está en ejecución
        }
        executeCommand(null, Comandos.START);
    }

    @Override
    public void seeStatus() {
        executeCommand(null, Comandos.STATUS);
    }

    @Override
    public void createCore(String nombre) {
        if (nombre == null) {
            nombre = Comandos.Constantes.NOMBRE_DEFAULT_COLECCION;
        }
        executeCommand(null, Comandos.CREATE_CORE + nombre);
    }

    @Override
    public void deleteCore(String nombre) {
        if (nombre == null) {
            nombre = Comandos.Constantes.NOMBRE_DEFAULT_COLECCION;
        }
        System.out.println("\n Eliminando coleccion.");
        executeCommand(null, Comandos.DELETE_CORE + nombre);
    }

    @Override
    public void stopSolr() {
        executeCommand(null, Comandos.STOP);
    }

    @Override
    public synchronized void executeCommand(String ruta, String comando) {
        try {
            // Construir el comando completo
            String command = comando;
            if (ruta == null) {
                ruta = Comandos.Constantes.SOLR_RUTA;
            }
            // Usar ProcessBuilder para iniciar el proceso   
            ProcessBuilder pb = new ProcessBuilder();
            pb.directory(new java.io.File(ruta));
            pb.command("cmd.exe", "/c", command);

            if (!command.equals(Comandos.STOP)) {
                pb.redirectErrorStream(true); // Redirige el error al flujo de salida
            } else {
                pb.redirectErrorStream(false);
            }
            //pb.inheritIO(); //no sirve

            // Inicia el proceso
            process = pb.start();
            readProcessOutput(process);
            //int exitCode = process.waitFor();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee la salida del proceso mediante una expresion lambda. Si no se utiliza
     * un hilo, el buffer se llena y no funciona.
     *
     * @param process
     */
    private void readProcessOutput(Process process) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public boolean isCoreCreated(String nombre) {
        boolean encontrado = false;
        if (nombre == null) {
            nombre = Comandos.Constantes.NOMBRE_DEFAULT_COLECCION;
        }
        try (SolrClient solrClient = new HttpSolrClient.Builder(Comandos.Constantes.URL).build()) {
            // realiza una consulta para obtener la lista de colecciones
            CoreAdminRequest request = new CoreAdminRequest();
            request.setAction(CoreAdminAction.STATUS);
            CoreAdminResponse cores = request.process(solrClient);

            if (cores.getCoreStatus().get(nombre) != null) {
                encontrado = true;
            }
            //System.out.println(cores.getCoreStatus().get(nombre));           
        } catch (SolrServerException | IOException e) {
            System.err.println("\nSe ha producido un error al verificar el core." + e.getMessage());
            return false;
        }
        return encontrado;
    }

    @Override
    public void addSchemaField(String nombre, String tipo) {
        String url = Constantes.URL_DEFAULT_COLLECTION;
        try {
            SolrClient solrClient = new HttpSolrClient.Builder(url).build();

            Map<String, Object> propiedadesCampo = Field.getDefaultField(nombre, tipo);

            // Crear una solicitud para agregar un nuevo campo
            AddField addFieldRequest = new AddField(propiedadesCampo);
            solrClient.request(addFieldRequest);

            System.out.println("\nSe ha agregado el campo " + nombre + " correctamente");
        } catch (SolrServerException | IOException ex) {
            Logger.getLogger(SolrServerImp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
