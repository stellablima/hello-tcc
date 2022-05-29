package com.example.v1tcc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdicionarProcedimentoActivity extends AppCompatActivity {

    private EditText edtNomeProcedimento;
    private TextView txtHoraProcedimento;
    private TextView txtFrequenciaAlarme;
    private TextView txtProcedimento;
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
    public static final String EXTRA_ID = "idprocedimento";
    private TextView txt;
    private String s;
    private LinearLayout llAlarmeDistribuido;
    private Long idProcedimento;
    private BDRotinaHelper bdRotinaHelper;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private Button btnManterProcedimento;
    private boolean flagAlterarLVBugado = false;
    private String[] dataPrevisaoSplitado;

    /*DEFINIR AQUI QUANDO CADASTRO QUANDO EDIÇÃO E OTIMIZAR O CODIGO, E RETIRAR BUG SWT DE REPETICAO*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_procedimento);

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




//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
////nao sei o que ta rolando, mudar o foco, ele seta certo e depois simplesmente zera tudo
////        if(flagAlterarLVBugado)
////            Helpers.lvDinamico(getApplicationContext(),dataPrevisaoSplitado, lvRepeticaoDesproporcinalAlarme);
////        Toast.makeText(this, dataPrevisaoSplitado[0]+"", Toast.LENGTH_LONG);
//    }

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

        Helpers.spinnerNumero(this, R.array.numeros, spnPeriodo0Alarme);
        Helpers.spinnerNumero(this, R.array.numeros, spnPeriodo1Alarme);
        Helpers.spinnerNumero(this, R.array.categorias, spnCategoriasAlarme);
        Helpers.spinnerNumero(this, R.array.periodos, spnPeriodoAlarme);
        Helpers.txtHoraConfig(this, txtHoraProcedimento,extraProcedimento);
        lvRepeticaoDesproporcinalAlarme.setVisibility(View.INVISIBLE);
        llAlarmeDistribuido.setVisibility(View.INVISIBLE);
        spnPeriodo0Alarme.setSelection(0);
        spnPeriodo0Alarme.setEnabled(false);
        spnPeriodoAlarme.setSelection(3);
        spnPeriodoAlarme.setEnabled(false);

    }

    private void carregaDados() {
        /*
        logica deletar e editar


        pendente preecnher arrays e txt na linha 316
        */

        try { //pode ver a logica de deletar se quiser pegar os alarmes
            idProcedimento = getIntent().getExtras().getLong(EXTRA_ID);
            bdRotinaHelper = new BDRotinaHelper(this);
            bd = bdRotinaHelper.getReadableDatabase();
            cursor = bd.query("PROCEDIMENTO",
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
                        lvRepeticaoDesproporcinalAlarme.setVisibility(View.INVISIBLE);
                        spnPeriodoAlarme.setEnabled(true);
                        txt.setText("EM");
                        spnPeriodo1Alarme.setSelection(intSpnPeriodo1Alarme);
                        spnPeriodo0Alarme.setSelection(spnPeriodo1Alarme.getSelectedItemPosition());
                        txtHoraProcedimento.setText(dataPrevisaoSplitado[0]);
                    }else{
                        txtHoraProcedimento.setVisibility(View.INVISIBLE);
                        lvRepeticaoDesproporcinalAlarme.setVisibility(View.VISIBLE);
                        spnPeriodoAlarme.setEnabled(false);
                        txt.setText("X");
                        spnPeriodo0Alarme.setSelection(0);
                        spnPeriodo1Alarme.setSelection(intSpnPeriodo1Alarme);//ja esta zerado ai ele preenche com mais zeros

                        flagAlterarLVBugado = true;
                        //itens
                        //porquqe nao preencheu, ele precnhe e zera logo em seguida
                        //tentando mexer com a var global list horario
//                        listHorarios.clear();
//                        for (int count =0; count < (i+1); count++){
//                            listHorarios.add("00:00");
//                        }

                        //String[] itens = new String[listHorarios.size()];
                        //listHorarios.toArray(dataPrevisaoSplitado);
                        //Helpers.lvDinamico(getApplicationContext(),dataPrevisaoSplitado, lvRepeticaoDesproporcinalAlarme);





//                        ArrayAdapter<String> adapter = new ArrayAdapter<String> ( getApplicationContext(),
//                                android.R.layout.simple_list_item_1, dataPrevisaoSplitado );
//
//                        lvRepeticaoDesproporcinalAlarme.setAdapter(null);
//                        lvRepeticaoDesproporcinalAlarme.setAdapter(adapter);
//                        lvRepeticaoDesproporcinalAlarme.setOnItemClickListener( new AdapterView.OnItemClickListener() {
//
//                            @Override
//                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//
//                                Helpers.lvHoraConfig(getApplicationContext(), dataPrevisaoSplitado, lvRepeticaoDesproporcinalAlarme,arg2);
//                            }
//                        } );






                        //Helpers.lvDinamico(context, itens, lvRepeticaoDesproporcinalAlarme);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //resetLvDesproporcional(getApplicationContext(),i);
                        //Toast.makeText(getApplicationContext(),"dataPrevisaoSplitado:" + dataPrevisaoSplitado[0]+dataPrevisaoSplitado[1], Toast.LENGTH_SHORT).show();
                    }
                }else
                    txtHoraProcedimento.setText(dataPrevisaoSplitado[0]);
            }
            else
                Toast.makeText(this, "Procedimento não encontrado", Toast.LENGTH_SHORT).show();
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Falha no acesso ao Banco de Dados "+e, Toast.LENGTH_LONG).show();
        }
    }



    public void btnEditarProcedimentoOnClick(View view){
        Toast.makeText(this,"editeiiii", Toast.LENGTH_LONG).show();
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
            BDRotinaHelper bdEstoqueHelper = new BDRotinaHelper(this);
            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
            bd.update("PROCEDIMENTO", cv, "_id = ?", new String[] {Long.toString(idProcedimento)});

            //recuperar tamanho do alarme
            bd = bdRotinaHelper.getReadableDatabase();
            cursor = bd.query("PROCEDIMENTO",
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
            bd.close();

            AlarmReceiver.cancelAlarmDef(this, (idProcedimento).intValue(), qtdDisparos);//, qtdDisparos); //ate salvar no banco ou achar outra logica
            //finish();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Deleção falhou", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

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


            //if (operacao == OP_INCLUI) {
            int idInserted = insereEstq(); // como nao é orientado a obj ainda, o id sera unico no banco concatenado co _n no java para identificar repetições
            if(idInserted == -1)
                Toast.makeText(this, "Inclusão falhou "+"-1", Toast.LENGTH_LONG).show();
            else{
                //Toast.makeText(this, "Id inserido:"+idInserted+"_qtd:"+spnPeriodo1Alarme.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Id inserido:"+idInserted+"_qtd:"+alarmeTempo.size(), Toast.LENGTH_SHORT).show();
                AlarmReceiver.startAlarmProcedimento(this, alarmeTempo, idInserted, swtRepete, swtFrequencia, spnPeriodo, spnPeriodo1); //endiando 1 e la pego 0?
            }

            //}
            //else if (operacao == OP_ALTERA) {
            //    alteraEstq();
            //    finish();
            //}
            finish();


        } catch (SQLiteException e) {
            Toast.makeText(this, "Inclusão falhou "+e, Toast.LENGTH_LONG).show();
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
        cv.put("CATEGORIA", spnCategoriasAlarme.getSelectedItem().toString());
        cv.put("FLAG", "1");




        Boolean swtRepete = swtRepeteAlarme.isChecked();
        Boolean swtFrequencia = swtFrequenciaAlarme.isChecked();
        String FLAGREPETICAO;
        String FLAGFREQUENCIA;


        //arrumar QTDDISPAROS  > 1 so quando for swtrepete e swtfrequencia
        String qtdDisparos;

        if(swtRepete && !swtFrequencia)
            qtdDisparos = spnPeriodo1Alarme.getSelectedItem().toString();
        else
            qtdDisparos = "1";

        cv.put("QTDDISPAROS", qtdDisparos); //spnPeriodo1Alarme.getSelectedItem().toString()

/*?situação, salvou o layout certo mas passou horario errado horario array quando deveria ser 1 cara so?*/

        //rever
        if(swtRepete)
            FLAGREPETICAO="1";
        else
            FLAGREPETICAO="0";
        if(swtFrequencia) // a frequencia pode estar em 1 mas se o repete tiver disabled de nada vale
            FLAGFREQUENCIA="1";//na real nao importa porque isso é um switch e vai ter valor de qqr forma
        else
            FLAGFREQUENCIA="0";
        cv.put("FLAG_REPETICAO", FLAGREPETICAO); //comentar ajudou no bug de limpar cache?
        cv.put("FLAG_FREQUENCIA", FLAGFREQUENCIA);
        //o que ele vai salvar no futuro no banco vai ser um alarme com uma configuração e uma peridiocidade, e o que vai
        //persistir no banco é quando a ação for realizada os dados mínimos para relatório, toda a mágica fora o crud
        //vai acontecer ali e pode ser necessário rependar melhor esse módulo, vai ser um tabelão de crescimento esponencial com indexes
        //mas tbm pode filtrar gerar o relatório e apagar os dados mais antigos periodicamente

        //forlar bug aqui
        if(!swtRepete) { // se alarme unico
            cv.put("DATA_PREVISAO", "["+txtHoraProcedimento.getText().toString()+"]");
        }
        else if(swtRepete && swtFrequencia) { // aqui importa
            cv.put("DATA_PREVISAO", "["+txtHoraProcedimento.getText().toString()+"]"+spnPeriodo1Alarme.getSelectedItem()+txt.getText()+spnPeriodo0Alarme.getSelectedItem()+spnPeriodoAlarme.getSelectedItem());
        }
        else {
            cv.put("DATA_PREVISAO", listHorarios.toString()+""+spnPeriodo1Alarme.getSelectedItem()+txt.getText()+spnPeriodo0Alarme.getSelectedItem()+spnPeriodoAlarme.getSelectedItem());
        }

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
