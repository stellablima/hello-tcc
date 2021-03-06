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
import com.flowerroutine.v1tcc.controller.EstadoController;
import com.flowerroutine.v1tcc.models.Estado;

public class ManterInstrucaoActivity extends AppCompatActivity {

    public static final String EXTRA_ESTADO = "extraestado";
    public static final String EXTRA_ID = "idestado";
    private Button btnSalvarInstrucao;
    private Button btnFecharSalvarInstrucao;
    private Button btnExcluirInstrucao;
    private EditText edtTituloInstrucao;
    private EditText edtTextoInstrucao;
    private TextView txtCadastroInstrucao;
    private SQLiteConnection SQLiteConnection;
    private SQLiteDatabase SQLiteDatabase;
    private Cursor cursor;
    private long idEstado;
    private Estado estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_instrucao);

        SQLiteConnection = SQLiteConnection.getInstanciaConexao(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getExtras().getString(EXTRA_ESTADO).equals("ADICIONAR_INSTRUCAO")) {
            configurarCampos(true, false);

        } else if (getIntent().getExtras().getString(EXTRA_ESTADO).equals("EDITAR_INSTRUCAO")) {

            configurarCampos(false, false);
            carregaDados();
            //Toast.makeText(this, "EXTRA_ID" + getIntent().getExtras().getLong(EXTRA_ID), Toast.LENGTH_SHORT).show();
        } else { //if (getIntent().getExtras().getString(EXTRA_ESTADO).equals("CONSULTAR_INSTRUCAO"))
            configurarCampos(false, true);
            carregaDados();
        }
    }

    private void configurarCampos(Boolean cadastrarInstrucao, Boolean soConsulta) {
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

            findViewById(R.id.spcBtnExcluirInstrucao).setVisibility(View.GONE);
            btnExcluirInstrucao.setVisibility(View.GONE);

        } else if(soConsulta){

            txtCadastroInstrucao = findViewById(R.id.txtCadastroInstrucao);
            txtCadastroInstrucao.setText("Instru????o");

            findViewById(R.id.spcBtnExcluirInstrucao).setVisibility(View.GONE);
            findViewById(R.id.spcBtnFecharSalvarInstrucao).setVisibility(View.GONE);

            btnSalvarInstrucao.setVisibility(View.GONE);
            btnExcluirInstrucao.setVisibility(View.GONE);

            //edtTituloInstrucao.setEnabled(false);
            edtTituloInstrucao.setKeyListener(null);
            edtTextoInstrucao.setKeyListener(null);

        } else{

            txtCadastroInstrucao = findViewById(R.id.txtCadastroInstrucao);
            txtCadastroInstrucao.setText("Editar Instru????o");

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
            //SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase = SQLiteConnection.getReadableDatabase();
            cursor = SQLiteDatabase.query("ESTADO",
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
                Toast.makeText(this, "Tarefa n??o encontrada", Toast.LENGTH_SHORT).show();

            SQLiteDatabase.close();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Falha no acesso ao Banco de Dados " + e, Toast.LENGTH_LONG).show();
        }


    }

    private void btnSalvarInstrucaoOnClick(View view, Boolean updateRow){
        try {

            Helpers.preenchimentoValido(edtTituloInstrucao);
            Helpers.preenchimentoValido(edtTextoInstrucao);


            String msgModal;
            if (getIntent().getExtras().getString(EXTRA_ESTADO).equals("ADICIONAR_INSTRUCAO"))
                msgModal = "Deseja incluir instru????o?";
            else
                msgModal = "Deseja alterar instru????o?";

            AlertDialog alertDialog = new AlertDialog.Builder(ManterInstrucaoActivity.this)
                    //.setTitle(alertaDiaTitulo)
                    .setMessage(msgModal)
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            long idInserted = insereInstrucao(updateRow);
                            if (idInserted == -1)
                                Toast.makeText(ManterInstrucaoActivity.this, "Inclus??o falhou " + "-1", Toast.LENGTH_LONG).show();
                //            else {
                //                Toast.makeText(this, "Id inserido:" + idInserted, Toast.LENGTH_SHORT).show();
                //            }
                            finish();

                            if (getIntent().getExtras().getString(EXTRA_ESTADO).equals("ADICIONAR_INSTRUCAO"))
                                Toast.makeText(ManterInstrucaoActivity.this, "Instru????o cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ManterInstrucaoActivity.this, "Instru????o alterada com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Fechar", null)
                    .show();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void btnFecharSalvarInstrucaoOnClick(){

        finish();

    }

    private void btnExcluirInstrucaoOnClick(View view) {

        try{

            AlertDialog alertDialog = new AlertDialog.Builder(ManterInstrucaoActivity.this)
                    //.setTitle(alertaDiaTitulo)
                    .setMessage("Deseja excluir instru????o?")
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SQLiteConnection bdEstoqueHelper = new SQLiteConnection(ManterInstrucaoActivity.this);//?close? SQLiteDatabase.close();
                            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                            bd.delete("ESTADO","_id = ?", new String[] {Long.toString(idEstado)});
                            bd.close();
                            finish();

                            Toast.makeText(ManterInstrucaoActivity.this, "Instru????o exclu??da com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Fechar", null)
                    .show();


        } catch (SQLiteException e) {
            Toast.makeText(ManterInstrucaoActivity.this, "Dele????o falhou", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(ManterInstrucaoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private long insereInstrucao(Boolean updateRow) {

        if(updateRow){
            try {
                ContentValues cv = new ContentValues();
                //cv.put("FLAG", "4");
                cv.put("TITULO", edtTituloInstrucao.getText().toString());
                cv.put("TEXTO", edtTextoInstrucao.getText().toString());
                SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
                SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                return (long) bd.update("ESTADO", cv, "_id = ?", new String[]{Long.toString(idEstado)});

            } catch (SQLiteException e) {
                Toast.makeText(this, "Atualiza????o falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else {
            try {
//                SQLiteConnection = new SQLiteConnection(this);
//                SQLiteDatabase = SQLiteConnection.getWritableDatabase();
//                ContentValues cvTarefa = new ContentValues();
//                cvTarefa.put("TITULO", edtTituloInstrucao.getText().toString());
//                cvTarefa.put("TEXTO", edtTextoInstrucao.getText().toString());
//                cvTarefa.put("FLAG", "1");
//                cvTarefa.put("CATEGORIA", "Instrucao");
//                return (int) SQLiteDatabase.insert("ESTADO", null, cvTarefa);
                return insereEstado();
            } catch (SQLiteException e) {
                Toast.makeText(this, "Cria????o falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return -1;
    }

    private long insereEstado(){
        EstadoController estadoController =  new EstadoController(SQLiteConnection);
        return estadoController.createEstadoController(getInstrucaoActivity());
    }

    private Estado getInstrucaoActivity(){

        this.estado = new Estado();

        //if(this.edtTituloInstrucao.toString().isEmpty() == false)
        this.estado.setTITULO(edtTituloInstrucao.getText().toString());
        //else return null;

        this.estado.setCATEGORIA("Instrucao");
        this.estado.setFLAG("1");
        this.estado.setTEXTO(edtTextoInstrucao.getText().toString());

        return estado;
    }


}