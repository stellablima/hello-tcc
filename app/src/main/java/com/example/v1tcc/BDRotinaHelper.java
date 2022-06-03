package com.example.v1tcc;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;

public class BDRotinaHelper extends SQLiteOpenHelper {

    private static final String NOMEDB = "rotina";
    private static final int VERSAOBD = 6;

    public BDRotinaHelper(Context context) {
        super(context, NOMEDB, null, VERSAOBD);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        criaBdRotina(bd);
        insereDadosAlertaDia(bd);
        //insereDadosTarefas(bd);
        //insereDadosProcedimento(bd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int i, int i1) {
//        String sql = "";
//        sql = "ALTER TABLE ESTADO ADD COLUMN DATA_ULTIMA_OCORRENCIA TEXT;";
//        bd.execSQL(sql);

//        switch (i){
//            case 1://anterior(i) atual(i1)
//                sql = "ALTER TABLE PROCEDIMENTO ADD COLUMN QTDDISPAROS TEXT;";
//                bd.execSQL(sql);
//                break;
//            case 2:
//                sql= "ALTER TABLE PROCEDIMENTO ADD COLUMN FLAG_REPETICAO TEXT;";
//                bd.execSQL(sql);
//                sql= "ALTER TABLE PROCEDIMENTO ADD COLUMN FLAG_FREQUENCIA TEXT;";
//                bd.execSQL(sql);
//                break;
/*
tutorial update
acrescentar switch > compilar
acrescentar numero > compilar // instalar como 2, upar 3
*/
        //}

//        sql = "CREATE TABLE RELATORIO (" +
//                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "_id_PROCEDIMENTO NUMERIC, " +
//                "CATEGORIA TEXT, " +
//                "DATA_INICIO TEXT, " +
//                "DATA_PREVISAO TEXT, " +//como o procedimento pode ser editavel, melhor replicar o dado
//                "NOME TEXT " +
//                ")";
//
//        bd.execSQL(sql);
//
//        sql = "CREATE TABLE ESTADO (" +
//                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "TITULO TEXT, " +
//                "CATEGORIA TEXT, " +
//                "FLAG TEXT, " +
//                "TEXTO TEXT " +
//                ")";
//
//        bd.execSQL(sql);
//
//        insereDadosAlertaDia(bd);
//        insereDadosTarefas(bd);
    }

    private void criaBdRotina(SQLiteDatabase bd) {
        String sql;
        sql = "CREATE TABLE PROCEDIMENTO (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CATEGORIA TEXT, " +
                "DATA_INICIO NUMERIC, " +
                "DATA_PREVISAO NUMERIC, " +
                "DEBITO_ESTOQUE TEXT, " +
                "DEBITO_FISIOLOGICO TEXT, " +
                "FLAG TEXT, " +
                "FLAG_FREQUENCIA TEXT, " + //SOCRR??
                "FLAG_REPETICAO TEXT, " + //SOCRR??
                "QTDDISPAROS TEXT, " + //SOCRR??
                "NOME TEXT, " +
                "OBSERVACAO TEXT, " +
                "TEMPO_PREVISAO NUMERIC " +
                ")";

        bd.execSQL(sql);

        sql = "CREATE TABLE RELATORIO (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "_id_PROCEDIMENTO NUMERIC, " +
                "CATEGORIA TEXT, " +
                "DATA_INICIO TEXT, " +
                "DATA_PREVISAO TEXT, " +//como o procedimento pode ser editavel, melhor replicar o dado
                "NOME TEXT " +
                //falta OBSERVACAO que poderia se chamar DESCRICAO nas duas tabelas talvez por ser tarefa tudo bem pegar por join...
                ")";

        bd.execSQL(sql);

        sql = "CREATE TABLE ESTADO (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TITULO TEXT, " +
                "DATA_ULTIMA_OCORRENCIA TEXT," +
                "CATEGORIA TEXT, " +
                "FLAG TEXT, " +
                "TEXTO TEXT " +
                ")";

        bd.execSQL(sql);
    }

     void insereDadosAlertaDia(SQLiteDatabase bd){
        ContentValues cv = new ContentValues();
        cv.put("CATEGORIA", "Alerta"); //a categoria é como se fosse um agrupamento mesmo
        cv.put("TITULO", "Clique para adicionar");
        cv.put("FLAG", "1");//default ativo
        cv.put("TEXTO", "Verificar a real necessidade de existtir um alerta na tela principal, e se isso seria manual, verificar a viabilidade e uso disso no app, pode edixar na verdade e na semana teste uso ver s situaçãoo e o uso disso, ao meu ver talvez seria manual nao sei \n melhore revisao o escopo, lorem ipsun loren ispsuj lotens ispsum");
        bd.insert("ESTADO", null, cv);
    }

    static void insereDadosInstrucaoDia(SQLiteDatabase bd){
        ContentValues cv = new ContentValues();
        cv.put("CATEGORIA", "Instrucao"); //a categoria é como se fosse um agrupamento mesmo
        cv.put("TITULO", "Protocolo de saída");
        cv.put("FLAG", "1");//default ativo
        cv.put("TEXTO", "Texto enorme de uma instrucao aleatoria aqui\n\n1-Equipamentos a levar\n*espaço morto\nblablabla");
        bd.insert("ESTADO", null, cv);
    }

