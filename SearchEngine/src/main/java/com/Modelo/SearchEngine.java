package com.Modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author Prieto
 */
public class SearchEngine {
    
    private String coreName;

    public SearchEngine(String coreName) {
        if(coreName == null)
            coreName = Constantes.NOMBRE_DEFAULT_COLECCION;
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

            SolrDocumentList docs = rsp.getResults();
            long encontrados = rsp.getResults().getNumFound();
            boolean flag = false;
            if(encontrados > 100)
                query.setRows(100);
            else
                query.setRows((int) encontrados);
            
            rsp = solr.query(query);
            int numDocs = (int) rsp.getResults().getNumFound();
            System.out.println("\n documentos encontrados en la consulta " + numDocs);
            docs = rsp.getResults();
            for(int i =0; i<numDocs; i++) {
                String indice = docs.get(i).getFieldValue("indice").toString();
                String texto = docs.get(i).getFieldValue("texto").toString();
                texto = transfomarCuerpoDocumento(texto);
                Documento d = new Documento(indice, texto);
                documentos.add(d);
            }
            //query.setRows(0);
            /*
            QueryResponse rsp = solr.query(query);

            SolrDocumentList docs = rsp.getResults();
            long encontrados = rsp.getResults().getNumFound();
            System.out.println("\nNumero consulta: " + indice + "\nConsulta: "
                    + consultaTransformada + "\nDocumentos encontrados: " + encontrados + "\n");
            for (int i = 0; i < docs.size(); ++i) {

                System.out.println(docs.get(i));
            }*/
            
            /*
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
                for (int i = 0; i<documents.size(); i++) { 
                    String index = indice;
                    String numDoc = documents.get(i).getFieldValue("indice").toString();
                    String rank = String.valueOf(i + 1 + start);
                    String scoreDoc = documents.get(i).getFieldValue("score").toString();
                    
                    //DocumentoRecuperado dr = new DocumentoRecuperado(index, numDoc, rank, scoreDoc);
                    //System.out.println(dr.toString());
                    //documentos.add(dr);
                }

                start += rows; // Incrementar el inicio para la siguiente página
            }
            //System.out.println("@@@@@@@@@@@@@@@@ \t" + documentos.size());
            
            //consultasResultados.put(Integer.valueOf(indice), documentos);
            */
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
        
        for(int i = 0; i<parts.length; i++) {
            sb.append(parts[i]);
             if (i < parts.length - 1) {
                sb.append(" ");
            }
            if((i+1)%12 == 0) //cada doce palabras
                sb.append("\n");
        }
        
        return sb.toString();
    }
    
    
    
}
