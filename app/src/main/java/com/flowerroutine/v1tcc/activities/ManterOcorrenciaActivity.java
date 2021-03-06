package com.flowerroutine.v1tcc.activities;

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

import com.flowerroutine.v1tcc.BDHelper.SQLiteConnection;
import com.flowerroutine.v1tcc.Helpers;
import com.flowerroutine.v1tcc.R;
import com.flowerroutine.v1tcc.controller.RelatorioController;
import com.flowerroutine.v1tcc.models.Relatorio;

public class ManterOcorrenciaActivity extends AppCompatActivity {

    public static final String EXTRA_OCORRENCIA = "extraocorrencia";
    public static final String EXTRA_ID = "idocorrencia";
    private Button btnSalvarOcorrencia;
    private Button btnFecharSalvarOcorrencia;
    private Button btnExcluirOcorrencia;
    private EditText edtNomeOcorrencia;
    private EditText edtObservacaoOcorrencia;
    private TextView txtCadastroOcorrencia;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private long idRelatorio;
    private TextView txtDataInicioOcorrencia;

    private SQLiteConnection SQLiteConnection;
    private SQLiteDatabase SQLiteDatabase;
    private Relatorio relatorio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_ocorrencia);

        SQLiteConnection = SQLiteConnection.getInstanciaConexao(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getExtras().getString(EXTRA_OCORRENCIA).equals("ADICIONAR_OCORRENCIA")) {
            configurarCampos(true, false);

        } else if (getIntent().getExtras().getString(EXTRA_OCORRENCIA).equals("EDITAR_OCORRENCIA")) {

            configurarCampos(false, false);
            carregaDados();
            //Toast.makeText(this, "EXTRA_ID" + getIntent().getExtras().getLong(EXTRA_ID), Toast.LENGTH_SHORT).show();
        } else { //if (getIntent().getExtras().getString(EXTRA_ESTADO).equals("CONSULTAR_OCORRENCIA"))
            configurarCampos(false, true);
            carregaDados();
        }
    }

    private void configurarCampos(Boolean cadastrarOcorrencia, Boolean soConsulta) {
        txtDataInicioOcorrencia = findViewById(R.id.txtDataInicioOcorrencia);
        //Helpers.txtDataConfig(this, txtDataInicioOcorrencia,true);
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
            Helpers.txtDataConfig(this, txtDataInicioOcorrencia,true);
            btnSalvarOcorrencia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarOcorrenciaOnClick(view, false);
                }
            });

            findViewById(R.id.spcbtnExcluirOcorrencia).setVisibility(View.GONE);
            btnExcluirOcorrencia.setVisibility(View.GONE);

        } else if(soConsulta){

            txtCadastroOcorrencia = findViewById(R.id.txtCadastroOcorrencia);
            txtCadastroOcorrencia.setText("Ocorr??ncia");

            findViewById(R.id.spcbtnExcluirOcorrencia).setVisibility(View.GONE);
            findViewById(R.id.spcBtnSalvarOcorrencia).setVisibility(View.GONE);

            btnSalvarOcorrencia.setVisibility(View.GONE);
            btnExcluirOcorrencia.setVisibility(View.GONE);

            edtNomeOcorrencia.setKeyListener(null);
            edtObservacaoOcorrencia.setKeyListener(null);

        }else {
            Helpers.txtDataConfig(this, txtDataInicioOcorrencia,true);
            txtCadastroOcorrencia = findViewById(R.id.txtCadastroOcorrencia);
            txtCadastroOcorrencia.setText("Editar Ocorr??ncia");

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
            //SQLiteConnection = new SQLiteConnection(this);
            bd = SQLiteConnection.getReadableDatabase();
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
                Toast.makeText(this, "Ocorr??ncia n??o encontrada", Toast.LENGTH_SHORT).show();

            bd.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Falha no acesso ao Banco de Dados " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void btnSalvarOcorrenciaOnClick(View view, Boolean updateRow){
        try {

            Helpers.preenchimentoValido(edtNomeOcorrencia);
            Helpers.preenchimentoValido(edtObservacaoOcorrencia);

            String msgModal;
            if (getIntent().getExtras().getString(EXTRA_OCORRENCIA).equals("ADICIONAR_OCORRENCIA"))
                msgModal = "Deseja incluir ocorr??ncia?";
            else
                msgModal = "Deseja alterar ocorr??ncia?";

            AlertDialog alertDialog = new AlertDialog.Builder(ManterOcorrenciaActivity.this)
                    //.setTitle(alertaDiaTitulo)
                    .setMessage(msgModal)
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            long idInserted = insereOcorrencia(updateRow);
                            if (idInserted == -1)
                                Toast.makeText(ManterOcorrenciaActivity.this, "Inclus??o falhou " + "-1", Toast.LENGTH_LONG).show();
                //            else {
                //                Toast.makeText(this, "Id inserido:" + idInserted, Toast.LENGTH_SHORT).show();
                //            }
                            finish();

                            if (getIntent().getExtras().getString(EXTRA_OCORRENCIA).equals("ADICIONAR_OCORRENCIA"))
                                Toast.makeText(ManterOcorrenciaActivity.this, "Ocorr??ncia cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ManterOcorrenciaActivity.this, "Ocorr??ncia alterada com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Fechar", null)
                    .show();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void btnFecharSalvarOcorrenciaOnClick(){

        finish();

    }

    private void btnExcluirOcorrenciaOnClick(View view) {

        try {

            AlertDialog alertDialog = new AlertDialog.Builder(ManterOcorrenciaActivity.this)
                    //.setTitle(alertaDiaTitulo)
                    .setMessage("Deseja excluir ocorr??ncia?")
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SQLiteConnection bdEstoqueHelper = new SQLiteConnection(ManterOcorrenciaActivity.this);
                            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                            bd.delete("RELATORIO", "_id = ?", new String[]{Long.toString(idRelatorio)});
                            bd.close();
                            finish();

                            Toast.makeText(ManterOcorrenciaActivity.this, "Ocorr??ncia exclu??da com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Fechar", null)
                    .show();
        } catch (SQLiteException e) {
            Toast.makeText(ManterOcorrenciaActivity.this, "Dele????o falhou", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(ManterOcorrenciaActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private long insereOcorrencia(Boolean updateRow) {

        if(updateRow){
            try {
                ContentValues cv = new ContentValues();
                //cv.put("FLAG", "4");
                cv.put("NOME", edtNomeOcorrencia.getText().toString());
                cv.put("OBSERVACAO", edtObservacaoOcorrencia.getText().toString());
                cv.put("DATA_INICIO", txtDataInicioOcorrencia.getText().toString());
                SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
                SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                return bd.update("RELATORIO", cv, "_id = ?", new String[]{Long.toString(idRelatorio)});

            } catch (SQLiteException e) {
                Toast.makeText(this, "Atualiza????o falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else {
            try {
//                SQLiteConnection = new SQLiteConnection(this);
//                bd = SQLiteConnection.getWritableDatabase();
//                ContentValues cv = new ContentValues();
//                cv.put("NOME", edtNomeOcorrencia.getText().toString());
//                cv.put("OBSERVACAO", edtObservacaoOcorrencia.getText().toString());
//                cv.put("DATA_INICIO", txtDataInicioOcorrencia.getText().toString());
//                cv.put("CATEGORIA", "Ocorr??ncia");
//                return (int) bd.insert("RELATORIO", null, cv);

                long idOcorrencia = insereRelatorio();
                return idOcorrencia;

            } catch (SQLiteException e) {
                Toast.makeText(this, "Cria????o falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        SQLiteDatabase.close();
        return -1;
    }

    private Relatorio getOcorrenciaActivity(){



        this.relatorio = new Relatorio();

        this.relatorio.setNOME(edtNomeOcorrencia.getText().toString());
        this.relatorio.setCATEGORIA("Ocorr??ncia");
        this.relatorio.setOBSERVACAO(edtObservacaoOcorrencia.getText().toString());
        this.relatorio.setDATA_INICIO(txtDataInicioOcorrencia.getText().toString());


        return relatorio;
    }

    private long insereRelatorio(){
        RelatorioController relatorioController =  new RelatorioController(SQLiteConnection);
        return relatorioController.createRelatorioController(getOcorrenciaActivity());
    }
}