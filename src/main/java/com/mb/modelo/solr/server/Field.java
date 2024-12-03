package com.mb.modelo.solr.server;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Prieto
 */
public class Field {

    private Map<String, Object> propiedades;

    public Field() {
        propiedades = new HashMap<>();
    }

    public Field(String nombre, String tipo) {
        propiedades = new HashMap<>();
        propiedades.put("name", nombre);
        propiedades.put("type", tipo);
        propiedades.put("stored", true);
        propiedades.put("indexed", true);
        propiedades.put("uninvertible", true);
        propiedades.put("multiValued", false);
        propiedades.put("required", false);
    }

    public Map<String, Object> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(Map<String, Object> propiedades) {
        this.propiedades = propiedades;
    }

    public static Map<String, Object> getDefaultField(String nombre, String tipo) {
        Map<String, Object> propiedadesCampo = new HashMap<>();
        propiedadesCampo.put("name", nombre);
        propiedadesCampo.put("type", tipo);
        propiedadesCampo.put("stored", true);
        propiedadesCampo.put("indexed", true);
        propiedadesCampo.put("uninvertible", true);
        propiedadesCampo.put("multiValued", false);
        propiedadesCampo.put("required", false);
        return propiedadesCampo;
    }

    public Object getKeyValue(String clave) {
        return propiedades.get(clave);
    }

    public void setName(String valor) {
        propiedades.put("name", valor);
    }

    public void setType(String type) {
        propiedades.put("type", type);
    }

    public void setStored(boolean valor) {
        propiedades.put("stored", valor);
    }

    public void setIndexed(boolean valor) {
        propiedades.put("indexed", valor);
    }

    public void setUninvertible(boolean valor) {
        propiedades.put("uninvertible", valor);
    }

    public void setMultiValued(boolean valor) {
        propiedades.put("multiValued", valor);
    }

    public void setRequired(boolean valor) {
        propiedades.put("required", valor);
    }

    public void setOmitNorms(boolean valor) {
        propiedades.put("omitNorms", valor);
    }

    public void setOmitTermFreqPositions(boolean valor) {
        propiedades.put("omitTermFreqAndPositions", valor);
    }

    public void setOmitPositions(boolean valor) {
        propiedades.put("omitPositions", valor);
    }

    public void setTermVectors(boolean valor) {
        propiedades.put("termVectors", valor);
    }

    public void setTermPositions(boolean valor) {
        propiedades.put("termPositions", valor);
    }

    public void setTermOffsets(boolean valor) {
        propiedades.put("termOffsets", valor);
    }

    public void setTermPayloads(boolean valor) {
        propiedades.put("termPayloads", valor);
    }

    public void setSortMissingFirst(boolean valor) {
        propiedades.put("sortMissingFirst", valor);
    }

    public void setSortMissingLast(boolean valor) {
        propiedades.put("sortMissingLast", valor);
    }

}
