package com.mb.modelo.solr.client;

import com.mb.modelo.solr.Constantes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

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
            String index, text;
            index = text = null;
            StringBuilder sb = new StringBuilder();
            borrarTodosLosDocumentos();

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
                    text = line.trim();
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
        HttpSolrClient solr = new HttpSolrClient.Builder(Constantes.URL_COLLECTION + Constantes.NOMBRE_COLECCION).build();
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
        String urlString = "http://localhost:8983/solr/" + Constantes.NOMBRE_COLECCION;
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
            String index, text, consulta;
            index = text = consulta = null;
            StringBuilder sb = new StringBuilder();
            //borrarTodosLosDocumentos();

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.startsWith(".I")) {
                    if (index != null) {
                        String texto = sb.toString();
                        consulta = obtenerLasPrimeras5Palabras(texto);
                        hacerConsulta(index, consulta);
                        sb.setLength(0); //reset
                        //break;
                    }
                    String[] parts = line.split(" ");
                    index = parts[1];
                    //System.out.println(index);
                } else if (line.startsWith(".W")) {
                    continue;
                } else {
                    text = line.trim();
                    sb.append(text).append(" ");
                }
            }
            if (index != null) {
                hacerConsulta(index, consulta);
                //indexaDocumento(index, sb.toString());
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
            HttpSolrClient solr = new HttpSolrClient.Builder(Constantes.URL_COLLECTION+Constantes.NOMBRE_COLECCION).build();
            
            SolrQuery query = new SolrQuery();
            //query.setQuery("texto:\""+ consulta + "\""); //busca la consulta como si fuera una frase
            query.setQuery("texto:" + consultaCorta);
            query.setRows(20);
            
            //query.addFilterQuery("cat:electronics");
            //query.setFields("texto");
            QueryResponse rsp = solr.query(query);
            SolrDocumentList docs = rsp.getResults();
            System.out.println("\nNumero consulta: " + indice + "\n");
            for (int i = 0; i < docs.size(); ++i) {
                System.out.println(docs.get(i));
            }
        } catch (SolrServerException ex) {
            Logger.getLogger(SolrClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SolrClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private String obtenerLasPrimeras5Palabras(String texto) {
        String regex = "[ .,;:!()]+"; //expresion regular para omitir esos caracteres en la palabra
        StringBuilder sb = new StringBuilder();
        String palabras[] = texto.split(regex);
        
        for(int i = 0; i<Math.min(5, palabras.length); i++) {
            sb.append(palabras[i]).append("+"); //busca las palabras pero no en el mismo orden
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

}
