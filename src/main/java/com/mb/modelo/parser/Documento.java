package com.mb.modelo.parser;

/**
 *
 * @author Prieto
 */
public class Documento {

    private String indice;

    private String texto;

    public Documento(String indice, String texto) {
        this.indice = indice;
        this.texto = texto;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

}
