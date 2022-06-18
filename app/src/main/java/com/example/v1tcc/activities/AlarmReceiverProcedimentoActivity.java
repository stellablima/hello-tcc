package com.example.v1tcc.activities;

import static com.example.v1tcc.activities.MainActivity.CHANNEL_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import android.app.Notification;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v1tcc.AlarmReceiver;
import com.example.v1tcc.BDHelper.SQLiteConnection;
import com.example.v1tcc.R;
import com.example.v1tcc.controller.RelatorioController;
import com.example.v1tcc.models.Relatorio;
import com.example.v1tcc.application.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AlarmReceiverProcedimentoActivity extends AppCompatActivity{

    private MediaPlayer mp;
    public static final String EXTRA_ID_PROCEDIMENTO = "idprocedimento";
    public static final String EXTRA_ID_ALARME = "idalarme";
    public static final String EXTRA_CANTAR = "cantarprocedimento";
    private Cursor cursor;
    private int idProcedimento;
    private int idAlarme;
    private TextView txtNomeProcedimento;
    private TextView txtCategoria;
    private TextView txtHoraProcedimento2;
    private String flagRepeticaoAlarme;
    private String flagFrequenciaAlarme;
    private String dataPrevisaoTxt;
    private Relatorio relatorio;

/*Os alarmes não são acionados quando o dispositivo está inativo no modo Soneca.
Todos os alarmes programados serão adiados até que o dispositivo saia do modo Soneca.
Várias opções estão disponíveis se você precisa garantir que seu trabalho seja concluído
mesmo quando o dispositivo estiver inativo. Você pode usar setAndAllowWhileIdle() ou
setExactAndAllowWhileIdle() para garantir que os alarmes serão executados. Outra opção é
usar a nova API WorkManager, que foi criada para executar trabalho em segundo plano uma
única vez ou periodicamente. Para saber mais, consulte Agendar tarefas com o WorkManager.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_receiver);
        carregaDados();


//        if (getIntent().getExtras().getString(EXTRA_CANTAR).equals("NAO_CANTAR")) {
////            mp=MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
////            mp.start();
//
//        } else
        if (getIntent().getExtras().getString(EXTRA_CANTAR).equals("CANTAR")) {
            mp=MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
            mp.start();
        }



        String txt = "EXTRA_ID_ALARME:"+ (getIntent().getExtras().getInt(EXTRA_ID_ALARME)) + "\nflagRepeticaoAlarme:" + flagRepeticaoAlarme;
        Toast.makeText(this, txt, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (getIntent().getExtras().getString(EXTRA_CANTAR).equals("CANTAR")) {
            mp.stop();
        }
    }



    public void btnFecharAlarmeOnClick(View view){

        if (flagRepeticaoAlarme.equals("0"))
            btnDeletarProcedimentoOnClick(view);

        preencherRelatorio(view);
        finish();
    }

    public void btnAtrasarAlarmeOnClick(View view){

        try {

            Boolean swtRepete = false;
            Boolean swtFrequencia = false;
            String spnPeriodo = "";
            String spnPeriodo1 = "";
            ArrayList<Calendar> alarmeTempo = new ArrayList<>();

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 1);//1min//expandir pra mais com opção de escolha
            alarmeTempo.add(cal);

            AlarmReceiver.snoozeAlarmProcedimento(this, alarmeTempo, idAlarme, swtRepete, swtFrequencia, spnPeriodo, spnPeriodo1);
            finish();


        } catch (SQLiteException e) {
            Toast.makeText(this, "Inclusão de atraso falhou "+e, Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void carregaDados(){
        txtNomeProcedimento = findViewById(R.id.txtProcedimento2);
        txtCategoria = findViewById(R.id.txtCategoria);
        txtHoraProcedimento2 = findViewById(R.id.txtHoraProcedimento2);
        idProcedimento = getIntent().getExtras().getInt(EXTRA_ID_PROCEDIMENTO);
        idAlarme = getIntent().getExtras().getInt(EXTRA_ID_ALARME);

        SQLiteConnection SQLiteConnection = null;
        SQLiteConnection = SQLiteConnection.getInstanciaConexao(this);
        SQLiteDatabase SQLiteDatabase = SQLiteConnection.getReadableDatabase();
        try {
            cursor = SQLiteDatabase.query("PROCEDIMENTO",
                    new String[] {"_id", "NOME", "CATEGORIA", "DATA_PREVISAO", "QTDDISPAROS", "FLAG_REPETICAO", "FLAG_FREQUENCIA"},
                    "_id = ?",
                    new String[] {Long.toString(idProcedimento)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {

                txtNomeProcedimento.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
                txtCategoria.setText(cursor.getString(cursor.getColumnIndexOrThrow("CATEGORIA")));

                dataPrevisaoTxt = cursor.getString(cursor.getColumnIndexOrThrow("DATA_PREVISAO"));
                dataPrevisaoTxt = dataPrevisaoTxt.substring(dataPrevisaoTxt.indexOf("[")+1, dataPrevisaoTxt.indexOf("]"));
                String[] dataPrevisaoSplitadoTxt = dataPrevisaoTxt.split(", ");

                //int qtdDisparos = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("QTDDISPAROS")));
                flagRepeticaoAlarme = cursor.getString(cursor.getColumnIndexOrThrow("FLAG_REPETICAO"));
                flagFrequenciaAlarme = cursor.getString(cursor.getColumnIndexOrThrow("FLAG_FREQUENCIA"));


                if (flagRepeticaoAlarme.equals("1")){

                    String idAlarmePop = Integer.toString(idAlarme).substring((Integer.toString(idAlarme)).length() - 1);
                    txtHoraProcedimento2.setText(dataPrevisaoSplitadoTxt[Integer.parseInt(idAlarmePop)]);

                }else{

                    txtHoraProcedimento2.setText(dataPrevisaoSplitadoTxt[0]);
                }
            }
            else
                Toast.makeText(this, "Procedimento não encontrado", Toast.LENGTH_SHORT).show();
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Falha no acesso ao Banco de Dados "+e, Toast.LENGTH_LONG).show();
        }

        SQLiteDatabase.close();

    }

    public void btnDeletarProcedimentoOnClick(View view){
        /******************************************************/

        SQLiteConnection SQLiteConnection = null;
        SQLiteConnection = SQLiteConnection.getInstanciaConexao(this);
        SQLiteDatabase SQLiteDatabase = SQLiteConnection.getWritableDatabase();

        try {

            ContentValues cv = new ContentValues();
            cv.put("FLAG", "2");

            SQLiteDatabase.update("PROCEDIMENTO", cv, "_id = ?", new String[] {Long.toString(idProcedimento)});

            AlarmReceiver.cancelAlarmDef(this, (idProcedimento), 1);

        } catch (SQLiteException e) {
            Toast.makeText(this, "Deleção falhou", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        SQLiteDatabase.close();

    }

    public void preencherRelatorio(View view){

        SQLiteConnection SQLiteConnection = null;
        SQLiteConnection = SQLiteConnection.getInstanciaConexao(this);
        SQLiteDatabase SQLiteDatabase = SQLiteConnection.getWritableDatabase();

        try {

            long idRelatorio = insereRelatorio(SQLiteConnection);

            if(idRelatorio > 1)
                Toast.makeText(this,"Gravado em relatório", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Erro ao gravar", Toast.LENGTH_LONG).show();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Inclusão falhou "+e, Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        SQLiteDatabase.close();
    }

    private Relatorio getRelatorioActivity(){

        this.relatorio = new Relatorio();

        this.relatorio.set_id_PROCEDIMENTO(idProcedimento);
        this.relatorio.setCATEGORIA(txtCategoria.getText().toString());
        this.relatorio.setDATA_INICIO(Calendar.getInstance(Locale.getDefault()).getTime().toString());
        //cv.put("DATA_INICIO", Calendar.getInstance(Locale.BRAZIL).getTime().toString());
        this.relatorio.setNOME(txtNomeProcedimento.getText().toString());
        this.relatorio.setDATA_PREVISAO(dataPrevisaoTxt);

        return relatorio;
    }

    private long insereRelatorio(SQLiteConnection SQLiteConnection){
        RelatorioController relatorioController =  new RelatorioController(SQLiteConnection);
        return relatorioController.createRelatorioController(getRelatorioActivity());
    }


}