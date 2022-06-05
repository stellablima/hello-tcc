package com.example.v1tcc.models;

/*Esta classe corresponde a procedimentos equivalentes a disparos
* e contempla a classe embutida tarefas, a qual nao tem disparos*/

public class Procedimento {
    private long _id;
    private String CATEGORIA;
    //DATA_INICIO
    private String DATA_PREVISAO; //NUMERIC
    //DEBITO_ESTOQUE
    //DEBITO_FISIOLOGICO
    private String FLAG;
    private String FLAG_FREQUENCIA;
    private String FLAG_REPETICAO;
    private String QTDDISPAROS;
    private String NOME;
    private String OBSERVACAO; //apenas tarefas usam por enquanto
    //TEMPO_PREVISAO
    //private int wx;
    //private double preeco;


    public Procedimento() {
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getCATEGORIA() {
        return CATEGORIA;
    }

    public void setCATEGORIA(String CATEGORIA) {
        this.CATEGORIA = CATEGORIA;
    }

    public String getDATA_PREVISAO() {
        return DATA_PREVISAO;
    }

    public void setDATA_PREVISAO(String DATA_PREVISAO) {
        this.DATA_PREVISAO = DATA_PREVISAO;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }

    public String getFLAG_FREQUENCIA() {
        return FLAG_FREQUENCIA;
    }

    public void setFLAG_FREQUENCIA(String FLAG_FREQUENCIA) {
        this.FLAG_FREQUENCIA = FLAG_FREQUENCIA;
    }

    public String getFLAG_REPETICAO() {
        return FLAG_REPETICAO;
    }

    public void setFLAG_REPETICAO(String FLAG_REPETICAO) {
        this.FLAG_REPETICAO = FLAG_REPETICAO;
    }

    public String getQTDDISPAROS() {
        return QTDDISPAROS;
    }

    public void setQTDDISPAROS(String QTDDISPAROS) {
        this.QTDDISPAROS = QTDDISPAROS;
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
        return "Procedimento{" +
                "_id=" + _id +
                ", CATEGORIA='" + CATEGORIA + '\'' +
                ", DATA_PREVISAO='" + DATA_PREVISAO + '\'' +
                ", FLAG='" + FLAG + '\'' +
                ", FLAG_FREQUENCIA='" + FLAG_FREQUENCIA + '\'' +
                ", FLAG_REPETICAO='" + FLAG_REPETICAO + '\'' +
                ", QTDDISPAROS='" + QTDDISPAROS + '\'' +
                ", NOME='" + NOME + '\'' +
                ", OBSERVACAO='" + OBSERVACAO + '\'' +
                '}';
    }
}
