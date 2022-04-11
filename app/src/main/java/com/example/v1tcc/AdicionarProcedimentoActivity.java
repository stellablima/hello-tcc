package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AdicionarProcedimentoActivity extends AppCompatActivity {

    private EditText edtNomeProcedimento;
    private TextView txtHoraProcedimento;
    private TextView txtFrequenciaAlarme;
    private TimePicker tmpHoraAlarme;
    private Calendar calHoraAlarm;
    private String horaAtual;
    private String minAtual;
    private TimePickerDialog mTimePicker;
    private Spinner spnRepeticoesAlarme;
    private Spinner spnCategoriasAlarme;
    private Spinner spnPeriodoAlarme;
    private Spinner spnPeriodo0Alarme;
    private Spinner spnPeriodo1Alarme;
    private Switch swtRepeteAlarme;
    private Switch swtFrequenciaAlarme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_procedimento);

        edtNomeProcedimento = findViewById(R.id.edtNomeProcedimento);
        txtHoraProcedimento = findViewById(R.id.txtHoraProcedimento);
        txtFrequenciaAlarme = findViewById(R.id.txtFrequencia);
        spnRepeticoesAlarme = findViewById(R.id.spnRepeticoesAlarme);
        spnCategoriasAlarme = findViewById(R.id.spnCategoriasAlarme);
        spnPeriodoAlarme = findViewById(R.id.spnPeriodoAlarme);
        swtRepeteAlarme = findViewById(R.id.swtRepeteAlarme);
        swtFrequenciaAlarme = findViewById(R.id.swtFrequenciaAlarme);
        spnPeriodo1Alarme = findViewById(R.id.spnPeriodo1);
        spnPeriodo0Alarme = findViewById(R.id.spnPeriodo0);

        Helpers.spinnerNumero(this, R.array.numeros, spnRepeticoesAlarme);
        Helpers.spinnerNumero(this, R.array.numeros, spnPeriodo0Alarme);
        Helpers.spinnerNumero(this, R.array.numeros, spnPeriodo1Alarme);
        Helpers.spinnerNumero(this, R.array.categorias, spnCategoriasAlarme);
        Helpers.spinnerNumero(this, R.array.periodos, spnPeriodoAlarme);
        Helpers.txtHoraConfig(this, txtHoraProcedimento);

        ///arrummar ou arrancar
        swtRepeteAlarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swtRepeteAlarme.isChecked()){
                    swtFrequenciaAlarme.setEnabled(true);
                    txtFrequenciaAlarme.setVisibility(View.VISIBLE);
                    spnPeriodo1Alarme.setVisibility(View.VISIBLE);
                    spnPeriodo0Alarme.setVisibility(View.VISIBLE);
                    spnPeriodoAlarme.setVisibility(View.VISIBLE);
                    // spnRepeticoesAlarme.setSelection(0);
                    // spnRepeticoesAlarme.setEnabled(false);
                }else {
                    swtFrequenciaAlarme.setEnabled(false);
                    txtFrequenciaAlarme.setVisibility(View.INVISIBLE);
                    spnPeriodo1Alarme.setVisibility(View.INVISIBLE);
                    spnPeriodo0Alarme.setVisibility(View.INVISIBLE);
                    spnPeriodoAlarme.setVisibility(View.INVISIBLE);
                    // spnRepeticoesAlarme.setEnabled(true);
                }
            }
        });

        //codigo duplicado
        TextView txt;
        txt = findViewById(R.id.txtFrequencia);
        txt.setText("X");

        spnPeriodo0Alarme.setEnabled(true);

        swtFrequenciaAlarme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    TextView txt;
                    txt = findViewById(R.id.txtFrequencia);
                    txt.setText("EM");

                    spnPeriodo0Alarme.setEnabled(false);
                    spnPeriodo1Alarme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            spnPeriodo0Alarme.setSelection(i);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }else{
                    //codigo duplicado
                    TextView txt;
                    txt = findViewById(R.id.txtFrequencia);
                    txt.setText("X");

                    spnPeriodo0Alarme.setEnabled(true);
                    spnPeriodo1Alarme.setOnItemSelectedListener(null);
                }
            }
        });

        //swtPropocionalAlarme.setClickable(false);
        //swtFrequenciaAlarme.setEnabled(false);

        //spnPeriodoAlarme.setClickable(false);
        //spnPeriodoAlarme.setEnabled(false);

          /*
           listener no proporcional

           adicionar interface para setar o array cal e enviar informaçõe de set de repetição para o alarmReceiver

           5arraycal x 5repeticao (DIA). sugiro que o campo DIA seja passado como multiplicador x7,x24 etc
           susbstituir swp de dois estagios para um de 3 estágios

           listener para mudança de layout
            */


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
            Boolean swtFrequencia = swtFrequenciaAlarme.isChecked();
            String spnRepeticao = spnRepeticoesAlarme.getSelectedItem().toString();
            String spnCategoria = spnCategoriasAlarme.getSelectedItem().toString();
            String spnPeriodo = spnPeriodoAlarme.getSelectedItem().toString();
            String spnPeriodo1 = spnPeriodo1Alarme.getSelectedItem().toString();
            ArrayList<Calendar> alarmeTempo = new ArrayList<>();
            alarmeTempo.add(cal);








            //como montar um id? primeiro digito referente e tabela sendo 0 procedimento
            //+ id do procedimento gravado na tabela
            //if (operacao == OP_INCLUI) {
            int idInserted = insereEstq();
            Toast.makeText(this, "Id inserido:"+idInserted, Toast.LENGTH_SHORT).show();
            AlarmReceiver.startAlarmProcedimento(this, alarmeTempo, idInserted, swtRepete, swtFrequencia, spnPeriodo, spnPeriodo1);
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
