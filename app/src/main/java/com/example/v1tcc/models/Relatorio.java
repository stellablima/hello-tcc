package com.example.v1tcc.models;

public class Relatorio {

    private long _id;
    private long _id_PROCEDIMENTO;
    private String CATEGORIA;
    private String DATA_INICIO;
    //"DATA_PREVISAO TEXT, " +//como o procedimento pode ser editavel, melhor replicar o dado
    private String NOME;
    private String OBSERVACAO;

    public Relatorio() {
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_id_PROCEDIMENTO() {
        return _id_PROCEDIMENTO;
    }

    public void set_id_PROCEDIMENTO(long _id_PROCEDIMENTO) {
        this._id_PROCEDIMENTO = _id_PROCEDIMENTO;
    }

    public String getCATEGORIA() {
        return CATEGORIA;
    }

    public void setCATEGORIA(String CATEGORIA) {
        this.CATEGORIA = CATEGORIA;
    }

    public String getDATA_INICIO() {
        return DATA_INICIO;
    }

    public void setDATA_INICIO(String DATA_INICIO) {
        this.DATA_INICIO = DATA_INICIO;
    }

    public String getNOME() {
        return NOME;
    }

    public void setNOME(String NOME) {
        this.NOME = NOME;
    }

    public String getOBSERVACAO() {
        return OBSERVACAO;
    }

    public void setOBSERVACAO(String OBSERVACAO) {
        this.OBSERVACAO = OBSERVACAO;
    }

    @Override
    public String toString() {
        return "Relatorio{" +
                "_id=" + _id +
                ", _id_PROCEDIMENTO=" + _id_PROCEDIMENTO +
                ", CATEGORIA='" + CATEGORIA + '\'' +
                ", DATA_INICIO='" + DATA_INICIO + '\'' +
                ", NOME='" + NOME + '\'' +
                ", OBSERVACAO='" + OBSERVACAO + '\'' +
                '}';
    }
}
