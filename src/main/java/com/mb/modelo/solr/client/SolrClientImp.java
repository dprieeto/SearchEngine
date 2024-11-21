package com.mb.modelo.solr.client;

import com.mb.modelo.solr.Constantes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author Prieto
 */
public class SolrClientImp implements SolrClient {

    public SolrClientImp() {
    }

    @Override
    public void leerArchivoDelCorpus(String nombreArchivo) {
        try {
            if (nombreArchivo == null) {
                nombreArchivo = "MED2.ALL";
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

}
