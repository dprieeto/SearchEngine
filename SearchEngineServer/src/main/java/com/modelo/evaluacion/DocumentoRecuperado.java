package com.modelo.evaluacion;

/**
 *
 * @author Prieto
 */
public class DocumentoRecuperado {

    private final String numConsulta;

    private final String numDocumento;

    private final String rank;

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
