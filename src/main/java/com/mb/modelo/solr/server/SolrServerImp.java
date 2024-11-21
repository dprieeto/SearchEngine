package com.mb.modelo.solr.server;

import com.mb.modelo.solr.Comandos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
    public void createCore() {
        executeCommand(null, Comandos.CREATE_CORE);
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

}
