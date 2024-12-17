package com.modelo.solr.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase representa un campo/Filed del Schema de Solr.
 * @author Prieto
 * @see https://solr.apache.org/guide/8_10/defining-fields.html
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

    /**
     * Configuracion por defecto.
     * @param nombre
     * @param tipo
     * @return 
     */
    public static Map<String, Object> getDefaultField(String nombre, String tipo) {
        Map<String, Object> propiedadesCampo = new HashMap<>();
        propiedadesCampo.put("name", nombre);
        propiedadesCampo.put("type", tipo);
        propiedadesCampo.put("stored", true);
        propiedadesCampo.put("indexed", true);
        propiedadesCampo.put("uninvertible", true);
        propiedadesCampo.put("multiValued", false);
        propiedadesCampo.put("required", false);
        //propiedadesCampo.put("omitTermFreqAndPositions", true);
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

    /**
     * 	If true, the actual value of the field can be retrieved by queries.
     * @param valor 
     */
    public void setStored(boolean valor) {
        propiedades.put("stored", valor);
    }

    /**
     * 	If true, the value of the field can be used in queries to retrieve 
     * matching documents.
     * @param valor 
     */
    public void setIndexed(boolean valor) {
        propiedades.put("indexed", valor);
    }

    /**
     * If true, indicates that an indexed="true" docValues="false" field can 
     * be "un-inverted" at query time to build up large in memory data
     * structure to serve in place of DocValues. Defaults to true for
     * historical reasons, but users are strongly encouraged to set this to 
     * false for stability and use docValues="true" as needed.
     * @param valor 
     */
    public void setUninvertible(boolean valor) {
        propiedades.put("uninvertible", valor);
    }

    /**
     * 	If true, indicates that a single document might contain multiple 
     * values for this field type.
     * @param valor 
     */
    public void setMultiValued(boolean valor) {
        propiedades.put("multiValued", valor);
    }

    public void setRequired(boolean valor) {
        propiedades.put("required", valor);
    }

    /**
     * If true, omits the norms associated with this field (this disables 
     * length normalization for the field, and saves some memory). Defaults to 
     * true for all primitive (non-analyzed) field types, such as int, float, 
     * data, bool, and string. Only full-text fields or fields need norms.
     * @param valor 
     */
    public void setOmitNorms(boolean valor) {
        propiedades.put("omitNorms", valor);
    }

    /**
     * If true, omits term frequency, positions, and payloads from postings 
     * for this field. This can be a performance boost for fields that don’t 
     * require that information. It also reduces the storage space required for
     * the index. Queries that rely on position that are issued on a field 
     * with this option will silently fail to find documents. This property 
     * defaults to true for all field types that are not text fields.
     * @param valor 
     */
    public void setOmitTermFreqPositions(boolean valor) {
        propiedades.put("omitTermFreqAndPositions", valor);
    }

    /**
     * 	Similar to omitTermFreqAndPositions but preserves term frequency 
     * information.
     * @param valor 
     */
    public void setOmitPositions(boolean valor) {
        propiedades.put("omitPositions", valor);
    }

    /**
     * These options instruct Solr to maintain full term vectors for 
     * each document, optionally including position, offset and payload 
     * information for each term occurrence in those vectors. These can be 
     * used to accelerate highlighting and other ancillary functionality, 
     * but impose a substantial cost in terms of index size. They are not 
     * necessary for typical uses of Solr.
     * @param valor 
     */
    public void setTermVectors(boolean valor) {
        propiedades.put("termVectors", valor);
    }

    /**
     * These options instruct Solr to maintain full term vectors for 
     * each document, optionally including position, offset and payload 
     * information for each term occurrence in those vectors. These can be 
     * used to accelerate highlighting and other ancillary functionality, 
     * but impose a substantial cost in terms of index size. They are not 
     * necessary for typical uses of Solr.
     * @param valor 
     */
    public void setTermPositions(boolean valor) {
        propiedades.put("termPositions", valor);
    }

    /**
     * These options instruct Solr to maintain full term vectors for 
     * each document, optionally including position, offset and payload 
     * information for each term occurrence in those vectors. These can be 
     * used to accelerate highlighting and other ancillary functionality, 
     * but impose a substantial cost in terms of index size. They are not 
     * necessary for typical uses of Solr.
     * @param valor 
     */
    public void setTermOffsets(boolean valor) {
        propiedades.put("termOffsets", valor);
    }

    /**
     * These options instruct Solr to maintain full term vectors for 
     * each document, optionally including position, offset and payload 
     * information for each term occurrence in those vectors. These can be 
     * used to accelerate highlighting and other ancillary functionality, 
     * but impose a substantial cost in terms of index size. They are not 
     * necessary for typical uses of Solr.
     * @param valor 
     */
    public void setTermPayloads(boolean valor) {
        propiedades.put("termPayloads", valor);
    }

    /**
     * Control the placement of documents when a sort field is not present.
     * @param valor 
     */
    public void setSortMissingFirst(boolean valor) {
        propiedades.put("sortMissingFirst", valor);
    }

    /**
     * Control the placement of documents when a sort field is not present.
     * @param valor 
     */
    public void setSortMissingLast(boolean valor) {
        propiedades.put("sortMissingLast", valor);
    }

}
