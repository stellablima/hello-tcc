package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProcedimentosActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "idprocedimento";
    //public static final String EXTRA_TIPO = "tipoevento";

    private TextView txtNomeProcedimento;
    private TextView txtHoraProcedimento;
    private Long idProcedimento;
    private BDRotinaHelper bdRotinaHelper;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private Button btnEditarProcedimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedimentos);
        txtNomeProcedimento=findViewById(R.id.txtNomeProcedimento);
        txtHoraProcedimento=findViewById(R.id.txtHoraProcedimento);
        //teste
        txtNomeProcedimento.setText(String.valueOf(getIntent().getExtras().getLong(EXTRA_ID)));

    }

    @Override
    protected void onStart() {
        super.onStart();
        carregaDados();
    }

    public void btnDeletarProcedimentoOnClick(View view){
        Toast.makeText(this, "Alarme Cancelado", Toast.LENGTH_LONG).show();

        /******************************************************/
        try {

            ContentValues cv = new ContentValues();
            cv.put("FLAG", "0");
            BDRotinaHelper bdEstoqueHelper = new BDRotinaHelper(this);
            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
            bd.update("PROCEDIMENTO", cv, "_id = ?", new String[] {Long.toString(idProcedimento)});
            AlarmReceiver.cancelAlarmDef(this, (idProcedimento).intValue());
            //finish();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Deleção falhou", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void btnEditarProcedimentoOnClick(View view){
        Toast.makeText(this, "a fazer", Toast.LENGTH_LONG).show();

        /******************************************************/
//        try {

            //AlarmReceiver ar= new AlarmReceiver(1); //com id proprio
            //AlarmReceiver.cancelAlarmDef(this, 11);
            //AlarmReceiver.cancelarAlarme(this, 5);
//            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            Intent intent = new Intent(this, AlarmReceiver.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
//                    0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            alarmManager.cancel(pendingIntent);

            /**alteração de alarme existente tentando**/
//            String[] txtHora =  "23:59".split(":"); //txtHoraProcedimento.getText().toString().split(":");
//            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM); //valor.substring(0,valor.length-2);
//            intent.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(txtHora[0]));//(int) txtHoraProcedimento.getText().toString().indexOf(0,2));//int
//            intent.putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(txtHora[1]));//txtHora.substring(3,5));// (int) txtHoraProcedimento.toString().indexOf(2,4));//int
//            intent.putExtra(AlarmClock.EXTRA_MESSAGE,"alterado:"+txtNomeProcedimento.getText().toString());
//






            //if(intent.resolveActivity(getPackageManager())!=null)
            //    startActivity(intent);


            //search PendingIntent.FLAG_CANCEL_CURRENT, testar apagando depois update manager.getNextAlarmClock()
//            PendingIntent pendingIntent =
//                    PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//

            //private static void setupEditOnClick(Context context, RemoteViews widget) {
                //String intentAction = Build.VERSION.SDK_INT >= 19 ? AlarmClock.ACTION_SHOW_ALARMS : AlarmClock.ACTION_SET_ALARM;
                //Intent launchIntent = new Intent(intentAction);
//                PendingIntent launchPendingIntent = PendingIntent.getActivity(this, r.nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                view.setOnClickListener(PendingIntent(R.id.btnEditarProcedimento, launchPendingIntent);
            //}


//            Intent myIntent = new Intent(this, ProcedimentosActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this,'Nitrazepam', myIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
//            if (am != null) {
//                am.cancel(pendingIntent);
//            }
//            pendingIntent.cancel();
//            am.cancel(pendingIntent);

            /**pendete o update**/
            //BDRotinaHelper bdRotinaHelper = new BDRotinaHelper(this);
            //SQLiteDatabase bd = bdRotinaHelper.getWritableDatabase();

            //ContentValues cv = new ContentValues();
            //cv.put("NOME", txtNomeProcedimento.getText().toString());
            //cv.put("DATA_PREVISAO", txtHoraProcedimento.getText().toString());
            //bd.insert("PROCEDIMENTO", null, cv); //aqui iria um update
            //finish();

//        } //catch (SQLiteException e) {
//            Toast.makeText(this, "Inclusão falhou", Toast.LENGTH_LONG).show();
//        }
//        catch (Exception e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }
        /******************************************************/

    }

    private void carregaDados() {
        TextView txt;
        String s;
        int valor; //txtHoraProcedimento
        try {
            idProcedimento = getIntent().getExtras().getLong(EXTRA_ID);
            bdRotinaHelper = new BDRotinaHelper(this);
            bd = bdRotinaHelper.getReadableDatabase();
            // Podemos criar o cursor com rawQuery()
//            //cursor = bd.rawQuery("select _id, CODIGO, NOME, UNID, QTDE, PCUNIT from ESTOQUE where _id = ?",
//            // new String[] {Long.toString(idEstq)} );
//            // ou com query(). O rawQuery foi usado na app anterior (Biblioteca) e aqui usamos query()
            cursor = bd.query("PROCEDIMENTO",
                    new String[] {"_id", "NOME", "DATA_PREVISAO"},
                    "_id = ?",
                    new String[] {Long.toString(idProcedimento)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                txt = findViewById(R.id.txtNomeProcedimento);
                txt.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
                txt = findViewById(R.id.txtHoraProcedimento);
                txt.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATA_PREVISAO")));
//                txt = findViewById(R.id.txtUnid);
//                String unid = cursor.getString(cursor.getColumnIndexOrThrow("UNID"));
//                txt.setText(unid);
//                valor = cursor.getInt(cursor.getColumnIndexOrThrow("QTDE"));
//                if (unid.equals("Kg"))
//                    s = "Qtde. Estq: " + String.format(Locale.getDefault(), "%,.3f", valor/1000.0);
//                else
//                    s = "Qtde. Estq: " + String.format(Locale.getDefault(), "%,d", valor);
//                txt = findViewById(R.id.txtQtde);
//                txt.setText(s);
//                valor = cursor.getInt(cursor.getColumnIndexOrThrow("PCUNIT"));
//                txt = findViewById(R.id.txtPcUnit);
//                txt.setText("Valor: R$ " + String.format(Locale.getDefault(), "%,.2f", valor/100.0));
            }
            else
                Toast.makeText(this, "Produto não encontrado", Toast.LENGTH_SHORT).show();
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Falha no acesso ao Banco de Dados", Toast.LENGTH_LONG).show();
        }
    }


}
