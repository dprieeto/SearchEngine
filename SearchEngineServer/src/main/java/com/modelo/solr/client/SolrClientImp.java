package com.modelo.solr.client;

import com.modelo.solr.Constantes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.solr.client.solrj.response.TermsResponse.Term;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;

/**
 *
 * @author Prieto
 */
public class SolrClientImp implements SolrClient {

    public SolrClientImp() {
    }

    @Override
    public void leerArchivoContenido(String nombreArchivo) {
        try {
            if (nombreArchivo == null || nombreArchivo.isBlank()) {
                nombreArchivo = Constantes.NOMBRE_ARCHIVO_CORPUS;
            }
            String carpAct = System.getProperty("user.dir");
            String fileName = carpAct + "\\src\\corpus\\" + nombreArchivo;
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
        HttpSolrClient solr = new HttpSolrClient.Builder(Constantes.URL_SOLR + Constantes.NOMBRE_DEFAULT_COLECCION).build();
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
        try {
            if (nombreArchivo == null || nombreArchivo.isBlank()) {
                nombreArchivo = Constantes.NOMBRE_ARCHIVO_CONSULTAS;
            }
            String carpAct = System.getProperty("user.dir");
            String fileName = carpAct + "\\src\\corpus\\" + nombreArchivo;
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
                        String texto = sb.toString();
                        String consulta = obtenerLasPrimeras5Palabras(texto);
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
                String texto = sb.toString().trim();
                String consulta = obtenerLasPrimeras5Palabras(texto);
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
            String consultaCorta = obtenerLasPrimeras5Palabras(consulta);
            HttpSolrClient solr = new HttpSolrClient.Builder(Constantes.URL_SOLR + Constantes.NOMBRE_DEFAULT_COLECCION).build();

            SolrQuery query = new SolrQuery();
            //query.setQuery("texto:\""+ consulta + "\""); //busca la consulta como si fuera una frase
            query.setQuery("texto:" + consultaCorta);

            query.setFields("indice", "score");
            query.setRows(20);

            QueryResponse rsp = solr.query(query);

            SolrDocumentList docs = rsp.getResults();
            long encontrados = rsp.getResults().getNumFound();
            System.out.println("\nNumero consulta: " + indice + "\nConsulta: "
                    + consultaCorta + "\nDocumentos encontrados: " + encontrados + "\n");
            for (int i = 0; i < docs.size(); ++i) {

                System.out.println(docs.get(i));
            }
        } catch (SolrServerException | IOException ex) {
            System.err.println("\nError al realizar la consulta.\n" + ex.getMessage());
        }

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

    @Override
    public void actualizarPalabrasVacias() {
        try {
            List<String> frequentTerms = getTopFrequentTerms();
            int maxNewStopWords = 50;
            List<String> newStopWords = frequentTerms.subList(0, maxNewStopWords - 1);
            String stopWords = String.join(",", newStopWords);

            String url = Constantes.URL_DEFAULT_COLLECTION;
            HttpSolrClient solr = new HttpSolrClient.Builder(url).build();

            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", "stopwords");
            doc.addField("value", stopWords);

            UpdateRequest update = new UpdateRequest();
            update.add(doc);
            update.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);

            solr.request(update);
            System.out.println("Palabras vacias actualizadas");
        } catch (SolrServerException | IOException ex) {
            Logger.getLogger(SolrClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }

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
            /*
            for (int i=0; i<50; i++) {
                System.out.println(i);
                System.out.println("Termino: " + terms.get(i).getTerm() + "\tFrecuencia: " + terms.get(i).getFrequency());
            }
             */
            for (Term t : terms) {
                frequentTerms.add(t.getTerm());
            }

        } catch (SolrServerException | IOException ex) {
            Logger.getLogger(SolrClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return frequentTerms;
    }

}
