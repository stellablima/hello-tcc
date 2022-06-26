package com.flowerroutine.v1tcc.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.flowerroutine.v1tcc.BDHelper.SQLiteConnection;
import com.flowerroutine.v1tcc.models.Procedimento;

import java.util.ArrayList;
import java.util.List;

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
        }finally {

            if(db != null)
            db.close();
        }
        return -1;

    }

    public List<Procedimento> listProcedimento(){

        List<Procedimento> procedimentos =  new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor;

        String query = "SELECT * FROM PROCEDIMENTO WHERE FLAG=1;";

        try{

            sqLiteDatabase = this.sqlConnection.getReadableDatabase();

            cursor =  sqLiteDatabase.rawQuery(query, null);

            if (cursor.moveToFirst()){

                Procedimento procedimento = null;

                do {

                    procedimento = new Procedimento();
                    procedimento.set_id(cursor.getLong(0));
                    procedimento.setCATEGORIA(cursor.getString(1));
                    //DATA_INICIO
                    procedimento.setDATA_PREVISAO(cursor.getString(3));
                    //DEBITO_ESTOQUE
                    //DEBITO_FISIOLOGICO
                    procedimento.setFLAG(cursor.getString(6));
                    procedimento.setFLAG_FREQUENCIA(cursor.getString(7));
                    procedimento.setFLAG_REPETICAO(cursor.getString(8));
                    procedimento.setQTDDISPAROS(cursor.getString(9));
                    procedimento.setNOME(cursor.getString(10));
                    procedimento.setOBSERVACAO(cursor.getString(11));
                    //TEMPO_PREVISAO

                    procedimentos.add(procedimento);

                }while (cursor.moveToNext());
            }

        }catch (Exception e){

            Log.e("ERRO LIST PROCEDIMENTO", "Erro ao buscar procedimentos");
            return null;
        }finally {

            if (sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }

        return procedimentos;
    }

    //public List<Procedimento>
}
