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

public class AdicionarOcorrenciaActivity extends AppCompatActivity {

    public static final String EXTRA_OCORRENCIA = "extraocorrencia";
    public static final String EXTRA_ID = "idocorrencia";
    private Button btnSalvarOcorrencia;
    private Button btnFecharSalvarOcorrencia;
    private Button btnExcluirOcorrencia;
    private EditText edtNomeOcorrencia;
    private EditText edtObservacaoOcorrencia;
    private TextView txtCadastroOcorrencia;
    private BDRotinaHelper bdRotinaHelper;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private long idRelatorio;
    private TextView txtDataInicioOcorrencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_ocorrencia);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getExtras().getString(EXTRA_OCORRENCIA).equals("ADICIONAR_OCORRENCIA")) {
            configurarCampos(true);

        } else if (getIntent().getExtras().getString(EXTRA_OCORRENCIA).equals("EDITAR_OCORRENCIA")) {

            configurarCampos(false);
            carregaDados();
            Toast.makeText(this, "EXTRA_ID" + getIntent().getExtras().getLong(EXTRA_ID), Toast.LENGTH_SHORT).show();

        }
    }

    private void configurarCampos(Boolean cadastrarOcorrencia) {
        txtDataInicioOcorrencia = findViewById(R.id.txtDataInicioOcorrencia);
        Helpers.txtDataConfig(this, txtDataInicioOcorrencia,true);
        btnSalvarOcorrencia = findViewById(R.id.btnSalvarOcorrencia);
        btnFecharSalvarOcorrencia = findViewById(R.id.btnFecharSalvarOcorrencia);
        //btnExcluirInstrucaoOnClick
        btnExcluirOcorrencia = findViewById(R.id.btnExcluirOcorrencia);

        edtNomeOcorrencia = findViewById(R.id.edtNomeOcorrencia);
        edtObservacaoOcorrencia = findViewById(R.id.edtObservacaoOcorrencia);

        btnFecharSalvarOcorrencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFecharSalvarOcorrenciaOnClick();
            }
        });

        if (cadastrarOcorrencia) {

            btnSalvarOcorrencia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarOcorrenciaOnClick(view, false);
                }
            });

            findViewById(R.id.spcbtnExcluirOcorrencia).setVisibility(View.GONE);
            btnExcluirOcorrencia.setVisibility(View.GONE);

        } else {
            txtCadastroOcorrencia = findViewById(R.id.txtCadastroOcorrencia);
            txtCadastroOcorrencia.setText("Editar Ocorrência");

            btnSalvarOcorrencia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarOcorrenciaOnClick(view, true);
                }
            });
            btnExcluirOcorrencia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnExcluirOcorrenciaOnClick(view);
                }
            });
        }
    }

    private void carregaDados() {
        try { //pode ver a logica de deletar se quiser pegar os alarmes
            idRelatorio = getIntent().getExtras().getLong(EXTRA_ID);
            bdRotinaHelper = new BDRotinaHelper(this);
            bd = bdRotinaHelper.getReadableDatabase();
            cursor = bd.query("RELATORIO",
                    new String[]{"_id", "NOME", "OBSERVACAO", "DATA_INICIO"},
                    "_id = ?",
                    new String[]{Long.toString(idRelatorio)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                edtNomeOcorrencia.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
                edtObservacaoOcorrencia.setText(cursor.getString(cursor.getColumnIndexOrThrow("OBSERVACAO")));
                txtDataInicioOcorrencia.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATA_INICIO")));
            } else
                Toast.makeText(this, "Ocorrência não encontrada", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Falha no acesso ao Banco de Dados " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void btnSalvarOcorrenciaOnClick(View view, Boolean updateRow){
        try {

            Helpers.preenchimentoValido(edtNomeOcorrencia);

            int idInserted = insereOcorrencia(updateRow);
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

    private void btnFecharSalvarOcorrenciaOnClick(){

        finish();

    }

    private void btnExcluirOcorrenciaOnClick(View view) {
        BDRotinaHelper bdEstoqueHelper = new BDRotinaHelper(this);
        SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
        bd.delete("RELATORIO","_id = ?", new String[] {Long.toString(idRelatorio)});
        bd.close();
        finish();
    }

    private int insereOcorrencia(Boolean updateRow) {

        if(updateRow){
            try {
                ContentValues cv = new ContentValues();
                //cv.put("FLAG", "4");
                cv.put("NOME", edtNomeOcorrencia.getText().toString());
                cv.put("OBSERVACAO", edtObservacaoOcorrencia.getText().toString());
                cv.put("DATA_INICIO", txtDataInicioOcorrencia.getText().toString());
                BDRotinaHelper bdEstoqueHelper = new BDRotinaHelper(this);
                SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                return (int) bd.update("RELATORIO", cv, "_id = ?", new String[]{Long.toString(idRelatorio)});

            } catch (SQLiteException e) {
                Toast.makeText(this, "Atualização falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else {
            try {
                bdRotinaHelper = new BDRotinaHelper(this);
                bd = bdRotinaHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("NOME", edtNomeOcorrencia.getText().toString());
                cv.put("OBSERVACAO", edtObservacaoOcorrencia.getText().toString());
                cv.put("DATA_INICIO", txtDataInicioOcorrencia.getText().toString());
                cv.put("CATEGORIA", "Ocorrência");
                return (int) bd.insert("RELATORIO", null, cv);

            } catch (SQLiteException e) {
                Toast.makeText(this, "Criação falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return -1;
    }
}