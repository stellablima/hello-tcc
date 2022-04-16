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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdicionarProcedimentoActivity extends AppCompatActivity {

    private EditText edtNomeProcedimento;
    private TextView txtHoraProcedimento;
    private TextView txtFrequenciaAlarme;
    private TimePicker tmpHoraAlarme;
    private Calendar calHoraAlarm;
    private String horaAtual;
    private String minAtual;
    private TimePickerDialog mTimePicker;
    private Spinner spnCategoriasAlarme;
    private Spinner spnPeriodoAlarme;
    private Spinner spnPeriodo0Alarme;
    private Spinner spnPeriodo1Alarme;
    private Switch swtRepeteAlarme;
    private Switch swtFrequenciaAlarme;
    private ListView lvRepeticaoDesproporcinalAlarme;
    List<String> listHorarios = new ArrayList<String>(); //para uso em mais de um função, mmelhor implemmentação seria pegar do commponente na hora


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_procedimento);

        edtNomeProcedimento = findViewById(R.id.edtNomeProcedimento);
        txtHoraProcedimento = findViewById(R.id.txtHoraProcedimento);
        txtFrequenciaAlarme = findViewById(R.id.txtFrequencia);
        spnCategoriasAlarme = findViewById(R.id.spnCategoriasAlarme);
        spnPeriodoAlarme = findViewById(R.id.spnPeriodoAlarme);
        swtRepeteAlarme = findViewById(R.id.swtRepeteAlarme);
        swtFrequenciaAlarme = findViewById(R.id.swtFrequenciaAlarme);
        spnPeriodo1Alarme = findViewById(R.id.spnPeriodo1);
        spnPeriodo0Alarme = findViewById(R.id.spnPeriodo0);
        lvRepeticaoDesproporcinalAlarme = findViewById(R.id.lvRepeticaoDesproporcinal);

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

                    if(!swtFrequenciaAlarme.isChecked()) {
                        txtHoraProcedimento.setVisibility(View.INVISIBLE);
                        lvRepeticaoDesproporcinalAlarme.setVisibility(View.VISIBLE);
                    }
                }else {
                    swtFrequenciaAlarme.setEnabled(false);
                    txtFrequenciaAlarme.setVisibility(View.INVISIBLE);
                    spnPeriodo1Alarme.setVisibility(View.INVISIBLE);
                    spnPeriodo0Alarme.setVisibility(View.INVISIBLE);
                    spnPeriodoAlarme.setVisibility(View.INVISIBLE);
                    txtHoraProcedimento.setVisibility(View.VISIBLE);
                    lvRepeticaoDesproporcinalAlarme.setVisibility(View.INVISIBLE);
                }
            }
        });

        //codigo duplicado
        TextView txt;
        txt = findViewById(R.id.txtFrequencia);
        txt.setText("X");

        //txtHoraProcedimento.setVisibility(View.INVISIBLE);
        spnPeriodoAlarme.setSelection(3);
        spnPeriodoAlarme.setEnabled(false);
        spnPeriodo0Alarme.setSelection(0);
        spnPeriodo0Alarme.setEnabled(false);
        spnPeriodo1Alarme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Toast.makeText( view.getContext(), ":"+  (i+1), Toast.LENGTH_SHORT ).show();
                //List<String> listHorarios = new ArrayList<String>();//global por necessidade, reverter pois nao funcionou, vai ter que ser com extras

                listHorarios.clear();
                for (int count =0; count < (i+1); count++){
                    listHorarios.add("00:00");
                }

                String[] itens = new String[ listHorarios.size() ];
                listHorarios.toArray(itens);
                //setContentView(R.layout.activity_adicionar_procedimento);
                Helpers.lvDinamico(view.getContext(), itens, lvRepeticaoDesproporcinalAlarme);

                //atualize o listview
                lvRepeticaoDesproporcinalAlarme.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View view, int ii, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                        for (int i=0;i<lvRepeticaoDesproporcinalAlarme.getCount();i++){
                            listHorarios.set(i,lvRepeticaoDesproporcinalAlarme.getItemAtPosition(i).toString());
                        }
                    }
                });

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        swtFrequenciaAlarme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    TextView txt;
                    txt = findViewById(R.id.txtFrequencia);
                    txt.setText("EM");

                    txtHoraProcedimento.setVisibility(View.VISIBLE); ////
                    lvRepeticaoDesproporcinalAlarme.setVisibility(View.INVISIBLE);
                    /*
                    0- DAR DE CHECK PARA UM INTERRUPTOR
                    1- DESAPARECE HORARIO
                    2- HABILITA ARRAY
                    3- PEGA A FUNCAO DE BRIR POUPUP PARA HABILITAR
                    4- VER COMO PROGRAMAR ALARMES POR TURNO
                    5- EXECUTAR TODOS OS MANUAIS E NA REPETICAO REAGENDAR TUDO AO RECEBER ULTIMA REPETICAO OU ALGO ASSIM
                    */

                    spnPeriodoAlarme.setEnabled(true);
                    spnPeriodo0Alarme.setEnabled(false);
                    spnPeriodo1Alarme.setSelection(0); //para setar o array list com 00:00 aproveitar evento antigo
                    spnPeriodo1Alarme.setOnItemSelectedListener(null);
                    spnPeriodo1Alarme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            spnPeriodo0Alarme.setSelection(i);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    spnPeriodo0Alarme.setSelection(0);
                    spnPeriodo1Alarme.setSelection(0);

                }else{
                    //codigo duplicado 2
                    //codigo duplicado
                    TextView txt;
                    txt = findViewById(R.id.txtFrequencia);
                    txt.setText("X");

                    txtHoraProcedimento.setVisibility(View.INVISIBLE);
                    lvRepeticaoDesproporcinalAlarme.setVisibility(View.VISIBLE);
                    spnPeriodoAlarme.setSelection(3); //(dia)
                    spnPeriodoAlarme.setEnabled(false);
                    spnPeriodo1Alarme.setOnItemSelectedListener(null);
                    spnPeriodo1Alarme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            //Toast.makeText( view.getContext(), ":"+  (i+1), Toast.LENGTH_SHORT ).show();
                            List<String> listHorarios = new ArrayList<String>();
                            listHorarios.clear();

                            for (int count =0; count < (i+1); count++){
                                listHorarios.add("00:00");
                            }

                            String[] itens = new String[ listHorarios.size() ];
                            listHorarios.toArray(itens);
                            //setContentView(R.layout.activity_adicionar_procedimento);
                            Helpers.lvDinamico(view.getContext(), itens, lvRepeticaoDesproporcinalAlarme);

                            //atualize o listview
                            lvRepeticaoDesproporcinalAlarme.addOnLayoutChangeListener(null);
                            lvRepeticaoDesproporcinalAlarme.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                @Override
                                public void onLayoutChange(View view, int ii, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                                    for (int i=0;i<lvRepeticaoDesproporcinalAlarme.getCount();i++){
                                        listHorarios.set(i,lvRepeticaoDesproporcinalAlarme.getItemAtPosition(i).toString());
                                    }
                                }
                            });

                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }


                    });

                    spnPeriodo1Alarme.setSelection(0);
                    spnPeriodo0Alarme.setSelection(0);
                    spnPeriodo0Alarme.setEnabled(false);
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

            Boolean swtRepete = swtRepeteAlarme.isChecked();
            Boolean swtFrequencia = swtFrequenciaAlarme.isChecked();
            String spnCategoria = spnCategoriasAlarme.getSelectedItem().toString();
            String spnPeriodo = spnPeriodoAlarme.getSelectedItem().toString();
            String spnPeriodo1 = spnPeriodo1Alarme.getSelectedItem().toString();
            ArrayList<Calendar> alarmeTempo = new ArrayList<>();

            if(swtRepete && swtFrequencia){
                String[] txtHora =  txtHoraProcedimento.getText().toString().split(":");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(txtHora[0]));
                cal.set(Calendar.MINUTE, Integer.parseInt(txtHora[1]));
                cal.set(Calendar.SECOND, 0);
                alarmeTempo.add(cal);

            }else{
                //resgatar os elementos dos list view
                //preencher o listarray de calendar

                //fazer variavel global porque nao sei pegar escopo que o list view ta inflado, nao da global é so contador de posição, pior hipotese trazendo do helper pa ca a função e conseguir o valor semmpre atuazado
                //String itens = (String) lvRepeticaoDesproporcinalAlarme.getSelectedItem();

                // movito para listener on change para atualizer var assis que ouver alteração
//                for (int i=0;i<lvRepeticaoDesproporcinalAlarme.getCount();i++){
//                    listHorarios.set(i,lvRepeticaoDesproporcinalAlarme.getItemAtPosition(i).toString());
//                }
                for (String horarios:listHorarios) {
                    String[] txtHora =  horarios.split(":");
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(txtHora[0]));
                    cal.set(Calendar.MINUTE, Integer.parseInt(txtHora[1]));
                    cal.set(Calendar.SECOND, 0);
                    alarmeTempo.add(cal);
                }
                //Toast.makeText(this, "itens:"+alarmeTempo.size(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "itens:"+alarmeTempo.get(0).getTime()+"_"+alarmeTempo.get(1).getTime(), Toast.LENGTH_SHORT).show();
                //return;
            }


            //como montar um id? primeiro digito referente e tabela sendo 0 procedimento
            //+ id do procedimento gravado na tabela
            //if (operacao == OP_INCLUI) {

            //SE RECEBO -1 É CATCH
            int idInserted = insereEstq(); // como nao é orientado a obj ainda, o id sera unico no banco concatenado co _n no java para identificar repetições

            if(idInserted == -1){

                Toast.makeText(this, "Inclusão falhou", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Id inserido:"+idInserted, Toast.LENGTH_SHORT).show();
                AlarmReceiver.startAlarmProcedimento(this, alarmeTempo, idInserted, swtRepete, swtFrequencia, spnPeriodo, spnPeriodo1);
            }

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

    //retornar lista de ids referentes para preparar para alarmes nao proporcionais?
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
        cv.put("QTDDISPAROS", spnPeriodo1Alarme.getSelectedItem().toString()); //spnPeriodo1Alarme.getSelectedItem().toString()
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
