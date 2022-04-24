package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
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
    public static final String EXTRA_PROCEDIMENTO = "extraProcedimento";
    private TextView txt;
    private LinearLayout llAlarmeDistribuido;


    /*DEFINIR AQUI QUANDO CADASTRO QUANDO EDIÇÃO E OTIMIZAR O CODIGO, E RETIRAR BUG SWT DE REPETICAO*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras().getString(EXTRA_PROCEDIMENTO).equals("ADICIONAR_PROCEDIMENTO")){

            configurarCampos();
            spnPeriodoAlarme.setSelection(3);
            spnPeriodoAlarme.setEnabled(false);
            spnPeriodo0Alarme.setSelection(0);

        }
        else if(getIntent().getExtras().getString(EXTRA_PROCEDIMENTO).equals("EDITAR_PROCEDIMENTO")){

            Toast.makeText(this, "configure pra editar e otimize o codigo", Toast.LENGTH_SHORT).show();
            configurarCampos();
        }
    }

    private void configurarCampos(){
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
        txt = findViewById(R.id.txtFrequencia);
        llAlarmeDistribuido = findViewById(R.id.llAlarmeDistribuido);

        Helpers.spinnerNumero(this, R.array.numeros, spnPeriodo0Alarme);
        Helpers.spinnerNumero(this, R.array.numeros, spnPeriodo1Alarme);
        Helpers.spinnerNumero(this, R.array.categorias, spnCategoriasAlarme);
        Helpers.spinnerNumero(this, R.array.periodos, spnPeriodoAlarme);
        Helpers.txtHoraConfig(this, txtHoraProcedimento,true);
        lvRepeticaoDesproporcinalAlarme.setVisibility(View.INVISIBLE);
        llAlarmeDistribuido.setVisibility(View.INVISIBLE);
        spnPeriodo0Alarme.setEnabled(false);                   //pré set que sera apagado e reprogramado repetição
        spnPeriodo1Alarme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                listHorarios.clear();
                for (int count =0; count < (i+1); count++){
                    listHorarios.add("00:00");
                }

                String[] itens = new String[listHorarios.size()];
                listHorarios.toArray(itens);
                Helpers.lvDinamico(view.getContext(), itens, lvRepeticaoDesproporcinalAlarme);

                lvRepeticaoDesproporcinalAlarme.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                        for (int a=0;a<lvRepeticaoDesproporcinalAlarme.getCount();a++){
                            listHorarios.set(a,lvRepeticaoDesproporcinalAlarme.getItemAtPosition(a).toString());
                        }
                    }
                });

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        swtRepeteAlarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swtRepeteAlarme.isChecked()){
                    swtFrequenciaAlarme.setEnabled(true);
                    txtFrequenciaAlarme.setVisibility(View.VISIBLE);
                    llAlarmeDistribuido.setVisibility(View.VISIBLE);

                    if(swtFrequenciaAlarme.isChecked()) {
                        txtHoraProcedimento.setVisibility(View.VISIBLE);
                        lvRepeticaoDesproporcinalAlarme.setVisibility(View.INVISIBLE);
                        spnPeriodoAlarme.setEnabled(true);
                        txt.setText("EM");
                    }else{
                        txtHoraProcedimento.setVisibility(View.INVISIBLE);
                        lvRepeticaoDesproporcinalAlarme.setVisibility(View.VISIBLE);
                        spnPeriodoAlarme.setSelection(3);
                        spnPeriodo0Alarme.setSelection(0);
                        spnPeriodoAlarme.setEnabled(false);
                        txt.setText("X");
                    }
                }else {
                    swtFrequenciaAlarme.setEnabled(false);
                    txtFrequenciaAlarme.setVisibility(View.INVISIBLE);
                    llAlarmeDistribuido.setVisibility(View.INVISIBLE);
                    txtHoraProcedimento.setVisibility(View.VISIBLE);
                    lvRepeticaoDesproporcinalAlarme.setVisibility(View.INVISIBLE);
                }
            }
        });
        swtFrequenciaAlarme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    txt.setText("EM");

                    txtHoraProcedimento.setVisibility(View.VISIBLE);
                    lvRepeticaoDesproporcinalAlarme.setVisibility(View.INVISIBLE);

                    spnPeriodoAlarme.setEnabled(true);
                    spnPeriodo1Alarme.setSelection(0); //para setar o array list com 00:00 aproveitar evento antigo

                    //sobrescreve o evento de mudar o swt com 00:00
                    spnPeriodo1Alarme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            spnPeriodo0Alarme.setSelection(i);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    //spnPeriodo0Alarme.setSelection(0); teria que respeitar o valor do 1
                    //spnPeriodo0Alarme.setSelection(spnPeriodo1Alarme.getSelectedItemPosition());
                    //spnPeriodo1Alarme.setSelection(0);
                    spnPeriodo1Alarme.setSelection(0);
                    spnPeriodo0Alarme.setSelection(0);

                }else{

                    txt.setText("X");

                    txtHoraProcedimento.setVisibility(View.INVISIBLE);
                    lvRepeticaoDesproporcinalAlarme.setVisibility(View.VISIBLE);
                    spnPeriodoAlarme.setSelection(3);
                    spnPeriodoAlarme.setEnabled(false);
                    spnPeriodo0Alarme.setSelection(0);
                    spnPeriodo1Alarme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            resetLvDesproporcional(view.getContext(),i);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    resetLvDesproporcional(getApplicationContext(),0);
                    spnPeriodo1Alarme.setSelection(0);
                }
            }
        });

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

            if(!swtRepete || (swtRepete && swtFrequencia)){
                String[] txtHora =  txtHoraProcedimento.getText().toString().split(":");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(txtHora[0]));
                cal.set(Calendar.MINUTE, Integer.parseInt(txtHora[1]));
                cal.set(Calendar.SECOND, 0);
                alarmeTempo.add(cal);

                Toast.makeText(this, "calendario unico: "+ cal.get(0), Toast.LENGTH_LONG).show();
            }else{
                //resgatar os elementos dos list view
                //preencher o listarray de calendar

                //fazer variavel global porque nao sei pegar escopo que o list view ta inflado, nao da global é so contador de posição, pior hipotese trazendo do helper pa ca a função e conseguir o valor semmpre atuazado
                //String itens = (String) lvRepeticaoDesproporcinalAlarme.getSelectedItem();

                // movito para listener on change para atualizer var assis que ouver alteração
//                for (int i=0;i<lvRepeticaoDesproporcinalAlarme.getCount();i++){
//                    listHorarios.set(i,lvRepeticaoDesproporcinalAlarme.getItemAtPosition(i).toString());
//                }
                for (String horarios:listHorarios) { //parece qque ele so repete o ultimo??nao proporcional



                    //depois de muitos alarmes disparados parece que ta
                    //atrasando os demais




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
                Toast.makeText(this, "calendario array", Toast.LENGTH_SHORT).show();
            }


            //como montar um id? primeiro digito referente e tabela sendo 0 procedimento
            //+ id do procedimento gravado na tabela
            //if (operacao == OP_INCLUI) {
            int idInserted = insereEstq(); // como nao é orientado a obj ainda, o id sera unico no banco concatenado co _n no java para identificar repetições
            if(idInserted == -1)
                Toast.makeText(this, "Inclusão falhou", Toast.LENGTH_LONG).show();
            else{
                Toast.makeText(this, "Id inserido:"+idInserted+"_qtd:"+spnPeriodo1Alarme.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                AlarmReceiver.startAlarmProcedimento(this, alarmeTempo, idInserted, swtRepete, swtFrequencia, spnPeriodo, spnPeriodo1); //endiando 1 e la pego 0?
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
        Boolean swtRepete = swtRepeteAlarme.isChecked();
        Boolean swtFrequencia = swtFrequenciaAlarme.isChecked();
        if(!swtRepete || (swtRepete && swtFrequencia))
            cv.put("DATA_PREVISAO", txtHoraProcedimento.getText().toString());
        else
            cv.put("DATA_PREVISAO", listHorarios.toString()+" "+spnPeriodoAlarme.getSelectedItem());

        //cv.put("UNID", spnUnidade.getSelectedItem().toString());
        //cv.put("QTDE", qtde);
        //cv.put("PCUNIT", pcUnit);
        //preencher demais dados que descrevem o alarme
        //para recuperar na tela de edição
        //posterior//documentação urgente//apenas deixe começar do 0
        return cv;
    }

    private void resetLvDesproporcional(Context context, int i){
        listHorarios.clear();
        for (int count =0; count < (i+1); count++){
            listHorarios.add("00:00");
        }

        String[] itens = new String[listHorarios.size()];
        listHorarios.toArray(itens);
        Helpers.lvDinamico(context, itens, lvRepeticaoDesproporcinalAlarme);

        lvRepeticaoDesproporcinalAlarme.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                for (int a=0;a<lvRepeticaoDesproporcinalAlarme.getCount();a++){
                    listHorarios.set(a,lvRepeticaoDesproporcinalAlarme.getItemAtPosition(a).toString());
                }
            }
        });
    }
}
