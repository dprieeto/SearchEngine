package com.mb.modelo.parser;

import com.mb.modelo.solr.client.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Prieto
 */
public class DocumentParserImp  implements DocumentParser{

    
    
    
    
    private SolrClient cliente;


    public DocumentParserImp() {
        cliente = new SolrClientImp();
        //estado = Estado.inicial;
    }
    
    
    
    @Override
    public void readCorpusFile(String nombre) {
        
    }

    @Override
    public void parseCorpusFile(String nombre) {
        
    }

    @Override
    public void deleteAllDocumentsFromCollection() {
        
    }
}
