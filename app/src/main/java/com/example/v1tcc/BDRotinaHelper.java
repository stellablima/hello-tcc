package com.example.v1tcc;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;

public class BDRotinaHelper extends SQLiteOpenHelper {

    private static final String NOMEDB = "rotina";
    private static final int VERSAOBD = 1;

    public BDRotinaHelper(Context context) {
        super(context, NOMEDB, null, VERSAOBD);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        criaBdRotina(bd);
        insereDadosProcedimento(bd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

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
                "NOME TEXT, " +
                "OBSERVACAO TEXT, " +
                "TEMPO_PREVISAO NUMERIC " +
                ")";
        bd.execSQL(sql);
    }
    private void insereDadosProcedimento(SQLiteDatabase bd) {
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "Nome Editavel", "Mussarela", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "Nome Editavel", "Mostarda Tradicional", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "Nome Editavel", "Leite Condensado", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "Nome Editavel", "Requeijão bisnaga", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "Nome Editavel", "Creme de Leite", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "Nome Editavel", "Queijo Parmesão Nacional", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "Nome Editavel", "Presunto Cozido", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "Nome Editavel", "Queijo Parmesão Argentino", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "Nome Editavel", "Peito de Peru", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "Nome Editavel", "Azeitona Verde com caroço", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "Nome Editavel", "Requeijão copo", "2021-01-01 00:00:00");
        insereProcedimento(bd, "Medicação", "2021-01-01 00:00:00", "2021-01-01 00:00:00", "4u8r_-1_un|cd09_-3_ml|jki5_-2_pc", "34e3_-350_ml|ks94_100_kg", "Nome Editavel", "Azeite de Oliva Premium", "2021-01-01 00:00:00");
    };
    private void insereProcedimento(SQLiteDatabase bd, String categoria, String dataInicio, String dataPrevisao,
                                    String debitoEstoque, String debitoFisiologico, String nome,
                                    String observacao, String tempoPrevisao) {
        ContentValues cvProc = new ContentValues();
        cvProc.put("CATEGORIA", categoria);
        cvProc.put("DATA_INICIO", dataInicio);
        cvProc.put("DATA_PREVISAO", dataPrevisao);
        cvProc.put("DEBITO_ESTOQUE", debitoEstoque);
        cvProc.put("DEBITO_FISIOLOGICO", debitoFisiologico);
        cvProc.put("NOME", nome);
        cvProc.put("OBSERVACAO", observacao);
        cvProc.put("TEMPO_PREVISAO", tempoPrevisao);
        bd.insert("PROCEDIMENTO", null, cvProc);
    }
}
/*
 * a tabela tupla deve ter quantas casas decimais deve se dividir o numero inteiro ou consultar tabela de tuplas
 * */