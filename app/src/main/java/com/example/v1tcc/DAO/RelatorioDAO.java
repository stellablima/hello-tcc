package com.example.v1tcc.DAO;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.v1tcc.BDHelper.SQLiteConnection;
import com.example.v1tcc.models.Procedimento;
import com.example.v1tcc.models.Relatorio;

public class RelatorioDAO {

    private final SQLiteConnection sqlConnection;

    public RelatorioDAO(SQLiteConnection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

    public long createRelatorio(Relatorio relatorio){

        SQLiteDatabase db = sqlConnection.getWritableDatabase();

        try{

            ContentValues cv = new ContentValues();
            cv.put("_id_PROCEDIMENTO",relatorio.get_id_PROCEDIMENTO());
            cv.put("CATEGORIA", relatorio.getCATEGORIA());
            cv.put("DATA_INICIO",relatorio.getDATA_INICIO());
            cv.put("NOME",relatorio.getNOME());
            cv.put("OBSERVACAO",relatorio.getOBSERVACAO());

            long idRelatorio = db.insert("RELATORIO", null, cv);
            return idRelatorio;

        }catch (Exception e){

            e.printStackTrace();
        }
        return -1;
    }

}
