package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

public class AdicionarInstrucaoActivity extends AppCompatActivity {

    public static final String EXTRA_ESTADO = "extraestado";
    public static final String EXTRA_ID = "idestado";
    private Button btnSalvarInstrucao;
    private Button btnFecharSalvarInstrucao;
    private Button btnExcluirInstrucao;
    private EditText edtTituloInstrucao;
    private EditText edtTextoInstrucao;
    private TextView txtCadastroInstrucao;
    private SQLiteConnection SQLiteConnection;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private long idEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_instrucao);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getExtras().getString(EXTRA_ESTADO).equals("ADICIONAR_INSTRUCAO")) {
            configurarCampos(true);

        } else if (getIntent().getExtras().getString(EXTRA_ESTADO).equals("EDITAR_INSTRUCAO")) {

            configurarCampos(false);
            carregaDados();
            Toast.makeText(this, "EXTRA_ID" + getIntent().getExtras().getLong(EXTRA_ID), Toast.LENGTH_SHORT).show();

        }
    }

    private void configurarCampos(Boolean cadastrarInstrucao) {
        btnSalvarInstrucao = findViewById(R.id.btnSalvarInstrucao);
        btnFecharSalvarInstrucao = findViewById(R.id.btnFecharSalvarInstrucao);
        //btnExcluirInstrucaoOnClick
        btnExcluirInstrucao = findViewById(R.id.btnExcluirInstrucao);

        edtTituloInstrucao = findViewById(R.id.edtTituloInstrucao);
        edtTextoInstrucao = findViewById(R.id.edtTextoInstrucao);

        btnFecharSalvarInstrucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFecharSalvarInstrucaoOnClick();
            }
        });

        if (cadastrarInstrucao) {

            btnSalvarInstrucao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarInstrucaoOnClick(view, false);
                }
            });

            findViewById(R.id.spcbtnExcluirInstrucao).setVisibility(View.GONE);
            btnExcluirInstrucao.setVisibility(View.GONE);

        } else {
//            edtTituloInstrucao = findViewById(R.id.edtTituloInstrucao);
//            edtTextoInstrucao = findViewById(R.id.edtTextoInstrucao);

            txtCadastroInstrucao = findViewById(R.id.txtCadastroInstrucao);
            txtCadastroInstrucao.setText("Editar Instrução");


            btnSalvarInstrucao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarInstrucaoOnClick(view, true);
                }
            });
            btnExcluirInstrucao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnExcluirInstrucaoOnClick(view);
                }
            });
        }
    }

    private void carregaDados() {
        try { //pode ver a logica de deletar se quiser pegar os alarmes
            idEstado = getIntent().getExtras().getLong(EXTRA_ID);
            SQLiteConnection = new SQLiteConnection(this);
            bd = SQLiteConnection.getReadableDatabase();
            cursor = bd.query("ESTADO",
                    new String[]{"_id", "TITULO", "TEXTO"},
                    "_id = ?",
                    new String[]{Long.toString(idEstado)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {

                edtTituloInstrucao.setText(cursor.getString(cursor.getColumnIndexOrThrow("TITULO")));
                edtTextoInstrucao.setText(cursor.getString(cursor.getColumnIndexOrThrow("TEXTO")));
            } else
                Toast.makeText(this, "Tarefa não encontrada", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Falha no acesso ao Banco de Dados " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void btnSalvarInstrucaoOnClick(View view, Boolean updateRow){
        try {

            Helpers.preenchimentoValido(edtTituloInstrucao);

            int idInserted = insereInstrucao(updateRow);
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

    private void btnFecharSalvarInstrucaoOnClick(){

        finish();

    }

    private void btnExcluirInstrucaoOnClick(View view) {
        SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
        SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
        bd.delete("ESTADO","_id = ?", new String[] {Long.toString(idEstado)});
        bd.close();
        finish();
    }

    private int insereInstrucao(Boolean updateRow) {

        if(updateRow){
            try {
                ContentValues cv = new ContentValues();
                //cv.put("FLAG", "4");
                cv.put("TITULO", edtTituloInstrucao.getText().toString());
                cv.put("TEXTO", edtTextoInstrucao.getText().toString());
                SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
                SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                return (int) bd.update("ESTADO", cv, "_id = ?", new String[]{Long.toString(idEstado)});

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
                cvTarefa.put("TITULO", edtTituloInstrucao.getText().toString());
                cvTarefa.put("TEXTO", edtTextoInstrucao.getText().toString());
                cvTarefa.put("FLAG", "1");
                cvTarefa.put("CATEGORIA", "Instrucao");
                return (int) bd.insert("ESTADO", null, cvTarefa);

            } catch (SQLiteException e) {
                Toast.makeText(this, "Criação falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return -1;
    }
}