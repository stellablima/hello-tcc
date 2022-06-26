package com.example.v1tcc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private SQLiteDatabase SQLiteDatabase;
    private Cursor cursor;
    private long idProcedimento;
    private Procedimento procedimento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_tarefa);

        SQLiteConnection = SQLiteConnection.getInstanciaConexao(this);
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

        }
    }

    private void configurarCampos(Boolean cadastrarTarefa) {
        btnSalvarTarefa = findViewById(R.id.btnSalvarTarefa);
        btnFecharSalvarTarefa = findViewById(R.id.btnFecharSalvarTarefa);
        edtNomeTarefa = findViewById(R.id.edtNomeTarefa);
        edtObservacaoTarefa = findViewById(R.id.edtObservacaoTarefa);
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
            //edtNomeTarefa = findViewById(R.id.edtNomeTarefa);
            //edtObservacaoTarefa = findViewById(R.id.edtObservacaoTarefa);

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
        try {
            idProcedimento = getIntent().getExtras().getLong(EXTRA_ID);
            SQLiteDatabase = SQLiteConnection.getReadableDatabase();
            cursor = SQLiteDatabase.query("PROCEDIMENTO",
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

        SQLiteDatabase.close();
    }

    private void btnSalvarTarefaOnClick(View view, Boolean updateRow) {
        try {

            Helpers.preenchimentoValido(edtNomeTarefa);
            Helpers.preenchimentoValido(edtObservacaoTarefa);

            String msgModal;
            if (getIntent().getExtras().getString(EXTRA_TAREFA).equals("ADICIONAR_TAREFA"))
                msgModal = "Deseja incluir Tarefa?";
            else
                msgModal = "Deseja alterar Tarefa?";

            AlertDialog alertDialog = new AlertDialog.Builder(ManterTarefaActivity.this)
                    //.setTitle(alertaDiaTitulo)
                    .setMessage(msgModal)
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            long idInserted = insereTarefa(updateRow);

                            if (idInserted == -1)
                                Toast.makeText(ManterTarefaActivity.this, "Inclusão falhou " + "-1", Toast.LENGTH_LONG).show();
                            finish();

                            if (getIntent().getExtras().getString(EXTRA_TAREFA).equals("ADICIONAR_TAREFA"))
                                Toast.makeText(ManterTarefaActivity.this, "Tarefa cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ManterTarefaActivity.this, "Tarefa alterada com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Fechar", null)
                    .show();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void btnFecharSalvarTarefaOnClick(View view, Boolean deleteRow) {



        try {

            if(deleteRow){





                AlertDialog alertDialog = new AlertDialog.Builder(ManterTarefaActivity.this)
                        //.setTitle(alertaDiaTitulo)
                        .setMessage("Deseja excluir tarefa?")
                        .setCancelable(false)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SQLiteConnection bdEstoqueHelper = new SQLiteConnection(ManterTarefaActivity.this);
                                SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                                bd.delete("PROCEDIMENTO","_id = ?", new String[] {Long.toString(idProcedimento)});
                                bd.close();

                                Toast.makeText(ManterTarefaActivity.this, "Tarefa excluída com sucesso", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("Fechar", null)
                        .show();

            } //else {
            //melhoria:tirar o piscado branco
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            //}


        } catch (SQLiteException e) {
            Toast.makeText(ManterTarefaActivity.this, "Deleção falhou", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(ManterTarefaActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }



    }

    private long insereTarefa(Boolean updateRow) {

        if(updateRow){
            try {
                ContentValues cv = new ContentValues();
                cv.put("NOME", edtNomeTarefa.getText().toString());
                cv.put("OBSERVACAO", edtObservacaoTarefa.getText().toString());

                SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
                SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();

                int idTarefa = bd.update("PROCEDIMENTO", cv, "_id = ?", new String[]{Long.toString(idProcedimento)});
                SQLiteDatabase.close();

                return idTarefa;

            } catch (SQLiteException e) {
                Toast.makeText(this, "Atualização falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else {
            try {

                long idTarefa = insereProcedimento();
                return idTarefa;

            } catch (SQLiteException e) {
                Toast.makeText(this, "Criação falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        SQLiteDatabase.close();
        return -1;
    }

    private Procedimento getTarefaActivity(){

        this.procedimento = new Procedimento();

        this.procedimento.setNOME(edtNomeTarefa.getText().toString());
        this.procedimento.setCATEGORIA("Tarefa");
        this.procedimento.setFLAG("3");
        this.procedimento.setOBSERVACAO(edtObservacaoTarefa.getText().toString());

        return procedimento;
    }

    private long insereProcedimento(){
        ProcedimentoController procedimentoController =  new ProcedimentoController(SQLiteConnection);
        return procedimentoController.createProcedimentoController(getTarefaActivity());
    }

}
