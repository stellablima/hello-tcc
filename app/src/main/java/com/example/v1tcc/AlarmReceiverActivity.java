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
    private int idProcedimento;
    private int idAlarme;
    private TextView txtNomeProcedimento;
    private TextView txtCategoria;
    private TextView txtHoraProcedimento2;
    String flagRepeticaoAlarme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_receiver);
        carregaDados();

        mp=MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mp.start();

        String txt = "EXTRA_ID_ALARME:"+ (getIntent().getExtras().getInt(EXTRA_ID_ALARME)) + "\nflagRepeticaoAlarme:" + flagRepeticaoAlarme;
        Toast.makeText(this, txt, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }

    public void btnFecharAlarmeOnClick(View view){
        //ao concluir um alarme unico ele tem que sumir da tela, apagar do banco

        //SO SE FOR ALARME UNICO
        //if (flagRepeticaoAlarme.equals("0"))
        //    btnDeletarProcedimentoOnClick(view); //ele da erro de flag aqui
        //else
        //    Toast.makeText(this, "nao apaga do banco", Toast.LENGTH_SHORT).show();
        //nao excluir se alarme multiplo
        //qual valor de hora carregar no txt

        //ao fechar gravar no relatorio de urina ou qual for a categoria do alarme como feito e o horario de inicio
        //pode futuramente configurar a duração(pré) e status(pos)
        //
        finish();
    }

    private void carregaDados(){
        txtNomeProcedimento = findViewById(R.id.txtProcedimento2);
        txtCategoria = findViewById(R.id.txtCategoria);
        txtHoraProcedimento2 = findViewById(R.id.txtHoraProcedimento2);
        idProcedimento = getIntent().getExtras().getInt(EXTRA_ID_PROCEDIMENTO);
        idAlarme = getIntent().getExtras().getInt(EXTRA_ID_ALARME);

        try {

            bdRotinaHelper = new BDRotinaHelper(this);
            bd = bdRotinaHelper.getReadableDatabase();
            cursor = bd.query("PROCEDIMENTO",
                    //new String[] {"_id", "", "", , "QTDDISPAROS", "FLAG_REPETICAO", "FLAG_FREQUENCIA"},
                    new String[] {"_id", "NOME", "CATEGORIA", "DATA_PREVISAO", "QTDDISPAROS", "FLAG_REPETICAO"},
                    "_id = ?",
                    new String[] {Long.toString(idProcedimento)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {

                txtNomeProcedimento.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
                txtCategoria.setText(cursor.getString(cursor.getColumnIndexOrThrow("CATEGORIA")));

                String dataPrevisaoTxt = cursor.getString(cursor.getColumnIndexOrThrow("DATA_PREVISAO"));
                dataPrevisaoTxt = dataPrevisaoTxt.substring(dataPrevisaoTxt.indexOf("[")+1, dataPrevisaoTxt.indexOf("]"));
                String[] dataPrevisaoSplitadoTxt = dataPrevisaoTxt.split(", ");

                //int qtdDisparos = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("QTDDISPAROS")));
                flagRepeticaoAlarme = cursor.getString(cursor.getColumnIndexOrThrow("FLAG_REPETICAO"));

                if (flagRepeticaoAlarme.equals("1")){

                    String idAlarmePop = Integer.toString(idAlarme).substring((Integer.toString(idAlarme)).length() - 1);
                    txtHoraProcedimento2.setText(dataPrevisaoSplitadoTxt[Integer.parseInt(idAlarmePop)]);
                    //txtHoraProcedimento2.setText("dataPrevisaoSplitadoTxt["+idAlarmePop+"]: "+dataPrevisaoSplitadoTxt[Integer.parseInt(idAlarmePop)]);
                    //Toast.makeText(this, "dataPrevisaoSplitadoTxt["+idAlarmePop+"]: "+dataPrevisaoSplitadoTxt[Integer.parseInt(idAlarmePop)], Toast.LENGTH_SHORT).show();
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//parece ter funfado porem bug: ele nao aciona segundo alarme do array na repeticao desproporcional
//ele duplicao alarme desproporcional ao alarmar
//sugiro estressar sem essa parte pra corrigir os bugs acima
//alarm tela branca e minimiza?
//outro bug: ao mudat tipo de alarme ele mantei string3 [00:00, 00:00] de array do antigo
//ou com aspas [] vazias
//provavel motivo, tem que resetar as flags antigas setadas ou mal funcionamento do modulo editar
//ele nao da erro apenas nao rendederiza p txt e interagindo vem o erro

// problema, quando se edita e muda p tipo do alarme, isso bug o campo de data previsao
// teste um programei dois alarmes e editei, passou
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                }else{
                    //txtHoraProcedimento.setText(dataPrevisaoSplitado[0]);
                    txtHoraProcedimento2.setText(dataPrevisaoSplitadoTxt[0]);
                }
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

            AlarmReceiver.cancelAlarmDef(this, (idProcedimento), 1);

        } catch (SQLiteException e) {
            Toast.makeText(this, "Deleção falhou", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}