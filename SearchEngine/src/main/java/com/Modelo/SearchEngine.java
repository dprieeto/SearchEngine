package com.Modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author Prieto
 */
public class SearchEngine {

    private String coreName;

    public SearchEngine(String coreName) {
        if (coreName == null) {
            coreName = Constantes.NOMBRE_DEFAULT_COLECCION;
        }
        this.coreName = coreName;
    }

    public String getCoreName() {
        return coreName;
    }

    public List<Documento> buscar(String consulta) {
        List<Documento> documentos = new ArrayList<>();
        try {
            String consultaTransformada = transformarTextoConsulta(consulta.trim());
            HttpSolrClient solr = new HttpSolrClient.Builder(Constantes.URL_SOLR + getCoreName()).build();

            SolrQuery query = new SolrQuery();
            //query.setQuery("texto:\""+ consulta + "\""); //busca la consulta como si fuera una frase
            query.setQuery("texto:" + consultaTransformada);
            query.setFields("indice", "texto");
            QueryResponse rsp = solr.query(query);

            long encontrados = rsp.getResults().getNumFound();
            int numDocsIt = 0;
            if (encontrados > 100) {
                numDocsIt = 100;
            } else {
                numDocsIt = (int) encontrados;
            }

            rsp = solr.query(query);
            //int numDocs = (int) rsp.getResults().getNumFound();
            //System.out.println("\n documentos encontrados en la consulta " + numDocs);
            SolrDocumentList docs = rsp.getResults();
            for (int i = 0; i < numDocsIt; i++) {
                String indice = docs.get(i).getFieldValue("indice").toString();
                String texto = docs.get(i).getFieldValue("texto").toString();
                texto = transfomarCuerpoDocumento(texto);
                Documento d = new Documento(indice, texto);
                documentos.add(d);
            }
        } catch (SolrServerException | IOException ex) {
            System.err.println("\nError al realizar la consulta.\n" + ex.getMessage());
        }
        return documentos;
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

    private String transfomarCuerpoDocumento(String texto) {
        StringBuilder sb = new StringBuilder();
        //texto = texto.trim();
        String[] parts = texto.split(" ");

        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < parts.length - 1) {
                sb.append(" ");
            }
            if ((i + 1) % 12 == 0) //cada doce palabras
            {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

}
