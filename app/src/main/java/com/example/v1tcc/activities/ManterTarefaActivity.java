package com.example.v1tcc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v1tcc.BDHelper.SQLiteConnection;
import com.example.v1tcc.Helpers;
import com.example.v1tcc.R;

//trocar pra manter tarefa parece melhor
public class ManterTarefaActivity extends AppCompatActivity {

    public static final String EXTRA_TAREFA = "extratarefa";
    public static final String EXTRA_ID = "idTarefa";
    private Button btnSalvarTarefa;
    private Button btnFecharSalvarTarefa;
    private EditText edtNomeTarefa;
    private EditText edtObservacaoTarefa;
    private TextView txtCadastroTarefa;
    private SQLiteConnection SQLiteConnection;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private long idProcedimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getExtras().getString(EXTRA_TAREFA).equals("ADICIONAR_TAREFA")) {
            configurarCampos(true);

        } else if (getIntent().getExtras().getString(EXTRA_TAREFA).equals("EDITAR_TAREFA")) {

            configurarCampos(false);
            carregaDados();
            Toast.makeText(this, "EXTRA_ID" + getIntent().getExtras().getLong(EXTRA_ID), Toast.LENGTH_SHORT).show();


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

    //update no salvar do editar
    private void configurarCampos(Boolean cadastrarTarefa) {
        btnSalvarTarefa = findViewById(R.id.btnSalvarTarefa);
        btnFecharSalvarTarefa = findViewById(R.id.btnFecharSalvarTarefa);
        if (cadastrarTarefa) {


            btnSalvarTarefa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarTarefaOnClick(view, false);
                }
            });
            btnFecharSalvarTarefa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnFecharSalvarTarefaOnClick(view, false);
                }
            });
        } else {
            edtNomeTarefa = findViewById(R.id.edtNomeTarefa);
            edtObservacaoTarefa = findViewById(R.id.edtObservacaoTarefa);

            txtCadastroTarefa = findViewById(R.id.txtCadastroTarefa);
            txtCadastroTarefa.setText("Editar Tarefa");

            btnFecharSalvarTarefa.setText("Excluir");
            btnSalvarTarefa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarTarefaOnClick(view, true);
                }
            });
            btnFecharSalvarTarefa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnFecharSalvarTarefaOnClick(view, true);
                }
            });
        }
    }

    private void carregaDados() {
        try { //pode ver a logica de deletar se quiser pegar os alarmes
            idProcedimento = getIntent().getExtras().getLong(EXTRA_ID);
            SQLiteConnection = new SQLiteConnection(this);
            bd = SQLiteConnection.getReadableDatabase();
            cursor = bd.query("PROCEDIMENTO",
                    new String[]{"_id", "NOME", "OBSERVACAO"},
                    "_id = ?",
                    new String[]{Long.toString(idProcedimento)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {

                edtNomeTarefa.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
                edtObservacaoTarefa.setText(cursor.getString(cursor.getColumnIndexOrThrow("OBSERVACAO")));
            } else
                Toast.makeText(this, "Tarefa não encontrada", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Falha no acesso ao Banco de Dados " + e, Toast.LENGTH_LONG).show();
        }
    }

    //fazer função update ou trocar para manter
    private void btnSalvarTarefaOnClick(View view, Boolean updateRow) {
        try {

            Helpers.preenchimentoValido(edtNomeTarefa);

            int idInserted = insereTarefa(updateRow);
            if (idInserted == -1)
                Toast.makeText(this, "Inclusão falhou " + "-1", Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(this, "Id inserido:" + idInserted, Toast.LENGTH_SHORT).show();
            }
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Falha ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void btnFecharSalvarTarefaOnClick(View view, Boolean deleteRow) {

        if(deleteRow){

            SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
            bd.delete("PROCEDIMENTO","_id = ?", new String[] {Long.toString(idProcedimento)});
            bd.close();
            finish();
        } else {
            //ficou ridiculo o branco que da mas pra agora ta pft,
            //replicar nos outros botões fechar, talvez colocar a classe um um helper
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private int insereTarefa(Boolean updateRow) {

        if(updateRow){
            try {
                ContentValues cv = new ContentValues();
                //cv.put("FLAG", "4");
                cv.put("NOME", edtNomeTarefa.getText().toString());
                cv.put("OBSERVACAO", edtObservacaoTarefa.getText().toString());
                //nome
                //obs
                SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
                SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                return (int) bd.update("PROCEDIMENTO", cv, "_id = ?", new String[]{Long.toString(idProcedimento)});

            } catch (SQLiteException e) {
                Toast.makeText(this, "Atualização falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else {
            try {
                SQLiteConnection = new SQLiteConnection(this);
                bd = SQLiteConnection.getWritableDatabase();
                ContentValues cvTarefa = new ContentValues();
                cvTarefa.put("NOME", edtNomeTarefa.getText().toString());
                cvTarefa.put("OBSERVACAO", edtObservacaoTarefa.getText().toString());
                cvTarefa.put("FLAG", "3");
                cvTarefa.put("CATEGORIA", "Tarefa");
                return (int) bd.insert("PROCEDIMENTO", null, cvTarefa);

            } catch (SQLiteException e) {
                Toast.makeText(this, "Criação falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return -1;
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(this, ListaPacotesActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }
}
///retirar action bar https://pt.stackoverflow.com/questions/86728/removendo-o-titlebar-do-app-android