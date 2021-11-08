package com.example.v1tcc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Calendar;

public class AdicionarProcedimentoActivity extends AppCompatActivity {

    private EditText edtNomeProcedimento;
    private TextView txtHoraProcedimento;
    private TimePicker tmpHoraAlarme;
    private Calendar calHoraAlarm;
    private String horaAtual;
    private String minAtual;
    private TimePickerDialog mTimePicker;
    private Spinner spnRepeticoesAlarme;
    private Spinner spnCategoriasAlarme;
    private Spinner spnPeriodoAlarme;
    private Switch swtRepeteAlarme;
    private Switch swtPropocionalAlarme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_procedimento);

        edtNomeProcedimento = findViewById(R.id.edtNomeProcedimento);
        txtHoraProcedimento = findViewById(R.id.txtHoraProcedimento);
        spnRepeticoesAlarme = findViewById(R.id.spnRepeticoesAlarme);
        spnCategoriasAlarme = findViewById(R.id.spnCategoriasAlarme);
        spnPeriodoAlarme = findViewById(R.id.spnPeriodoAlarme);
        swtRepeteAlarme = findViewById(R.id.swtRepeteAlarme);
        swtPropocionalAlarme = findViewById(R.id.swtPropocionalAlarme);

        Helpers.spinnerNumero(this, R.array.numeros, spnRepeticoesAlarme);
        Helpers.spinnerNumero(this, R.array.categorias, spnCategoriasAlarme);
        Helpers.spinnerNumero(this, R.array.periodos, spnPeriodoAlarme);
        Helpers.txtHoraConfig(this, txtHoraProcedimento);

        swtRepeteAlarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!swtRepeteAlarme.isChecked()){
                    spnRepeticoesAlarme.setSelection(0);
                    spnRepeticoesAlarme.setEnabled(false);
                }else
                    spnRepeticoesAlarme.setEnabled(true);
            }
        });

        //swtPropocionalAlarme.setClickable(false);
        swtPropocionalAlarme.setEnabled(false);

        //spnPeriodoAlarme.setClickable(false);
        spnPeriodoAlarme.setEnabled(false);
    }

    public void btnSalvarProcedimentoOnClick(View view){

        try {
            Helpers.preenchimentoValido(edtNomeProcedimento);

            String[] txtHora =  txtHoraProcedimento.getText().toString().split(":");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(txtHora[0]));  // set hour
            cal.set(Calendar.MINUTE, Integer.parseInt(txtHora[1]));          // set minute
            cal.set(Calendar.SECOND, 0);               // set seconds
            Boolean swtRepete = swtRepeteAlarme.isChecked();
            Boolean swtPropocional = swtPropocionalAlarme.isChecked();
            String spnRepeticao = spnRepeticoesAlarme.getSelectedItem().toString();
            String spnCategoria = spnCategoriasAlarme.getSelectedItem().toString();
            String spnPeriodo = spnPeriodoAlarme.getSelectedItem().toString();
            ArrayList<Calendar> alarmeTempo = new ArrayList<>();
            alarmeTempo.add(cal);
            //como montar umid? primeiro digito referente e tabela sendo 0 procedimento
            //+ id do procedimento gravado na tabela
            //if (operacao == OP_INCLUI) {
            int idInserted = insereEstq();
            Toast.makeText(this, "Id inserido:"+idInserted, Toast.LENGTH_SHORT).show();
            AlarmReceiver.startAlarmProcedimento(this, alarmeTempo, idInserted, spnRepeticao);
            //}
            //else if (operacao == OP_ALTERA) {
            //    alteraEstq();
            //    finish();
            //}
            finish();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Inclusão falhou", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private int insereEstq() {
        BDRotinaHelper bdRotinaHelper = new BDRotinaHelper(this);
        SQLiteDatabase bd = bdRotinaHelper.getWritableDatabase();
        ContentValues cvEstq = carregaCVProcedimento();
        return (int) bd.insert("PROCEDIMENTO", null, cvEstq);
    }

    private ContentValues carregaCVProcedimento() {
        ContentValues cv = new ContentValues();
        cv.put("NOME", edtNomeProcedimento.getText().toString());
        cv.put("FLAG", "1");
        //o que ele vai salvar no futuro no banco vai ser um alarme com uma configuração e uma peridiocidade, e o que vai
        //persistir no banco é quando a ação for realizada os dados mínimos para relatório, toda a mágica fora o crud
        //vai acontecer ali e pode ser necessário rependar melhor esse módulo, vai ser um tabelão de crescimento esponencial com indexes
        //mas tbm pode filtrar gerar o relatório e apagar os dados mais antigos periodicamente
        cv.put("DATA_PREVISAO", txtHoraProcedimento.getText().toString());
        //cv.put("UNID", spnUnidade.getSelectedItem().toString());
        //cv.put("QTDE", qtde);
        //cv.put("PCUNIT", pcUnit);


        return cv;
    }





}
