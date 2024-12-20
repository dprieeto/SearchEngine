package com.modelo.solr.server;

import com.controlador.Controlador;
import com.modelo.solr.Comandos;
import com.modelo.solr.Constantes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Prieto
 */
public class SolrServerImp implements SolrServer {

    private Process process;

    private CoreConf core;

    public SolrServerImp() {
        process = null;
        core = new CoreConf();
    }

    @Override
    public CoreConf getCore() {
        return core;
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
    public void setCoreName(String nombre) {
        if(nombre == null)
            nombre = Constantes.NOMBRE_DEFAULT_COLECCION;
        core.setCoreName(nombre);
    }

    @Override
    public void createCore() {        
        //nombre = core.getCoreName();//core.setCoreName(nombre);
        executeCommand(null, Comandos.CREATE_CORE + core.getCoreName());
    }

    @Override
    public void deleteCore() {
        /*if (nombre == null) {
            nombre = core.getCoreName();//Comandos.Constantes.NOMBRE_DEFAULT_COLECCION;
        }*/
        String nombre = core.getCoreName();
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
        } catch (IOException | InterruptedException e) {
            System.err.println("\nSe ha producido un error al ejecutar el comando.\n" + e.getMessage());
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
                System.err.println("\nError al leer la salida del proceso.\n" + e.getMessage());

            }
        }).start();
    }
    
    @Override
    public void restartSolr(int timeToWait) {
        executeCommand(null, Comandos.RESTART_SOLR);
        while(!SolrServerImp.isSolrServerRunning()) {
                
        }
        if(timeToWait <= 0)
            timeToWait = 30000; //30 segs
        try {
                System.out.println("Esperando a que se termine de reiniciar Solr. Espere");
                Thread.sleep(timeToWait); // tiempo de espera para que cargue el servidor
            } catch (InterruptedException ex) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public static boolean isSolrServerRunning() {
        try {
            String solrUrl = "http://localhost:8983/solr";
            URL url = new URL(solrUrl); // Endpoint para verificar el estado
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000); // Tiempo de espera para la conexión
            connection.setReadTimeout(2000); // Tiempo de espera para la lectura

            int responseCode = connection.getResponseCode();
            return responseCode == 200; // 200 significa que el servidor está en funcionamiento
        } catch (IOException e) {
            //e.printStackTrace();
            return false; // Si hay una excepción, el servidor no está disponible
        }
    }

}
