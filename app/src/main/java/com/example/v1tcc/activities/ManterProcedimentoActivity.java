package com.example.v1tcc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v1tcc.AlarmReceiver;
import com.example.v1tcc.BDHelper.SQLiteConnection;
import com.example.v1tcc.Helpers;
import com.example.v1tcc.R;
import com.example.v1tcc.controller.ProcedimentoController;
import com.example.v1tcc.models.Procedimento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ManterProcedimentoActivity extends AppCompatActivity {

    public static final String EXTRA_PROCEDIMENTO = "extraProcedimento";
    public static final String EXTRA_ID = "idprocedimento";

    private EditText edtNomeProcedimento;
    private TextView txtHoraProcedimento;
    private TextView txtFrequenciaAlarme;
    private TextView txtProcedimento;
    private Spinner spnCategoriasAlarme;
    private Spinner spnPeriodoAlarme;
    private Spinner spnPeriodo0Alarme;
    private Spinner spnPeriodo1Alarme;
    private Switch swtRepeteAlarme;
    private Switch swtFrequenciaAlarme;
    private Button btnManterProcedimento;
    private ListView lvRepeticaoDesproporcinalAlarme;
    private LinearLayout llAlarmeDistribuido;

    private SQLiteDatabase SQLiteDatabase;
    private SQLiteConnection SQLiteConnection;
    private Cursor cursor;
    private Procedimento procedimento;
    private Long idProcedimento;

    private String[] dataPrevisaoSplitado;
    private List<String> listHorarios = new ArrayList<String>(); //para uso em mais de um função, mmelhor implemmentação seria pegar do commponente na hora
    private TextView txt;

    //    private boolean flagAlterarLVBugado = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_procedimento);

        SQLiteConnection = SQLiteConnection.getInstanciaConexao(this);
    }

    @Override
    protected void onStart(){
        super.onStart();


        if(getIntent().getExtras().getString(EXTRA_PROCEDIMENTO).equals("ADICIONAR_PROCEDIMENTO")){
            configurarCampos(true);
            btnManterProcedimento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarProcedimentoOnClick(view);
                }
            });
        }
        else if(getIntent().getExtras().getString(EXTRA_PROCEDIMENTO).equals("EDITAR_PROCEDIMENTO")){

            configurarCampos(false);
            carregaDados();
            txtProcedimento.setText("Editar Procedimento");
            btnManterProcedimento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { btnEditarProcedimentoOnClick(view); }
            });

