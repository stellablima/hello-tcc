package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AdicionarTarefaActivity extends AppCompatActivity {

    public static final String EXTRA_TAREFA = "extraTarefa";
    private Button btnSalvarTarefa ;
    private Button btnFecharSalvarTarefa;
    private EditText edtNomeTarefa;
    private EditText edtObservacaoTarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(getIntent().getExtras().getString(EXTRA_TAREFA).equals("ADICIONAR_TAREFA")){
            configurarCampos(true);

        }
        else if(getIntent().getExtras().getString(EXTRA_TAREFA).equals("EDITAR_TAREFA")){
//
//            configurarCampos(false);
//            carregaDados();
//            txtProcedimento.setText("Editar Procedimento");
//            btnManterProcedimento.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) { btnEditarProcedimentoOnClick(view); }
//            });
//
////            if(flagAlterarLVBugado)
////                Helpers.lvDinamico(getApplicationContext(),dataPrevisaoSplitado, lvRepeticaoDesproporcinalAlarme);
        }
    }

    private void configurarCampos(Boolean extraTarefa){
        edtNomeTarefa = findViewById(R.id.edtNomeTarefa);
        edtObservacaoTarefa = findViewById(R.id.edtObservacaoTarefa);
        btnSalvarTarefa = findViewById(R.id.btnSalvarTarefa);
        btnSalvarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSalvarTarefaOnClick(view);
            }
        });
        btnFecharSalvarTarefa = findViewById(R.id.btnFecharSalvarTarefa);
        btnFecharSalvarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFecharSalvarTarefaOnClick(view);
            }
        });

    }

    private void carregaDados(){
        try { //pode ver a logica de deletar se quiser pegar os alarmes
          /* idProcedimento = getIntent().getExtras().getLong(EXTRA_ID);
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
                Toast.makeText(this, "Procedimento não encontrado", Toast.LENGTH_SHORT).show();*/
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Falha no acesso ao Banco de Dados "+e, Toast.LENGTH_LONG).show();
        }
    }

    private void btnSalvarTarefaOnClick(View view){
        try {

            Helpers.preenchimentoValido(edtNomeTarefa);

            int idInserted = insereTarefa();
            if(idInserted == -1)
                Toast.makeText(this, "Inclusão falhou "+"-1", Toast.LENGTH_LONG).show();
            else{
                Toast.makeText(this, "Id inserido:"+idInserted, Toast.LENGTH_SHORT).show();
            }
            finish();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Inclusão falhou "+e, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void btnFecharSalvarTarefaOnClick(View view){
        //finish();

        //ficou ridiculo o branco que da mas pra agora ta pft,
        //replicar nos outros botões fechar, talvez colocar a classe um um helper
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //criar função excluir sem concluir tarefa em relatorio

    private int insereTarefa() {

        BDRotinaHelper bdRotinaHelper = new BDRotinaHelper(this);
        SQLiteDatabase bd = bdRotinaHelper.getWritableDatabase();
        ContentValues cvTarefa = new ContentValues();
        cvTarefa.put("NOME", edtNomeTarefa.getText().toString());
        cvTarefa.put("OBSERVACAO", edtObservacaoTarefa.getText().toString());
        cvTarefa.put("FLAG", "3");
        cvTarefa.put("CATEGORIA", "Tarefa");

        return (int) bd.insert("PROCEDIMENTO", null, cvTarefa);
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(this, ListaPacotesActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }
}