     void insereDadosTarefas(SQLiteDatabase bd){
        ContentValues cv = new ContentValues();
        cv.put("CATEGORIA", "Tarefa");
        cv.put("FLAG", "3");
        //cv.put("TEXTO", "Verificar a real necessidade de existtir um alerta na tela principal, e se isso seria manual, verificar a viabilidade e uso disso no app, pode edixar na verdade e na semana teste uso ver s situaçãoo e o uso disso, ao meu ver talvez seria manual nao sei \n melhore revisao o escopo, lorem ipsun loren ispsuj lotens ispsum");
        cv.put("NOME", "Tarefa Mockada0");
        cv.put("OBSERVACAO", "Observacao Tarefa Mockada0");
        bd.insert("PROCEDIMENTO", null, cv);//procedimento sem alarme, clique concluir manual, grava em relatorio

        cv = new ContentValues();
        cv.put("CATEGORIA", "Tarefa");
        cv.put("FLAG", "3");
        //cv.put("TEXTO", "Verificar a real necessidade de existtir um alerta na tela principal, e se isso seria manual, verificar a viabilidade e uso disso no app, pode edixar na verdade e na semana teste uso ver s situaçãoo e o uso disso, ao meu ver talvez seria manual nao sei \n melhore revisao o escopo, lorem ipsun loren ispsuj lotens ispsum");
        cv.put("NOME", "Tarefa Mockada1");
        cv.put("OBSERVACAO", "Observacao Tarefa Mockada1");
        bd.insert("PROCEDIMENTO", null, cv);//procedimento sem alarme, clique concluir manual, grava em relatorio

        cv = new ContentValues();
        cv.put("CATEGORIA", "Tarefa");
        cv.put("FLAG", "3");
        //cv.put("TEXTO", "Verificar a real necessidade de existtir um alerta na tela principal, e se isso seria manual, verificar a viabilidade e uso disso no app, pode edixar na verdade e na semana teste uso ver s situaçãoo e o uso disso, ao meu ver talvez seria manual nao sei \n melhore revisao o escopo, lorem ipsun loren ispsuj lotens ispsum");
        cv.put("NOME", "Tarefa Mockada2");
        cv.put("OBSERVACAO", "Observacao Tarefa Mockada2");
        bd.insert("PROCEDIMENTO", null, cv);//procedimento sem alarme, clique concluir manual, grava em relatorio
    }

    private void insereDadosProcedimento(SQLiteDatabase bd) {
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "12:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "1", "1","Losec", "Mussarela", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "23:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "1","1", "Dieta", "Mostarda Tradicional", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "02:20", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "1","1", "Flit", "Leite Condensado", "2021-01-01 00:00:00");
//        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "05:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "1","1", "Inalação", "Requeijão bisnaga", "2021-01-01 00:00:00");
//        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "10:30", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "1","1", "Aeropuff", "Creme de Leite", "2021-01-01 00:00:00");
//        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "13:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "1","1", "Sondagem", "Queijo Parmesão Nacional", "2021-01-01 00:00:00");
//        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "14:30", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "1","1", "Troca de fralda", "Presunto Cozido", "2021-01-01 00:00:00");
//        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "1", "Nome Editavel", "Queijo Parmesão Argentino", "2021-01-01 00:00:00");
//        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "1", "Nome Editavel", "Peito de Peru", "2021-01-01 00:00:00");
//        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "1", "Nome Editavel", "Azeitona Verde com caroço", "2021-01-01 00:00:00");
//        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "1", "Nome Editavel", "Requeijão copo", "2021-01-01 00:00:00");
//        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "1", "Nome Editavel", "Azeite de Oliva Premium", "2021-01-01 00:00:00");
    };
    private void insereProcedimento(SQLiteDatabase bd, String categoria, String dataInicio, String dataPrevisao,
                                    String debitoEstoque, String debitoFisiologico, String qtdDisparos, String flag, String nome,
                                    String observacao, String tempoPrevisao) {
        ContentValues cvProc = new ContentValues();
        cvProc.put("CATEGORIA", categoria);
        cvProc.put("DATA_INICIO", dataInicio);
        cvProc.put("DATA_PREVISAO", dataPrevisao);
        cvProc.put("DEBITO_ESTOQUE", debitoEstoque);
        cvProc.put("QTDDISPAROS", qtdDisparos);
        cvProc.put("DEBITO_FISIOLOGICO", debitoFisiologico);
        cvProc.put("FLAG", flag);
        cvProc.put("NOME", nome);
        cvProc.put("OBSERVACAO", observacao);
        cvProc.put("TEMPO_PREVISAO", tempoPrevisao);
        bd.insert("PROCEDIMENTO", null, cvProc);
    }
}
/*
 * a tabela tupla deve ter quantas casas decimais deve se dividir o numero inteiro ou consultar tabela de tuplas
 * */