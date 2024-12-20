package com.modelo.solr.client;

import com.modelo.evaluacion.DocumentoRecuperado;
import com.modelo.solr.Constantes;
import com.modelo.solr.server.CoreConf;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.solr.client.solrj.response.TermsResponse.Term;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author Prieto
 */
public class SolrClientImp implements SolrClient {

    private CoreConf core;

    private List<DocumentoRecuperado> documentos;

    private Map<Integer, List<DocumentoRecuperado>> consultasResultados;

    public SolrClientImp(CoreConf core) {
        this.core = core;
        documentos = new ArrayList<>();
        consultasResultados = new HashMap<>();
    }

    @Override
    public void leerArchivoContenido(String nombreArchivo) {
        try {
            if (nombreArchivo == null || nombreArchivo.isBlank()) {
                nombreArchivo = Constantes.NOMBRE_ARCHIVO_CORPUS;
            }
            String carpAct = System.getProperty("user.dir");
            //String fileName = carpAct + "\\src\\corpus\\" + nombreArchivo;
            String fileName = carpAct + Constantes.DOCS_RUTA + nombreArchivo;
            Scanner scan = new Scanner(new File(fileName));
            String index;
            index = null;
            StringBuilder sb = new StringBuilder();
            borrarTodosLosDocumentos();
            System.out.println("\nAnalizando documentos para su parseo, espere.");
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.startsWith(".I")) {
                    if (index != null) {
                        String texto = sb.toString();
                        indexaDocumento(index, texto);
                        sb.setLength(0); //reset
                    }
                    String[] parts = line.split(" ");
                    index = parts[1];
                    //System.out.println(index);
                } else if (line.startsWith(".W")) {
                    continue;
                } else {
                    String text = line.trim();
                    sb.append(text).append(" ");
                }
            }
            if (index != null) {
                indexaDocumento(index, sb.toString());
            }
            System.out.println("\nTodos los documentos fueron parseados e indexados correctamente.");
        } catch (FileNotFoundException ex) {
            System.err.print("\nError. Archivo no encontrado." + ex.getMessage());
        }
    }

    @Override
    public void indexaDocumento(String indice, String texto) {
        HttpSolrClient solr = new HttpSolrClient.Builder(Constantes.URL_SOLR + core.getCoreName()).build();
        //Create solr document
        SolrInputDocument doc = new SolrInputDocument();

        doc.addField("indice", indice);
        doc.addField("texto", texto);
        try {
            solr.add(doc);
            solr.commit();
        } catch (SolrServerException | IOException ex) {
            System.err.print("\nSe ha producido un error al indexar el documento.\n" + ex.getMessage());
        }
    }

    @Override
    public void borrarTodosLosDocumentos() {
        //Preparing the Solr client 
        String urlString = "http://localhost:8983/solr/" + Constantes.NOMBRE_DEFAULT_COLECCION;
        HttpSolrClient Solr = new HttpSolrClient.Builder(urlString).build();

        //Preparing the Solr document 
        //SolrInputDocument doc = new SolrInputDocument();
        try {
            //Deleting the documents from Solr
            Solr.deleteByQuery("*");
            Solr.commit();
        } catch (SolrServerException | IOException ex) {
            System.err.print("\nSe ha producido un error al intentar borrar los documentos de la coleccion.\n"
                    + ex.getMessage());
        }
        //Saving the document 
        System.out.println("Documents deleted");
    }

    @Override
    public void leerArchivoConsultas(String nombreArchivo) {
        System.out.println("Escaneando archivo de consultas. Espere");
        try {
            if (nombreArchivo == null || nombreArchivo.isBlank()) {
                nombreArchivo = Constantes.NOMBRE_ARCHIVO_CONSULTAS;
            }
            String carpAct = System.getProperty("user.dir");
            //String fileName = carpAct + "\\src\\corpus\\" + nombreArchivo;
            String fileName = carpAct + Constantes.DOCS_RUTA + nombreArchivo;
            Scanner scan = new Scanner(new File(fileName));
            String index;
            index = null;
            StringBuilder sb = new StringBuilder();
            //borrarTodosLosDocumentos();
            System.out.println("\nAnalizando documentos de consultas, espere.");
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.startsWith(".I")) {
                    if (index != null) {
                        String consulta = sb.toString();
                        //String consulta = obtenerLasPrimeras5Palabras(texto);
                        hacerConsulta(index, consulta);
                        sb.setLength(0); //reset
                    }
                    String[] parts = line.split(" ");
                    index = parts[1];
                    //System.out.println(index);
                } else if (line.startsWith(".W")) {
                    continue;
                } else {
                    String text = line.trim();
                    sb.append(text).append(" ");
                }
            }
            if (index != null && sb.length() > 0) {
                String consulta = sb.toString().trim();
                //String consulta = obtenerLasPrimeras5Palabras(texto);
                hacerConsulta(index, consulta);
            }
            //System.out.println("\nTodos los documentos fueron parseados e indexados correctamente.");
        } catch (FileNotFoundException ex) {
            System.err.print("\nError. Archivo no encontrado." + ex.getMessage());
        }
    }

    @Override
    public void hacerConsulta(String indice, String consulta) {
        try {
            //String consultaTransformada = obtenerLasPrimeras5Palabras(consulta);
            String consultaTransformada = transformarTextoConsulta(consulta);
            HttpSolrClient solr = new HttpSolrClient.Builder(Constantes.URL_SOLR + Constantes.NOMBRE_DEFAULT_COLECCION).build();

            SolrQuery query = new SolrQuery();
            //query.setQuery("texto:\""+ consulta + "\""); //busca la consulta como si fuera una frase
            query.setQuery("texto:" + consultaTransformada);
            query.setFields("indice", "score");
            int rows = 100; // Número de documentos por página
            int start = 0;
            long totalDocuments;
            QueryResponse countResponse = solr.query(query);
            totalDocuments = countResponse.getResults().getNumFound();

            System.out.println("####################### consulta " + indice);
            System.out.println("documentos encontrados: " + totalDocuments);
            System.out.println("######################");
            //documentos= new ArrayList<>();
            while (start < totalDocuments) {
                query.setStart(start);
                query.setRows(rows); // Establecer el número de documentos a recuperar

                QueryResponse response = solr.query(query);
                List<SolrDocument> documents = response.getResults();

                // Mostrar los documentos recuperados
                for (int i = 0; i < documents.size(); i++) {
                    String index = indice;
                    String numDoc = documents.get(i).getFieldValue("indice").toString();
                    String rank = String.valueOf(i + 1 + start);
                    String scoreDoc = documents.get(i).getFieldValue("score").toString();
                    DocumentoRecuperado dr = new DocumentoRecuperado(index, numDoc, rank, scoreDoc);
                    //System.out.println(dr.toString());
                    documentos.add(dr);
                }

                start += rows; // Incrementar el inicio para la siguiente página
            }
            //System.out.println("@@@@@@@@@@@@@@@@ \t" + documentos.size());
            consultasResultados.put(Integer.valueOf(indice), documentos);

        } catch (SolrServerException | IOException ex) {
            System.err.println("\nError al realizar la consulta.\n" + ex.getMessage());
        }

    }

    @Override
    public Map<Integer, List<DocumentoRecuperado>> getConsultasResultados() {
        return consultasResultados;
    }

    private String obtenerLasPrimeras5Palabras(String texto) {
        //expresion regular para eliminar caracteres inncesarios
        String regex = "[ .,;:!()-]+"; // el + indica que paparece 1 o mas veces

        StringBuilder sb = new StringBuilder();
        String palabras[] = texto.split(regex);

        for (int i = 0; i < Math.min(5, palabras.length); i++) {
            sb.append(palabras[i]);
            if (i < Math.min(5, palabras.length) - 1) {
                sb.append("+");
            }
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    private String transformarTextoConsulta(String texto) {
        String regex = "[ .,;:!()-]+"; // el + indica que paparece 1 o mas veces

        StringBuilder sb = new StringBuilder();
        String palabras[] = texto.split(regex);

        for (int i = 0; i < palabras.length; i++) {
            sb.append(palabras[i]);
            if (i < palabras.length - 1) {
                sb.append("+");
            }
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    @Override
    public void actualizarPalabrasVacias(String fileName) {
        StopWords stopWords = new StopWords(fileName);
        stopWords.actualizarPalabrasVacias();
    }

    /**
     * @see documentacion TermsComponentParamters
     * @return
     */
    private List<String> getTopFrequentTerms() {
        List<String> frequentTerms = new ArrayList<>();
        try {
            String url = Constantes.URL_DEFAULT_COLLECTION;
            String fieldName = Constantes.SCHEMA_TEXT_FIELD;
            String termsLimit = "-1"; // devuelve todos

            HttpSolrClient solr = new HttpSolrClient.Builder(url).build();

            SolrQuery query = new SolrQuery();
            query.setRequestHandler("/terms");
            // especifica el nombre del campo del que recuperar terminos
            query.set("terms.fl", fieldName); // ES FL de field, no f+uno
            query.set("terms.limit", termsLimit); // limite de terminos a recuperar

            QueryResponse respuesta = solr.query(query);

            //obtener la lista de terminos
            TermsResponse termsResponse = respuesta.getTermsResponse();
            List<Term> terms = termsResponse.getTerms(fieldName);

            for (Term t : terms) {
                frequentTerms.add(t.getTerm());
            }
            System.out.println("\nNº de terminos frecuentes: " + terms.size());
        } catch (SolrServerException | IOException ex) {
            Logger.getLogger(SolrClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return frequentTerms;
    }

}
