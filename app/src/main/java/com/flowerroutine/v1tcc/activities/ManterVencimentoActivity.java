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

public class ManterVencimentoActivity extends AppCompatActivity {


    public static final String EXTRA_VENCIMENTO = "extravencimento";
    public static final String EXTRA_ID = "idvencimento";
    private TextView txtDataUltimaOcorrencia;
    private TextView txtCadastroVencimento;
    private EditText edtTituloVencimento;
    private EditText edtTextoVencimento;
    private long idEstado;
    private SQLiteConnection SQLiteConnection;
    private SQLiteDatabase bd;
    private SQLiteDatabase SQLiteDatabase;
    private Cursor cursor;
    private Button btnSalvarVencimento;
    private Button btnFecharSalvarVencimento;
    private Button btnExcluirVencimento;
    private Estado estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_vencimento);

        SQLiteConnection = SQLiteConnection.getInstanciaConexao(this);
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (getIntent().getExtras().getString(EXTRA_VENCIMENTO).equals("ADICIONAR_VENCIMENTO")) {
            configurarCampos(true, false);

        } else if (getIntent().getExtras().getString(EXTRA_VENCIMENTO).equals("EDITAR_VENCIMENTO")) {

            configurarCampos(false, false);
            carregaDados();
            //Toast.makeText(this, "EXTRA_ID" + getIntent().getExtras().getLong(EXTRA_ID), Toast.LENGTH_SHORT).show();
        }else{ //if (getIntent().getExtras().getString(EXTRA_ESTADO).equals("CONSULTAR_VENCIMENTO"))
            configurarCampos(false, true);
            carregaDados();
        }
    }

    private void btnSalvarVencimentoOnClick(View view, Boolean updateRow){

        try {

            Helpers.preenchimentoValido(edtTituloVencimento);
            Helpers.preenchimentoValido(edtTextoVencimento);

            String msgModal;
            if (getIntent().getExtras().getString(EXTRA_VENCIMENTO).equals("ADICIONAR_VENCIMENTO"))
                msgModal = "Deseja incluir vencimento?";
            else
                msgModal = "Deseja alterar vencimento?";

            AlertDialog alertDialog = new AlertDialog.Builder(ManterVencimentoActivity.this)
                    //.setTitle(alertaDiaTitulo)
                    .setMessage(msgModal)
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        long idInserted = insereVencimento(updateRow);
                        if (idInserted == -1)
                            Toast.makeText(ManterVencimentoActivity.this, "Inclusão falhou " + "-1", Toast.LENGTH_LONG).show();
                        else {
                            Toast.makeText(ManterVencimentoActivity.this, "Id inserido:" + idInserted, Toast.LENGTH_SHORT).show();
                        }
                        finish();

                            if (getIntent().getExtras().getString(EXTRA_VENCIMENTO).equals("ADICIONAR_VENCIMENTO"))
                                Toast.makeText(ManterVencimentoActivity.this, "Vencimento cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ManterVencimentoActivity.this, "Vencimento alterado com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Fechar", null)
                    .show();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void btnExcluirVencimentoOnClick(View view, Boolean deleteRow) {

//        if(deleteRow){
//
        try{

            AlertDialog alertDialog = new AlertDialog.Builder(ManterVencimentoActivity.this)
                    //.setTitle(alertaDiaTitulo)
                    .setMessage("Deseja excluir vencimento?")
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SQLiteConnection bdEstoqueHelper = new SQLiteConnection(ManterVencimentoActivity.this);
                            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                            bd.delete("ESTADO","_id = ?", new String[] {Long.toString(idEstado)});
                            bd.close();
                            finish();
                            Toast.makeText(ManterVencimentoActivity.this, "Vencimento excluído com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Fechar", null)
                    .show();


        } catch (SQLiteException e) {
            Toast.makeText(ManterVencimentoActivity.this, "Deleção falhou", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(ManterVencimentoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
//        } else {
//            //ficou ridiculo o branco que da mas pra agora ta pft,
//            //replicar nos outros botões fechar, talvez colocar a classe um um helper
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
    }

    private void btnFecharSalvarVencimentoOnClick(){
        finish();
    }

    private long insereVencimento(Boolean updateRow) {

        if(updateRow){
            try {
                ContentValues cv = new ContentValues();
                cv.put("TITULO", edtTituloVencimento.getText().toString());
                cv.put("TEXTO", edtTextoVencimento.getText().toString());
                cv.put("DATA_ULTIMA_OCORRENCIA", txtDataUltimaOcorrencia.getText().toString());
                SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
                SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                return bd.update("ESTADO", cv, "_id = ?", new String[]{Long.toString(idEstado)});

            } catch (SQLiteException e) {
                Toast.makeText(this, "Atualização falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else {
            try {
//                ContentValues cv = new ContentValues();
//                //cv.put("FLAG", "4");
//                cv.put("TITULO", edtTituloVencimento.getText().toString());
//                cv.put("TEXTO", edtTextoVencimento.getText().toString());
//                cv.put("DATA_ULTIMA_OCORRENCIA", txtDataUltimaOcorrencia.getText().toString());
//                cv.put("FLAG", "1");
//                cv.put("CATEGORIA", "Vencimento");
//                //nome
//                //obs
//                SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
//                SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
//                return (int) bd.insert("ESTADO", null, cv);

                long idVencimento = insereEstado();
                return idVencimento;

            } catch (SQLiteException e) {
                Toast.makeText(this, "Inserção falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return -1;
    }

    private Estado getVencimentoActivity(){

        this.estado = new Estado();

        this.estado.setTITULO(edtTituloVencimento.getText().toString());
        this.estado.setTEXTO(edtTextoVencimento.getText().toString());
        this.estado.setDATA_ULTIMA_OCORRENCIA(txtDataUltimaOcorrencia.getText().toString());
        this.estado.setFLAG("1");
        this.estado.setCATEGORIA("Vencimento");

        return estado;
    }

    private long insereEstado(){
        EstadoController estadoController =  new EstadoController(SQLiteConnection);
        return estadoController.createEstadoController(getVencimentoActivity());
    }



    private void configurarCampos(Boolean cadastrarVencimento, Boolean soConsulta) {
        txtDataUltimaOcorrencia = findViewById(R.id.txtDataUltimaOcorrencia);
        //Helpers.txtDataConfig(this, txtDataUltimaOcorrencia,true);
        edtTituloVencimento = findViewById(R.id.edtTituloVencimento);
        edtTextoVencimento = findViewById(R.id.edtTextoVencimento);
        btnSalvarVencimento = findViewById(R.id.btnSalvarVencimento);
        btnFecharSalvarVencimento = findViewById(R.id.btnFecharSalvarVencimento);
        txtCadastroVencimento = findViewById(R.id.txtCadastroVencimento);
        btnExcluirVencimento = findViewById(R.id.btnExcluirVencimento);

        btnFecharSalvarVencimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFecharSalvarVencimentoOnClick();
            }
        });

        if (cadastrarVencimento) {
            Helpers.txtDataConfig(this, txtDataUltimaOcorrencia,true);
            btnSalvarVencimento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarVencimentoOnClick(view, false);
                }
            });

            findViewById(R.id.spcBtnExcluirVencimento).setVisibility(View.GONE);
            btnExcluirVencimento.setVisibility(View.GONE);

        } else if(soConsulta){

            txtCadastroVencimento.setText("Vencimento");

            findViewById(R.id.spcBtnExcluirVencimento).setVisibility(View.GONE);
            findViewById(R.id.spcBtnSalvarVencimento).setVisibility(View.GONE);

            btnSalvarVencimento.setVisibility(View.GONE);
            btnExcluirVencimento.setVisibility(View.GONE);

            edtTituloVencimento.setKeyListener(null);
            edtTextoVencimento.setKeyListener(null);
            //txtDataUltimaOcorrencia.setKeyListener(null);

        } else {
            Helpers.txtDataConfig(this, txtDataUltimaOcorrencia,true);
            txtCadastroVencimento.setText("Editar Vencimento");

            btnSalvarVencimento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarVencimentoOnClick(view, true);
                }
            });

            btnExcluirVencimento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnExcluirVencimentoOnClick(view, true);
                }
            });
        }
    }

    private void carregaDados() {
        try { //pode ver a logica de deletar se quiser pegar os alarmes
            idEstado = getIntent().getExtras().getLong(EXTRA_ID);
            //SQLiteConnection = new SQLiteConnection(this);
            bd = SQLiteConnection.getReadableDatabase();
            cursor = bd.query("ESTADO",
                    new String[]{"_id", "TITULO", "TEXTO", "DATA_ULTIMA_OCORRENCIA"},
                    "_id = ?",
                    new String[]{Long.toString(idEstado)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                edtTituloVencimento.setText(cursor.getString(cursor.getColumnIndexOrThrow("TITULO")));
                edtTextoVencimento.setText(cursor.getString(cursor.getColumnIndexOrThrow("TEXTO")));
                txtDataUltimaOcorrencia.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATA_ULTIMA_OCORRENCIA")));
            } else
                Toast.makeText(this, "Vencimento não encontrado", Toast.LENGTH_SHORT).show();
            bd.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Falha no acesso ao Banco de Dados " + e, Toast.LENGTH_LONG).show();
        }
    }

}