package com.modelo.evaluacion;

/**
 * Esta clase se utiliza para generar el archivo '.TREC'.
 * @author Prieto
 */
public class DocumentoRecuperado {

    /**
     * Numero de consulta
     */
    private final String numConsulta;

    /**
     * Numero(.I) de documento
     */
    private final String numDocumento;

    /**
     * Ranking en el que el documento se ha recuperado.
     */
    private final String rank;

    /**
     * Puntuacion obtenida en la recuperacion del documento.
     */
    private final String score;

    public DocumentoRecuperado(String numConsulta, String numDocumento, String rank, String score) {
        this.numConsulta = numConsulta;
        this.numDocumento = numDocumento;
        this.rank = rank;
        this.score = score;
    }

    public String getNumConsulta() {
        return numConsulta;
    }

    public String getScore() {
        return score;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public String getRank() {
        return rank;
    }

    /**
     * Formato en el que se guarda cada documento recuperado en el archivo 
     * '.TREC'.
     * @return 
     */
    @Override
    public String toString() {
        String s = "";
        /* 
        s += "1 Q0" + getNumConsulta() + " " + getNumDocumento() + " " + 
                getScore() + " Prieto";
         */
        s += getNumConsulta() + " Q0 " + getNumDocumento() + " " + rank 
                + " " + getScore() + " Prieto";
        return s;
    }

}