//            if(flagAlterarLVBugado)
//                Helpers.lvDinamico(getApplicationContext(),dataPrevisaoSplitado, lvRepeticaoDesproporcinalAlarme);
        }
    }


    private void configurarCampos(Boolean extraProcedimento){

        btnManterProcedimento =  findViewById(R.id.btnManterProcedimento);
        edtNomeProcedimento = findViewById(R.id.edtNomeProcedimento);
        txtHoraProcedimento = findViewById(R.id.txtHoraProcedimento);
        txtFrequenciaAlarme = findViewById(R.id.txtFrequencia);
        txtProcedimento = findViewById(R.id.txtProcedimento);
        spnCategoriasAlarme = findViewById(R.id.spnCategoriasAlarme);
        spnPeriodoAlarme = findViewById(R.id.spnPeriodoAlarme);
        swtRepeteAlarme = findViewById(R.id.swtRepeteAlarme);
        swtFrequenciaAlarme = findViewById(R.id.swtFrequenciaAlarme);
        spnPeriodo1Alarme = findViewById(R.id.spnPeriodo1);
        spnPeriodo0Alarme = findViewById(R.id.spnPeriodo0);
        lvRepeticaoDesproporcinalAlarme = findViewById(R.id.lvRepeticaoDesproporcinal);
        txt = findViewById(R.id.txtFrequencia);
        llAlarmeDistribuido = findViewById(R.id.llAlarmeDistribuido);

        spnPeriodo1Alarme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                resetLvDesproporcional(view.getContext(),i);
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
                        lvRepeticaoDesproporcinalAlarme.setVisibility(View.GONE);
                        spnPeriodoAlarme.setEnabled(true);
                        txt.setText("EM");
                    }else{
                        txtHoraProcedimento.setVisibility(View.GONE);
                        lvRepeticaoDesproporcinalAlarme.setVisibility(View.VISIBLE);
                        spnPeriodoAlarme.setSelection(3);
                        spnPeriodo0Alarme.setSelection(0);
                        spnPeriodoAlarme.setEnabled(false);
                        txt.setText("X");
                    }
                }else {
                    swtFrequenciaAlarme.setEnabled(false);
                    txtFrequenciaAlarme.setVisibility(View.GONE);
                    llAlarmeDistribuido.setVisibility(View.GONE);
                    txtHoraProcedimento.setVisibility(View.VISIBLE);
                    lvRepeticaoDesproporcinalAlarme.setVisibility(View.GONE);
                }
            }
        });
        swtFrequenciaAlarme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    txt.setText("EM");

                    txtHoraProcedimento.setVisibility(View.VISIBLE);
                    lvRepeticaoDesproporcinalAlarme.setVisibility(View.GONE);

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

                    spnPeriodo1Alarme.setSelection(0);
                    spnPeriodo0Alarme.setSelection(0);

                }else{

                    txt.setText("X");

                    txtHoraProcedimento.setVisibility(View.GONE);
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

        Helpers.spinnerNumero(this, R.array.numeros, spnPeriodo0Alarme);
        Helpers.spinnerNumero(this, R.array.numeros, spnPeriodo1Alarme);
        Helpers.spinnerNumero(this, R.array.categorias, spnCategoriasAlarme);
        Helpers.spinnerNumero(this, R.array.periodos, spnPeriodoAlarme);
        Helpers.txtHoraConfig(this, txtHoraProcedimento,extraProcedimento);
        lvRepeticaoDesproporcinalAlarme.setVisibility(View.GONE);
        llAlarmeDistribuido.setVisibility(View.GONE);
        spnPeriodo0Alarme.setSelection(0);
        spnPeriodo0Alarme.setEnabled(false);
        spnPeriodoAlarme.setSelection(3);
        spnPeriodoAlarme.setEnabled(false);

    }

    private void carregaDados() {

        try {
            idProcedimento = getIntent().getExtras().getLong(EXTRA_ID);
            //SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase = SQLiteConnection.getReadableDatabase();
            cursor = SQLiteDatabase.query("PROCEDIMENTO",
                    new String[] {"_id", "NOME", "DATA_PREVISAO", "CATEGORIA", "QTDDISPAROS", "FLAG_REPETICAO", "FLAG_FREQUENCIA"},
                    "_id = ?",
                    new String[] {Long.toString(idProcedimento)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {

                edtNomeProcedimento.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));

                String categoriaAlarme = cursor.getString(cursor.getColumnIndexOrThrow("CATEGORIA"));
                String qtdDisparosAlarme = cursor.getString(cursor.getColumnIndexOrThrow("QTDDISPAROS"));
                String flagRepeticaoAlarme = cursor.getString(cursor.getColumnIndexOrThrow("FLAG_REPETICAO"));
                String flagFrequenciaAlarme = cursor.getString(cursor.getColumnIndexOrThrow("FLAG_FREQUENCIA"));
                String dataPrevisaoAlarme = cursor.getString(cursor.getColumnIndexOrThrow("DATA_PREVISAO"));
                switch (categoriaAlarme + "") {
                    case "Medicação":
                        spnCategoriasAlarme.setSelection(0);
                        break;
                    case "Higienização":
                        spnCategoriasAlarme.setSelection(1);
                        break;
                    default: //"Outro"
                        spnCategoriasAlarme.setSelection(2);
                        break;
                }

                //List<String> dataPrevisaoSplitado = new ArrayList<String>();
                String dataPrevisaoSplitadoTxt = dataPrevisaoAlarme.substring(dataPrevisaoAlarme.indexOf("[")+1, dataPrevisaoAlarme.indexOf("]"));
                dataPrevisaoSplitado = dataPrevisaoSplitadoTxt.split(", ");

                if (flagRepeticaoAlarme.equals("1")){
                    swtRepeteAlarme.setChecked(true);
                    String textoParenteses = dataPrevisaoAlarme.substring(dataPrevisaoAlarme.indexOf("(")+1, dataPrevisaoAlarme.lastIndexOf(")"));
                    switch (textoParenteses){
                        case "MINUTO":
                            spnPeriodoAlarme.setSelection(0);
                            break;
                        case "HORAS":
                            spnPeriodoAlarme.setSelection(1);
                            break;
                        case "DIA S/N":
                            spnPeriodoAlarme.setSelection(2);
                            break;
                        case "DIA":
                            spnPeriodoAlarme.setSelection(3);
                            break;
                        case "SEMANA":
                            spnPeriodoAlarme.setSelection(4);
                            break;
                        case "MÊS":
                            spnPeriodoAlarme.setSelection(5);
                            break;
                        case "ANO":
                            spnPeriodoAlarme.setSelection(6);
                            break;
                        default:
                            spnPeriodoAlarme.setSelection(3);
                            break;
                    }

                    int intSpnPeriodo1Alarme = (Integer.parseInt(dataPrevisaoAlarme.substring(dataPrevisaoAlarme.indexOf("]")+1, dataPrevisaoAlarme.indexOf("]")+2)))-1;

                    swtFrequenciaAlarme.setEnabled(true);
                    llAlarmeDistribuido.setVisibility(View.VISIBLE);

                    if (flagFrequenciaAlarme.equals("1")) {
                        swtFrequenciaAlarme.setChecked(true);
                        lvRepeticaoDesproporcinalAlarme.setVisibility(View.GONE);
                        spnPeriodoAlarme.setEnabled(true);
                        txt.setText("EM");
                        spnPeriodo1Alarme.setSelection(intSpnPeriodo1Alarme);
                        spnPeriodo0Alarme.setSelection(spnPeriodo1Alarme.getSelectedItemPosition());
                        txtHoraProcedimento.setText(dataPrevisaoSplitado[0]);
                    }else{
                        txtHoraProcedimento.setVisibility(View.GONE);
                        lvRepeticaoDesproporcinalAlarme.setVisibility(View.VISIBLE);
                        spnPeriodoAlarme.setEnabled(false);
                        txt.setText("X");
                        spnPeriodo0Alarme.setSelection(0);
                        spnPeriodo1Alarme.setSelection(intSpnPeriodo1Alarme);//ja esta zerado ai ele preenche com mais zeros

                    }
                }else
                    txtHoraProcedimento.setText(dataPrevisaoSplitado[0]);
            }
            else
                Toast.makeText(this, "Procedimento não encontrado", Toast.LENGTH_SHORT).show();

            SQLiteDatabase.close();
            //SQLiteConnection.close();
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Falha no acesso ao Banco de Dados "+e, Toast.LENGTH_LONG).show();
        }
    }

    public void btnEditarProcedimentoOnClick(View view){
        //Toast.makeText(this,"editeiiii", Toast.LENGTH_LONG).show();
        //excluir alarmes
        btnDeletarProcedimentoOnClick(view);
        //salvar um novo alarme
        btnSalvarProcedimentoOnClick(view);
        //rever logica:
        //realmente deletou os alarmes multiplos?
        //vale a pena manter no banco com flag desativada
        //ele iria criar outro cara com outro id pro banco e o mesmo id pro alarm? faz sentido
        //r: nao, ele deletou, criando assim outro alarme e deixando o antigo na banco com flag desativada
        //seria legal manter um historico entao
        //r: como nao teria nada que ligasse o historico com o alterado acho que é worth deletar do banco depois

    }

    public void btnDeletarProcedimentoOnClick(View view){
        /******************************************************/
        try {

            ContentValues cv = new ContentValues();
            cv.put("FLAG", "0");
            //SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
            SQLiteDatabase = SQLiteConnection.getWritableDatabase();
            SQLiteDatabase.update("PROCEDIMENTO", cv, "_id = ?", new String[] {Long.toString(idProcedimento)});

            SQLiteDatabase.close();
            //recuperar tamanho do alarme
            SQLiteDatabase = SQLiteConnection.getReadableDatabase();
            cursor = SQLiteDatabase.query("PROCEDIMENTO",
                    new String[] {"_id", "QTDDISPAROS"},
                    "_id = ?",
                    new String[] {Long.toString(idProcedimento)},
                    null,
                    null,
                    null
            );
            int qtdDisparos=1;
            if (cursor.moveToFirst())
                qtdDisparos = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("QTDDISPAROS")));
            else{
                Toast.makeText(this, "Disparos nao encontrados", Toast.LENGTH_SHORT).show();
                //tem que dar exeption, provavelmente o sql de?
            }
            //new
            SQLiteDatabase.close();

            AlarmReceiver.cancelAlarmDef(this, (idProcedimento).intValue(), qtdDisparos);//, qtdDisparos); //ate salvar no banco ou achar outra logica
            finish();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Deleção falhou", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void btnSalvarProcedimentoOnClick(View view){

        try {

            //validação
            Helpers.preenchimentoValido(edtNomeProcedimento);

            Boolean swtRepete = swtRepeteAlarme.isChecked();
            Boolean swtFrequencia = swtFrequenciaAlarme.isChecked();
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

            }else{

                for (String horarios:listHorarios) {

                    String[] txtHora =  horarios.split(":");
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(txtHora[0]));
                    cal.set(Calendar.MINUTE, Integer.parseInt(txtHora[1]));
                    cal.set(Calendar.SECOND, 0);
                    alarmeTempo.add(cal);
                }
            }

            long idProcedimento = insereProcedimento();

            if(idProcedimento == -1)
                Toast.makeText(this, "Inclusão falhou "+"-1", Toast.LENGTH_LONG).show();
            else{
                AlarmReceiver.startAlarmProcedimento(this, alarmeTempo, idProcedimento, swtRepete, swtFrequencia, spnPeriodo, spnPeriodo1);
            }

            finish();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Inclusão falhou "+e, Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private long insereProcedimento(){
        ProcedimentoController procedimentoController =  new ProcedimentoController(SQLiteConnection);
        return procedimentoController.createProcedimentoController(getProcedimentoActivity());
    }

    private Procedimento getProcedimentoActivity(){

        this.procedimento = new Procedimento();

        if(this.edtNomeProcedimento.getText().toString().isEmpty() == false)
            this.procedimento.setNOME(edtNomeProcedimento.getText().toString());
        //else return null;

        this.procedimento.setCATEGORIA(spnCategoriasAlarme.getSelectedItem().toString());

        this.procedimento.setFLAG("1");

        if(swtRepeteAlarme.isChecked() && !swtFrequenciaAlarme.isChecked())
            this.procedimento.setQTDDISPAROS(spnPeriodo1Alarme.getSelectedItem().toString());
        else
            this.procedimento.setQTDDISPAROS("1");

        if(swtRepeteAlarme.isChecked())
            this.procedimento.setFLAG_REPETICAO("1");
        else
            this.procedimento.setFLAG_REPETICAO("0");

        if(swtFrequenciaAlarme.isChecked())
            this.procedimento.setFLAG_FREQUENCIA("1");
        else
            this.procedimento.setFLAG_FREQUENCIA("0");

        if(!swtRepeteAlarme.isChecked())
            this.procedimento.setDATA_PREVISAO("["+txtHoraProcedimento.getText().toString()+"]");
        else if(swtRepeteAlarme.isChecked() && swtFrequenciaAlarme.isChecked())
            this.procedimento.setDATA_PREVISAO("["+txtHoraProcedimento.getText().toString()+"]"+spnPeriodo1Alarme.getSelectedItem()+txt.getText()+spnPeriodo0Alarme.getSelectedItem()+spnPeriodoAlarme.getSelectedItem());
        else
            this.procedimento.setDATA_PREVISAO(listHorarios.toString()+""+spnPeriodo1Alarme.getSelectedItem()+txt.getText()+spnPeriodo0Alarme.getSelectedItem()+spnPeriodoAlarme.getSelectedItem());

        return procedimento;
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
