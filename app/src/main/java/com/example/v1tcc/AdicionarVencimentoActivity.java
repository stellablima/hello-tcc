package com.example.v1tcc;

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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AdicionarVencimentoActivity extends AppCompatActivity {


    public static final String EXTRA_VENCIMENTO = "extravencimento";
    public static final String EXTRA_ID = "idvencimento";
    private TextView txtDataUltimaOcorrencia;
    private TextView txtCadastroVencimento;
    private EditText edtTituloVencimento;
    private EditText edtTextoVencimento;
    private long idEstado;
    private BDRotinaHelper bdRotinaHelper;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private Button btnSalvarVencimento;
    private Button btnFecharSalvarVencimento;
    private Button  btnExcluirVencimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_vencimento);
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (getIntent().getExtras().getString(EXTRA_VENCIMENTO).equals("ADICIONAR_VENCIMENTO")) {
            configurarCampos(true);

        } else if (getIntent().getExtras().getString(EXTRA_VENCIMENTO).equals("EDITAR_VENCIMENTO")) {

            configurarCampos(false);
            carregaDados();
            Toast.makeText(this, "EXTRA_ID" + getIntent().getExtras().getLong(EXTRA_ID), Toast.LENGTH_SHORT).show();
        }
    }

    private void btnSalvarVencimentoOnClick(View view, Boolean updateRow){

        try {

            Helpers.preenchimentoValido(edtTituloVencimento);

            int idInserted = insereVencimento(updateRow);
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

    private void btnExcluirVencimentoOnClick(View view, Boolean deleteRow) {

//        if(deleteRow){
//
            BDRotinaHelper bdEstoqueHelper = new BDRotinaHelper(this);
            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
            bd.delete("ESTADO","_id = ?", new String[] {Long.toString(idEstado)});
            bd.close();
            finish();
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

    private int insereVencimento(Boolean updateRow) {

        if(updateRow){
            try {
                ContentValues cv = new ContentValues();
                cv.put("TITULO", edtTituloVencimento.getText().toString());
                cv.put("TEXTO", edtTextoVencimento.getText().toString());
                cv.put("DATA_ULTIMA_OCORRENCIA", txtDataUltimaOcorrencia.getText().toString());
                BDRotinaHelper bdEstoqueHelper = new BDRotinaHelper(this);
                SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                return (int) bd.update("ESTADO", cv, "_id = ?", new String[]{Long.toString(idEstado)});

            } catch (SQLiteException e) {
                Toast.makeText(this, "Atualização falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else {
            try {
                ContentValues cv = new ContentValues();
                //cv.put("FLAG", "4");
                cv.put("TITULO", edtTituloVencimento.getText().toString());
                cv.put("TEXTO", edtTextoVencimento.getText().toString());
                cv.put("DATA_ULTIMA_OCORRENCIA", txtDataUltimaOcorrencia.getText().toString());
                cv.put("FLAG", "1");
                cv.put("CATEGORIA", "Vencimento");
                //nome
                //obs
                BDRotinaHelper bdEstoqueHelper = new BDRotinaHelper(this);
                SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                return (int) bd.insert("ESTADO", null, cv);

            } catch (SQLiteException e) {
                Toast.makeText(this, "Inserção falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return -1;
    }

    private void configurarCampos(Boolean cadastrarTarefa) {
        txtDataUltimaOcorrencia = findViewById(R.id.txtDataUltimaOcorrencia);
        Helpers.txtDataConfig(this, txtDataUltimaOcorrencia,true);
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

        if (cadastrarTarefa) {

            btnSalvarVencimento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarVencimentoOnClick(view, false);
                }
            });

            findViewById(R.id.spcBtnExcluirVencimento).setVisibility(View.GONE);
            btnExcluirVencimento.setVisibility(View.GONE);

        } else {

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
            bdRotinaHelper = new BDRotinaHelper(this);
            bd = bdRotinaHelper.getReadableDatabase();
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
        } catch (SQLiteException e) {
            Toast.makeText(this, "Falha no acesso ao Banco de Dados " + e, Toast.LENGTH_LONG).show();
        }
    }

}