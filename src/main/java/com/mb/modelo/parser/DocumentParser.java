package com.mb.modelo.parser;

/**
 *
 * @author Prieto
 */
public interface DocumentParser {
    
    
    public void readCorpusFile(String nombre);
    
    public void parseCorpusFile(String nombre);
    
    public void deleteAllDocumentsFromCollection();
    
    
}
