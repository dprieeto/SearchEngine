package com.Modelo;

/**
 *
 * @author Prieto
 */
public class Documento {
    
    private final String indice;
    
    private final String texto;

    public Documento(String indice, String texto) {
        this.indice = indice;
        this.texto = texto;
    }

    public String getIndice() {
        return indice;
    }

    public String getTexto() {
        return texto;
    }  
    
}
