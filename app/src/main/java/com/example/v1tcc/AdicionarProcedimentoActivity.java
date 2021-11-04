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
    private String horaSelecionadaAlarme = "11:11";
    private TimePickerDialog mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_procedimento);

        edtNomeProcedimento = findViewById(R.id.edtNomeProcedimento);
        txtHoraProcedimento = findViewById(R.id.txtHoraProcedimento);
    }

    public void txtHoraProcedimentoOnClick(View view){
        Toast.makeText(this, "clicado",Toast.LENGTH_LONG).show();

        calHoraAlarm = Calendar.getInstance();
        horaAtual = Integer.toString(calHoraAlarm.get(Calendar.HOUR_OF_DAY));
        minAtual = Integer.toString(calHoraAlarm.get(Calendar.MINUTE));
       /* tmpHoraAlarme = new TimePickerDialog(
                this,
                (TimePicker timePicker, int hourOfDay, int minutes) => {
                    txtHoraProcedimento.setText(String.format("%02d", hourOfDay));
                    txtHoraProcedimento.setText(String.format("%02d", hourOfDay));
                },
                horaAtual, minAtual, true);
        tmpHoraAlarme.show();
        );*/
        //bom exemplo https://stackoverflow.com/questions/17901946/timepicker-dialog-from-clicking-edittext
        //txtHoraProcedimento.setOnClickListener(new OnClickListener() {
           // @Override
            //public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txtHoraProcedimento.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//tem como pegar o padrão corernte no dispositivo?
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
           // }
      //  });

//        //mTimePicker.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//        //    @Override
//        //    public void onClick(View view) {
//                String[] txtHora =  txtHoraProcedimento.getText().toString().split(":");
//                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM); //valor.substring(0,valor.length-2);
//                intent.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(txtHora[0]));//(int) txtHoraProcedimento.getText().toString().indexOf(0,2));//int
//                intent.putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(txtHora[1]));//txtHora.substring(3,5));// (int) txtHoraProcedimento.toString().indexOf(2,4));//int
//                intent.putExtra(AlarmClock.EXTRA_MESSAGE,"Teste");
//                if(intent.resolveActivity(getPackageManager())!=null)
//                    startActivity(intent);
//        //    }
//        //});

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnSalvarProcedimentoOnClick(View view){

        try {
            preenchimentoValido();

            String[] txtHora =  txtHoraProcedimento.getText().toString().split(":");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(txtHora[0]));  // set hour
            cal.set(Calendar.MINUTE, Integer.parseInt(txtHora[1]));          // set minute
            cal.set(Calendar.SECOND, 0);               // set seconds

            //como montar umid? primeiro digito referente e tabela sendo 0 procedimento
            //+ id do procedimento gravado na tabela
            //if (operacao == OP_INCLUI) {
            int idInserted = insereEstq();
            Toast.makeText(this, "Id inserido:"+idInserted, Toast.LENGTH_SHORT).show();
            AlarmReceiver.startAlarmDef(this, cal, idInserted);
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

    private void preenchimentoValido() throws Exception {
        String s;
        s = edtNomeProcedimento.getText().toString();
        if (s.equals(""))
            throw new Exception("O Nome deve ser preenchido");
        //else if (operacao == OP_INCLUI && jaExiste(s))
        //    throw new Exception("Código já cadastrado");
        //s = edtNome.getText().toString();
        //if (s.equals(""))
        //    throw new Exception("O Nome do Produto deve ser preenchido");
        //s = spnUnidade.getSelectedItem().toString();
        //if (s.equals(""))
        //    throw new Exception("A Unidade deve ser preenchida");
    }





}
