package com.modelo.evaluacion;

import com.modelo.solr.Comandos;
import com.modelo.solr.Constantes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @see
 * https://faculty.washington.edu/levow/courses/ling573_SPR2011/hw/trec_eval_desc.htm
 * @author Prieto
 */
public class Evaluacion {

    private Process process;

    private String stopwordsFile;

    private String nameRelFile; //nombre del rel trec a generar

    private String trecevalResultFileName; //fichero generado por treceval

    private Map<Integer, List<DocumentoRecuperado>> consultasRecuperadas;

    public Evaluacion(String stopwords) {
        process = null;
        stopwordsFile = stopwords;
        trecevalResultFileName = null;
        nameRelFile = null;
        consultasRecuperadas = new HashMap<>();
    }

    public void crearDocumentoEvaluacion(Map<Integer, List<DocumentoRecuperado>> consultas) {
        consultasRecuperadas = consultas;
        // ruta donde se crea
        String carpAct = System.getProperty("user.dir");
        String ruta = Constantes.EVALUATION_PATH;
        String path = carpAct + ruta;
        String id = getPartName().toUpperCase(); // id del archivo de evaluacion
        trecevalResultFileName = "ev_" + id + ".txt";
        nameRelFile = "MED_REL_" + id + ".TREC"; //nombre dle archivo
        BufferedWriter br = null;

        try {
            File archivo = new File(path + nameRelFile);

            br = new BufferedWriter(new FileWriter(archivo));
            for (int i = 0; i < consultasRecuperadas.size(); i++) {
                List<DocumentoRecuperado> docs = consultasRecuperadas.get(i + 1);
                for (int j = 0; j < docs.size(); j++) {
                    //StringBuilder sb = new StringBuilder();
                    br.write(docs.get(j).toString() + "\n");
                }

            }
            System.out.println("Documento de trec " + nameRelFile + " creado");
        } catch (IOException ex) {
            Logger.getLogger(Evaluacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void evaluar() {
        String carpAct = System.getProperty("user.dir");
        String ruta = Constantes.EVALUATION_PATH;// + "\\resultados\\";
        String path = carpAct + ruta;

        // omitir -q -a?
        String trecevalOpciones = " -q -a " + path;
        String trecevalArgumentos = "MED_REL.TREC " + nameRelFile;
        // ruta donde se guardan los resultados
        String trecevalGuardar = " > " + path + "\\resultados\\" + this.trecevalResultFileName;
        String comando = Constantes.TRECEVAL + trecevalOpciones
                + trecevalArgumentos + trecevalGuardar;

        eliminarDocumento();
        executeCommand(path, comando);
        System.out.println("Documento de evaluacion creado " + trecevalResultFileName);
    }

    private void eliminarDocumento() {
        String carpAct = System.getProperty("user.dir");
        String ruta = Constantes.EVALUATION_PATH;
        String path = carpAct + ruta + "resultados\\";
        String fileName = trecevalResultFileName;
        File archivo = new File(path + fileName);

        if (archivo.delete()) {
            System.out.println("Archivo eliminado " + fileName);
        } else {
            System.err.println("Error al eliminar el archivo " + fileName);
        }
    }

    private synchronized void executeCommand(String ruta, String comando) {
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

    private String getPartName() {
        String name = stopwordsFile.split("\\.")[0];
        String id = name.split("-")[1];
        //System.out.println("@@@@@@@@@@@@@@     " + id);
        return id;
    }
}
