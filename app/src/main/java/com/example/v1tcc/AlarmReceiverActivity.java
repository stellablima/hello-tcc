package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.v1tcc.BDHelper.SQLiteConnection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AlarmReceiverActivity extends AppCompatActivity {

    private MediaPlayer mp;
    public static final String EXTRA_ID_PROCEDIMENTO = "idprocedimento";
    public static final String EXTRA_ID_ALARME = "idalarme";
    private SQLiteConnection SQLiteConnection;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private int idProcedimento;
    private int idAlarme;
    private TextView txtNomeProcedimento;
    private TextView txtCategoria;
    private TextView txtHoraProcedimento2;
    String flagRepeticaoAlarme;
    String flagFrequenciaAlarme;
    String dataPrevisaoTxt;
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
        if (flagRepeticaoAlarme.equals("0"))
            btnDeletarProcedimentoOnClick(view); //ele da erro de flag aqui
        else
            Toast.makeText(this, "nao apaga do banco", Toast.LENGTH_SHORT).show();
        //nao excluir se alarme multiplo
        //qual valor de hora carregar no txt

        //ao fechar gravar no relatorio de urina ou qual for a categoria do alarme como feito e o horario de inicio
        //pode futuramente configurar a duração(pré) e status(pos)

        preencherRelatorio(view);
        finish();


        /*
        * PRRENCHER RELATORIO
        * interface mostrar por categoria
        * adicionar filtro categoria e ordem dedrescente
        * */
    }

    public void btnAtrasarAlarmeOnClick(View view){
        //pegar id do alarme
        //renovar a função update
        //nao usar ela XD
        //reagendar disparo unico pra daquqi 5 minutos

        //qual id preencher? adicionar outro numero no final do id do alarme, ...
        //reservar o numero 9 para isso limitando ainda a 9 disparos em vez de 10?
        //adicionar uma flag, e usar o id da tabela para agendar alarme unico?
        //roubar id repetitivo em particular e escrever por cima e depois devolver?

        //id final 9 reservado para alarme de atraso podendo ser sobrescrito
        //interpretar pelo txt o tipo do alarme e mandar, tem um pedaço disso na edição

        try {

            Boolean swtRepete = false;
            Boolean swtFrequencia = false;
            int idInserted = idProcedimento;//9
            String spnPeriodo = "";
            String spnPeriodo1 = "";
            ArrayList<Calendar> alarmeTempo = new ArrayList<>();



            Calendar cal = Calendar.getInstance();


            cal.add(Calendar.MINUTE, 1);//1min
            alarmeTempo.add(cal);

            //if(flagRepeticaoAlarme=="1")
            //    swtRepete = true;
            //if(flagFrequenciaAlarme=="1")
            //    swtFrequencia = true;

            //idInserted é o id do banco nao id do alarme pegar o idlarme aqui, toquei o id inseretd
            AlarmReceiver.snoozeAlarmProcedimento(this, alarmeTempo, idAlarme, swtRepete, swtFrequencia, spnPeriodo, spnPeriodo1);
            Toast.makeText(this, "alarmeTempo:"+alarmeTempo.get(0)+"\nidAlarme:"+idAlarme+"\nswtRepete:"+swtRepete+"\nswtFrequencia:"+swtFrequencia+"\nspnPeriodo:"+spnPeriodo+"\nspnPeriodo1:"+spnPeriodo1, Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "Atraso incluído com sucesso", Toast.LENGTH_LONG).show();
            Log.v("VERBOSE","alarmeTempo:"+alarmeTempo.get(0)+"\nidAlarme:"+idAlarme+"\nswtRepete:"+swtRepete+"\nswtFrequencia:"+swtFrequencia+"\nspnPeriodo:"+spnPeriodo+"\nspnPeriodo1:"+spnPeriodo1);
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

        try {

            SQLiteConnection = new SQLiteConnection(this);
            bd = SQLiteConnection.getReadableDatabase();
            cursor = bd.query("PROCEDIMENTO",
                    //new String[] {"_id", "", "", , "QTDDISPAROS", "FLAG_REPETICAO", "FLAG_FREQUENCIA"},
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

                    //se alarme repetição -2 digitos, simplificado se apenas dobrar, dai o ultimo digito sera igual no index txt
                    //pegar ultimo digito, se for 9

                    String idAlarmePop = Integer.toString(idAlarme).substring((Integer.toString(idAlarme)).length() - 1);
                    txtHoraProcedimento2.setText(dataPrevisaoSplitadoTxt[Integer.parseInt(idAlarmePop)]);
                    //txtHoraProcedimento2.setText("dataPrevisaoSplitadoTxt["+idAlarmePop+"]: "+dataPrevisaoSplitadoTxt[Integer.parseInt(idAlarmePop)]);
                    Log.v("VERBOSE","dataPrevisaoSplitadoTxt["+idAlarmePop+"]: "+dataPrevisaoSplitadoTxt[Integer.parseInt(idAlarmePop)]);
                    Toast.makeText(this, "dataPrevisaoSplitadoTxt["+idAlarmePop+"]: "+dataPrevisaoSplitadoTxt[Integer.parseInt(idAlarmePop)], Toast.LENGTH_SHORT).show();

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
            cv.put("FLAG", "2");//0excluido,1ativo,2concluidoprocedimentounico(evolua pra alarme com expiração),3procedimentopararegistrosemalarme
            SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
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

    public void preencherRelatorio(View view){
        try {

            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getWritableDatabase();
            ContentValues cvEstq = carregaCVProcedimento();
            int idInserted = (int) bd.insert("RELATORIO", null, cvEstq);

            if(idInserted > 1)
                Toast.makeText(this,"Gravado em relatório", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Erro ao gravar", Toast.LENGTH_LONG).show();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Inclusão falhou "+e, Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private ContentValues carregaCVProcedimento() {



        ContentValues cv = new ContentValues();
        cv.put("_id_PROCEDIMENTO", idProcedimento);
        cv.put("CATEGORIA", txtCategoria.getText().toString());
        cv.put("DATA_INICIO", Calendar.getInstance(Locale.getDefault()).getTime().toString());
        //cv.put("DATA_INICIO", Calendar.getInstance(Locale.BRAZIL).getTime().toString());
        cv.put("DATA_PREVISAO", dataPrevisaoTxt);
        cv.put("NOME", txtNomeProcedimento.getText().toString());


        return cv;
    }

}