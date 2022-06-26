package com.flowerroutine.v1tcc.models;

public class Estado {

    private long _id;
    private String TITULO;
    private String DATA_ULTIMA_OCORRENCIA;
    private String CATEGORIA;
    private String FLAG;
    private String TEXTO;

    public Estado() {
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getTITULO() {
        return TITULO;
    }

    public void setTITULO(String TITULO) {
        this.TITULO = TITULO;
    }

    public String getDATA_ULTIMA_OCORRENCIA() {
        return DATA_ULTIMA_OCORRENCIA;
    }

    public void setDATA_ULTIMA_OCORRENCIA(String DATA_ULTIMA_OCORRENCIA) {
        this.DATA_ULTIMA_OCORRENCIA = DATA_ULTIMA_OCORRENCIA;
    }

    public String getCATEGORIA() {
        return CATEGORIA;
    }

    public void setCATEGORIA(String CATEGORIA) {
        this.CATEGORIA = CATEGORIA;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }

    public String getTEXTO() {
        return TEXTO;
    }

    public void setTEXTO(String TEXTO) {
        this.TEXTO = TEXTO;
    }

    @Override
    public String toString() {
        return "Estado{" +
                "_id=" + _id +
                ", TITULO='" + TITULO + '\'' +
                ", DATA_ULTIMA_OCORRENCIA='" + DATA_ULTIMA_OCORRENCIA + '\'' +
                ", CATEGORIA='" + CATEGORIA + '\'' +
                ", FLAG='" + FLAG + '\'' +
                ", TEXTO='" + TEXTO + '\'' +
                '}';
    }
}
