package com.example.v1tcc.DAO;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.v1tcc.BDHelper.SQLiteConnection;
import com.example.v1tcc.models.Procedimento;

public class ProcedimentoDAO {

    private final SQLiteConnection sqlConnection;


    public ProcedimentoDAO(SQLiteConnection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

    public long createProcedimento(Procedimento procedimento){

        SQLiteDatabase db = sqlConnection.getWritableDatabase();

        try{

            ContentValues cv = new ContentValues();
            cv.put("CATEGORIA", procedimento.getCATEGORIA());
            cv.put("DATA_PREVISAO",procedimento.getDATA_PREVISAO());
            cv.put("FLAG",procedimento.getFLAG());
            cv.put("FLAG_FREQUENCIA",procedimento.getFLAG_FREQUENCIA());
            cv.put("FLAG_REPETICAO",procedimento.getFLAG_REPETICAO());
            cv.put("QTDDISPAROS",procedimento.getQTDDISPAROS());
            cv.put("NOME",procedimento.getNOME());
            cv.put("OBSERVACAO",procedimento.getOBSERVACAO());

            long idProcedimento = db.insert("PROCEDIMENTO", null, cv);
            return idProcedimento;

        }catch (Exception e){

            e.printStackTrace();
        }
        return -1;

    }
}
