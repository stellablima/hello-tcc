package com.example.v1tcc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmReceiverActivity extends AppCompatActivity {

    private MediaPlayer mp;
    public static final String EXTRA_ID_PROCEDIMENTO = "idprocedimento";
    public static final String EXTRA_ID_ALARME = "idalarme";
    private BDRotinaHelper bdRotinaHelper;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private Long idProcedimento;
    private TextView txtNomeProcedimento;
    private TextView txtCategoria;
    private TextView txtHoraProcedimento2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_receiver);

        mp=MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mp.start();

        //Toast.makeText(this, "id"+ (getIntent().getExtras().getLong(EXTRA_ID)), Toast.LENGTH_LONG).show();

        carregaDados();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }

    public void btnFecharAlarmeOnClick(View view){
        //ao concluir um alarme unico ele tem que sumir da tela, apagar do banco

        //SO SE FOR ALARME UNICO
        btnDeletarProcedimentoOnClick(view);
        finish();
    }

    private void carregaDados(){
        txtNomeProcedimento = findViewById(R.id.txtProcedimento2);
        txtCategoria = findViewById(R.id.txtCategoria);
        txtHoraProcedimento2 = findViewById(R.id.txtHoraProcedimento2);
        idProcedimento = getIntent().getExtras().getLong(EXTRA_ID_PROCEDIMENTO);


        try {

            bdRotinaHelper = new BDRotinaHelper(this);
            bd = bdRotinaHelper.getReadableDatabase();
            cursor = bd.query("PROCEDIMENTO",
                    //new String[] {"_id", "", "", , "QTDDISPAROS", "FLAG_REPETICAO", "FLAG_FREQUENCIA"},
                    new String[] {"_id", "NOME", "CATEGORIA", "DATA_PREVISAO"},
                    "_id = ?",
                    new String[] {Long.toString(idProcedimento)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {

                txtNomeProcedimento.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
                txtCategoria.setText(cursor.getString(cursor.getColumnIndexOrThrow("CATEGORIA")));

                String dataPrevisaoSplitadoTxt = cursor.getString(cursor.getColumnIndexOrThrow("DATA_PREVISAO"));
                dataPrevisaoSplitadoTxt = dataPrevisaoSplitadoTxt.substring(dataPrevisaoSplitadoTxt.indexOf("[")+1, dataPrevisaoSplitadoTxt.indexOf("]"));
                String[] dataPrevisaoTxt = dataPrevisaoSplitadoTxt.split(", ");

                txtHoraProcedimento2.setText(dataPrevisaoTxt[0]);
//
//                String categoriaAlarme = cursor.getString(cursor.getColumnIndexOrThrow("CATEGORIA"));
//                String qtdDisparosAlarme = cursor.getString(cursor.getColumnIndexOrThrow("QTDDISPAROS"));
//                String flagRepeticaoAlarme = cursor.getString(cursor.getColumnIndexOrThrow("FLAG_REPETICAO"));
//                String flagFrequenciaAlarme = cursor.getString(cursor.getColumnIndexOrThrow("FLAG_FREQUENCIA"));
//                String dataPrevisaoAlarme = cursor.getString(cursor.getColumnIndexOrThrow("DATA_PREVISAO"));
//                switch (categoriaAlarme + "") {
//                    case "Medicação":
//                        spnCategoriasAlarme.setSelection(0);
//                        break;
//                    case "Higienização":
//                        spnCategoriasAlarme.setSelection(1);
//                        break;
//                    default: //"Outro"
//                        spnCategoriasAlarme.setSelection(2);
//                        break;
//                }
//
//                String dataPrevisaoSplitadoTxt = dataPrevisaoAlarme.substring(dataPrevisaoAlarme.indexOf("[")+1, dataPrevisaoAlarme.indexOf("]"));
//                dataPrevisaoSplitado = dataPrevisaoSplitadoTxt.split(", ");
//
//                if (flagRepeticaoAlarme.equals("1")){
//                    swtRepeteAlarme.setChecked(true);
//                    String textoParenteses = dataPrevisaoAlarme.substring(dataPrevisaoAlarme.indexOf("(")+1, dataPrevisaoAlarme.lastIndexOf(")"));
//                    switch (textoParenteses){
//                        case "MINUTO":
//                            spnPeriodoAlarme.setSelection(0);
//                            break;
//                        case "HORAS":
//                            spnPeriodoAlarme.setSelection(1);
//                            break;
//                        case "DIA S/N":
//                            spnPeriodoAlarme.setSelection(2);
//                            break;
//                        case "DIA":
//                            spnPeriodoAlarme.setSelection(3);
//                            break;
//                        case "SEMANA":
//                            spnPeriodoAlarme.setSelection(4);
//                            break;
//                        case "MÊS":
//                            spnPeriodoAlarme.setSelection(5);
//                            break;
//                        case "ANO":
//                            spnPeriodoAlarme.setSelection(6);
//                            break;
//                        default:
//                            spnPeriodoAlarme.setSelection(3);
//                            break;
//                    }
//
//                    int intSpnPeriodo1Alarme = (Integer.parseInt(dataPrevisaoAlarme.substring(dataPrevisaoAlarme.indexOf("]")+1, dataPrevisaoAlarme.indexOf("]")+2)))-1;
//
//                    swtFrequenciaAlarme.setEnabled(true);
//                    llAlarmeDistribuido.setVisibility(View.VISIBLE);
//
//                    if (flagFrequenciaAlarme.equals("1")) {
//                        swtFrequenciaAlarme.setChecked(true);
//                        lvRepeticaoDesproporcinalAlarme.setVisibility(View.INVISIBLE);
//                        spnPeriodoAlarme.setEnabled(true);
//                        txt.setText("EM");
//                        spnPeriodo1Alarme.setSelection(intSpnPeriodo1Alarme);
//                        spnPeriodo0Alarme.setSelection(spnPeriodo1Alarme.getSelectedItemPosition());
//                        txtHoraProcedimento.setText(dataPrevisaoSplitado[0]);
//                    }else{
//                        txtHoraProcedimento.setVisibility(View.INVISIBLE);
//                        lvRepeticaoDesproporcinalAlarme.setVisibility(View.VISIBLE);
//                        spnPeriodoAlarme.setEnabled(false);
//                        txt.setText("X");
//                        spnPeriodo0Alarme.setSelection(0);
//                        spnPeriodo1Alarme.setSelection(intSpnPeriodo1Alarme);//ja esta zerado ai ele preenche com mais zeros
//
//                        flagAlterarLVBugado = true;
//                    }
//                }else
//                    txtHoraProcedimento.setText(dataPrevisaoSplitado[0]);
            }
            else
                Toast.makeText(this, "Procedimento não encontrado", Toast.LENGTH_SHORT).show();
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Falha no acesso ao Banco de Dados "+e, Toast.LENGTH_LONG).show();
        }

    }

    public void btnDeletarProcedimentoOnClick(View view){
        /******************************************************/
        try {

            ContentValues cv = new ContentValues();
            cv.put("FLAG", "2");//0excluido,1ativo,2concluido
            BDRotinaHelper bdEstoqueHelper = new BDRotinaHelper(this);
            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
            bd.update("PROCEDIMENTO", cv, "_id = ?", new String[] {Long.toString(idProcedimento)});

            AlarmReceiver.cancelAlarmDef(this, (idProcedimento).intValue(), 1);

        } catch (SQLiteException e) {
            Toast.makeText(this, "Deleção falhou", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}