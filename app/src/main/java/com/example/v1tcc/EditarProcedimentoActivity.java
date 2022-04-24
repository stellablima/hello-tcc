package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
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

public class EditarProcedimentoActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "idprocedimento";

    private EditText edtNomeProcedimento;
    private TextView txtHoraProcedimento;
    private Long idProcedimento;
    private BDRotinaHelper bdRotinaHelper;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private Calendar calHoraAlarm;
    private String horaAtual;
    private String minAtual;
    private String horaSelecionadaAlarme = "11:11";
    private TimePickerDialog mTimePicker;


    private TextView txtFrequenciaAlarme;
    private TimePicker tmpHoraAlarme;
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
        setContentView(R.layout.activity_editar_procedimento);

        edtNomeProcedimento = findViewById(R.id.edtNomeProcedimento);
        txtHoraProcedimento = findViewById(R.id.txtHoraProcedimento);
    }

    @Override
    protected void onStart() {
        super.onStart();
        carregaDados();
        configurarCampos();
    }

    private void carregaDados() {
        TextView txt;
        String s;
        int valor; //txtHoraProcedimento
        try {
            idProcedimento = getIntent().getExtras().getLong(EXTRA_ID);
//            Toast.makeText(this, "idProcedimento:"+idProcedimento, Toast.LENGTH_SHORT).show();
            bdRotinaHelper = new BDRotinaHelper(this);
            bd = bdRotinaHelper.getReadableDatabase();
//            // Podemos criar o cursor com rawQuery()
////            //cursor = bd.rawQuery("select _id, CODIGO, NOME, UNID, QTDE, PCUNIT from ESTOQUE where _id = ?",
////            // new String[] {Long.toString(idEstq)} );
            // ou com query(). O rawQuery foi usado na app anterior (Biblioteca) e aqui usamos query()
            cursor = bd.query("PROCEDIMENTO",
                    new String[] {"_id", "NOME", "DATA_PREVISAO"},
                    "_id = ?",
                    new String[] {Long.toString(idProcedimento)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                txt = findViewById(R.id.edtNomeProcedimento);
                txt.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
                txt = findViewById(R.id.txtHoraProcedimento);
//                txt.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATA_PREVISAO")));
//                txt = findViewById(R.id.txtUnid);
//                String unid = cursor.getString(cursor.getColumnIndexOrThrow("UNID"));
////                txt.setText(unid);
////                valor = cursor.getInt(cursor.getColumnIndexOrThrow("QTDE"));
////                if (unid.equals("Kg"))
////                    s = "Qtde. Estq: " + String.format(Locale.getDefault(), "%,.3f", valor/1000.0);
////                else
////                    s = "Qtde. Estq: " + String.format(Locale.getDefault(), "%,d", valor);
////                txt = findViewById(R.id.txtQtde);
////                txt.setText(s);
////                valor = cursor.getInt(cursor.getColumnIndexOrThrow("PCUNIT"));
////                txt = findViewById(R.id.txtPcUnit);
////                txt.setText("Valor: R$ " + String.format(Locale.getDefault(), "%,.2f", valor/100.0));
            }
            else
                Toast.makeText(this, "Produto não encontrado", Toast.LENGTH_SHORT).show();
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Falha no acesso ao Banco de Dados", Toast.LENGTH_LONG).show();
        }
    }

    public void btnAtualizarProcedimentoOnClick(View view){

        //mudar logica de atualizar para apagar todos os alarmes e salvar de novo?
        //tentar manter atualizar para simples e deletar para nao proporcional?
        //seria mais facil so passar o id e mandar deletar tudo e salvar por cima de novo
        /*
        hoje ele sobrescreve com o mesmo id
        */

        try {

            Helpers.preenchimentoValido(edtNomeProcedimento);
            String[] txtHora =  txtHoraProcedimento.getText().toString().split(":");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(txtHora[0]));  // set hour
            cal.set(Calendar.MINUTE, Integer.parseInt(txtHora[1]));          // set minute
            cal.set(Calendar.SECOND, 0);               // set seconds

            //gambiarra provisoria sobrescrevendo com o mesmo idReq
            //AlarmReceiver.updateAlarmProcedimento(this, cal, (idProcedimento).intValue());
            //substituir essa gambiarra por deletetar anteriores e criar outro
            //passando o mesmo id raiz

            ContentValues cv = new ContentValues();
            cv.put("NOME", edtNomeProcedimento.getText().toString());
            //cv.put("DATA_PREVISAO", txtHoraProcedimento.getText().toString());
            BDRotinaHelper bdEstoqueHelper = new BDRotinaHelper(this);
            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
            bd.update("PROCEDIMENTO", cv, "_id = ?", new String[] {Long.toString(idProcedimento)});
            //AlarmReceiver.cancelAlarmDef(this, (idProcedimento).intValue()); //atualizar a hora que vai tocar
            //Toast.makeText(this, "Id inserido:"+idInserted, Toast.LENGTH_SHORT).show();
            finish();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Atualização falhou", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void txtHoraProcedimentoOnClick(View view){
        Helpers.txtHoraConfig(this, txtHoraProcedimento,false);
    }

    //copiado do cadastro,, sujeito a alteração e bugs
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

        Helpers.spinnerNumero(this, R.array.numeros, spnPeriodo0Alarme);
        Helpers.spinnerNumero(this, R.array.numeros, spnPeriodo1Alarme);
        Helpers.spinnerNumero(this, R.array.categorias, spnCategoriasAlarme);
        Helpers.spinnerNumero(this, R.array.periodos, spnPeriodoAlarme);
        Helpers.txtHoraConfig(this, txtHoraProcedimento,true);

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

    }
}