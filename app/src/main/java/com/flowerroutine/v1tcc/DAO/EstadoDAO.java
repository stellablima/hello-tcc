package com.flowerroutine.v1tcc.DAO;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.flowerroutine.v1tcc.BDHelper.SQLiteConnection;
import com.flowerroutine.v1tcc.models.Estado;

public class EstadoDAO {

    private final SQLiteConnection sqlConnection;

    public EstadoDAO(SQLiteConnection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

    public long createEstado(Estado estado){
        SQLiteDatabase db = sqlConnection.getWritableDatabase();

        try{

            ContentValues cv = new ContentValues();
            cv.put("TITULO",estado.getTITULO());
            cv.put("DATA_ULTIMA_OCORRENCIA", estado.getDATA_ULTIMA_OCORRENCIA());
            cv.put("CATEGORIA",estado.getCATEGORIA());
            cv.put("FLAG",estado.getFLAG());
            cv.put("TEXTO",estado.getTEXTO());

            long idEstado = db.insert("ESTADO", null, cv);
            return idEstado;

        }catch (Exception e){

            e.printStackTrace();
        }
        return -1;
    }
}
