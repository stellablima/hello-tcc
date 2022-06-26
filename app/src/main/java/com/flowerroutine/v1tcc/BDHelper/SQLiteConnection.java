package com.flowerroutine.v1tcc.BDHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteConnection extends SQLiteOpenHelper {

    private static SQLiteConnection INSTANCIA_CONEXAO;
    private static final String NOMEDB = "rotina";
    private static final int VERSAOBD = 7;

    public SQLiteConnection(Context context) {
        super(context, NOMEDB, null, VERSAOBD);
    }

    //https://www.youtube.com/watch?v=raYR3Mc6m2M
    public static SQLiteConnection getInstanciaConexao(Context context) {
        if (INSTANCIA_CONEXAO == null)
            INSTANCIA_CONEXAO = new SQLiteConnection(context);

        return INSTANCIA_CONEXAO;
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
//        sql = "ALTER TABLE RELATORIO ADD COLUMN OBSERVACAO TEXT;";
//        bd.execSQL(sql);
//
//        insereDadosNecessidadesOcorrencia(bd);

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
        sql = "CREATE TABLE IF NOT EXISTS PROCEDIMENTO (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CATEGORIA TEXT, " +
                "DATA_INICIO NUMERIC, " + //nao cabe no escopo
                "DATA_PREVISAO NUMERIC, " +
                "DEBITO_ESTOQUE TEXT, " + //nao cabe no escopo
                "DEBITO_FISIOLOGICO TEXT, " + //nao cabe no escopo
                "FLAG TEXT, " +
                "FLAG_FREQUENCIA TEXT, " +
                "FLAG_REPETICAO TEXT, " +
                "QTDDISPAROS TEXT, " +
                "NOME TEXT, " +
                "OBSERVACAO TEXT, " + //apenas tarefas usam por enquanto
                "TEMPO_PREVISAO NUMERIC " + //nao cabe no escopo
                ")";

        bd.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS RELATORIO (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "_id_PROCEDIMENTO NUMERIC, " +
                "CATEGORIA TEXT, " +
                "DATA_INICIO TEXT, " +
                "DATA_PREVISAO TEXT, " +//como o procedimento pode ser editavel, melhor replicar o dado
                "NOME TEXT, " +
                "OBSERVACAO TEXT " +
                //falta OBSERVACAO que poderia se chamar DESCRICAO nas duas tabelas talvez por ser tarefa tudo bem pegar por join...
                ")";

        bd.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS ESTADO (" +
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
        cv.put("CATEGORIA", "Destaque"); //"Alerta"/a categoria é como se fosse um agrupamento mesmo
        cv.put("TITULO", "Adicionar destaque");
        cv.put("FLAG", "1");//default ativo
        cv.put("TEXTO", "Clique em editar para adicionar");
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

    void insereDadosNecessidadesOcorrencia(SQLiteDatabase bd){

////PROCEDIMENTO: 0excluido,1ativo,2concluidoprocedimentounico(evolua pra alarme com expiração),3procedimentopararegistrosemalarme

        ContentValues cv = new ContentValues();

        cv.put("CATEGORIA", "Ocorrência");
        cv.put("DATA_INICIO", "Mock data");
        cv.put("NOME", "Dor de dente");
        cv.put("OBSERVACAO", "Dipirona 35Gotas e massagem");
        bd.insert("RELATORIO", null, cv);

        cv = new ContentValues();
        cv.put("CATEGORIA", "Necessidades Fisiológicas");
        cv.put("DATA_INICIO", "Mock data");
        cv.put("NOME", "Fezes Espontânea +---");
        cv.put("OBSERVACAO", "Muito mole");
        bd.insert("RELATORIO", null, cv);

        cv = new ContentValues();
        cv.put("CATEGORIA", "Necessidades Fisiológicas");
        cv.put("DATA_INICIO", "Mock data");
        cv.put("NOME", "Fezes Flit ++++ ");
        bd.insert("RELATORIO", null, cv);

        cv = new ContentValues();
        cv.put("CATEGORIA", "Necessidades Fisiológicas");
        cv.put("DATA_INICIO", "Mock data");
        cv.put("NOME", "Urina Sondagem 200ml");
        cv.put("OBSERVACAO", "Coloração escura");
        bd.insert("RELATORIO", null, cv);//procedimento sem alarme, clique concluir manual, grava em relatorio

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