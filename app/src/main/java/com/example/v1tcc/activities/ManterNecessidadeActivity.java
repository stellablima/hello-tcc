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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v1tcc.BDHelper.SQLiteConnection;
import com.example.v1tcc.Helpers;
import com.example.v1tcc.R;
import com.example.v1tcc.controller.RelatorioController;
import com.example.v1tcc.models.Relatorio;

import java.util.Calendar;
import java.util.Locale;

public class ManterNecessidadeActivity extends AppCompatActivity {

    public static final String EXTRA_NECESSIDADE = "extranecessidade";
    public static final String EXTRA_ID = "idnecessidade";
    private Button btnSalvarNecessidade;
    private Button btnFecharSalvarNecessidade;
    private Button btnExcluirNecessidade;
    //private EditText edtNomeNecessidade;
    private RadioButton rbNecessiadeEspontanea;
    private RadioButton rbNecessidadeNaoEspontanea;
    private RadioButton rbNecessidade1;
    private RadioButton rbNecessidade2;
    private EditText edtObservacaoNecessidade;
    private TextView txtCadastroNecessidade;
    private SQLiteConnection SQLiteConnection;
    private SQLiteDatabase SQLiteDatabase;
    private SQLiteDatabase bd; //?
    private Cursor cursor;
    private long idRelatorio;
    private TextView txtDataInicioNecessidade;
    private Relatorio relatorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_necessidade);

        SQLiteConnection = SQLiteConnection.getInstanciaConexao(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getExtras().getString(EXTRA_NECESSIDADE).equals("ADICIONAR_NECESSIDADE")) {
            configurarCampos(true, false);

        } else if (getIntent().getExtras().getString(EXTRA_NECESSIDADE).equals("EDITAR_NECESSIDADE")) {

            configurarCampos(false, false);
            carregaDados();
            //Toast.makeText(this, "EXTRA_ID" + getIntent().getExtras().getLong(EXTRA_ID), Toast.LENGTH_SHORT).show();
        } else { //if (getIntent().getExtras().getString(EXTRA_ESTADO).equals("CONSULTAR_NECESSIDADE"))
            configurarCampos(false, true);
            carregaDados();
        }
    }

    private void configurarCampos(Boolean cadastrarOcorrencia, Boolean soConsulta) {
        txtDataInicioNecessidade = findViewById(R.id.txtDataInicioNecessidade);
        Helpers.txtDataConfig(this, txtDataInicioNecessidade,true);
        btnSalvarNecessidade = findViewById(R.id.btnSalvarNecessidade);
        btnFecharSalvarNecessidade = findViewById(R.id.btnFecharSalvarNecessidade);
        btnExcluirNecessidade = findViewById(R.id.btnExcluirNecessidade);

//        edtNomeNecessidade = findViewById(R.id.edtNomeNecessidade);
        rbNecessiadeEspontanea = findViewById(R.id.rbNecessiadeEspontanea);
        rbNecessidadeNaoEspontanea = findViewById(R.id.rbNecessidadeNaoEspontanea);
        rbNecessidade1 = findViewById(R.id.rbNecessidade1);
        rbNecessidade2 = findViewById(R.id.rbNecessidade2);

        edtObservacaoNecessidade = findViewById(R.id.edtObservacaoNecessidade);

        btnFecharSalvarNecessidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFecharSalvarNecessidadeOnClick();
            }
        });

        if (cadastrarOcorrencia) {

            btnSalvarNecessidade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarNecessidadeOnClick(view, false);
                }
            });

            findViewById(R.id.spcbtnExcluirNecessidade).setVisibility(View.GONE);
            btnExcluirNecessidade.setVisibility(View.GONE);

        } else if(soConsulta){

            txtCadastroNecessidade = findViewById(R.id.txtCadastroNecessidade);
            txtCadastroNecessidade.setText("Necessidade");

            findViewById(R.id.spcbtnExcluirNecessidade).setVisibility(View.GONE);
            findViewById(R.id.spcBtnFecharSalvarNecessidade).setVisibility(View.GONE);

            btnSalvarNecessidade.setVisibility(View.GONE);
            btnExcluirNecessidade.setVisibility(View.GONE);

            rbNecessidade1.setKeyListener(null);
            rbNecessidade2.setKeyListener(null);
            rbNecessiadeEspontanea.setKeyListener(null);
            rbNecessidadeNaoEspontanea.setKeyListener(null);
            edtObservacaoNecessidade.setKeyListener(null);

        } else {
            txtCadastroNecessidade = findViewById(R.id.txtCadastroNecessidade);
            txtCadastroNecessidade.setText("Editar Necessidade");

            btnSalvarNecessidade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSalvarNecessidadeOnClick(view, true);
                }
            });
            btnExcluirNecessidade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnExcluirNecessidadeOnClick(view);
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
                //edtNomeNecessidade.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
                /*devolver os checkeds por split ou... nao deixar editar*/

                /*

                if Urina
                urina checked
                if Espontanea
                espontanea checked

                */
                String nomeSplit = cursor.getString(cursor.getColumnIndexOrThrow("NOME"));

                if(nomeSplit.contains("Urina"))
                    rbNecessidade1.setChecked(true);
                else
                    rbNecessidade2.setChecked(true);

                if(nomeSplit.contains("Espontânea"))
                    rbNecessiadeEspontanea.setChecked(true);
                else
                    rbNecessidadeNaoEspontanea.setChecked(true);


                edtObservacaoNecessidade.setText(cursor.getString(cursor.getColumnIndexOrThrow("OBSERVACAO")));
                //txtDataInicioNecessidade.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATA_INICIO")));
            } else
                Toast.makeText(this, "Não encontrado", Toast.LENGTH_SHORT).show();

            bd.close();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Falha no acesso ao Banco de Dados " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void btnSalvarNecessidadeOnClick(View view, Boolean updateRow){
        try {

            String msgModal;
            if (getIntent().getExtras().getString(EXTRA_NECESSIDADE).equals("ADICIONAR_NECESSIDADE"))
                msgModal = "Deseja incluir necessidade?";
            else
                msgModal = "Deseja alterar necessidade?";

            AlertDialog alertDialog = new AlertDialog.Builder(ManterNecessidadeActivity.this)
                    //.setTitle(alertaDiaTitulo)
                    .setMessage(msgModal)
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            long idInserted = insereNecessidade(updateRow);
                            if (idInserted == -1)
                                Toast.makeText(ManterNecessidadeActivity.this, "Inclusão falhou " + "-1", Toast.LENGTH_LONG).show();
//                            else {
//                                Toast.makeText(ManterNecessidadeActivity.this, "Id inserido:" + idInserted, Toast.LENGTH_SHORT).show();
//                            }
                            finish();

                            if (getIntent().getExtras().getString(EXTRA_NECESSIDADE).equals("ADICIONAR_NECESSIDADE"))
                                Toast.makeText(ManterNecessidadeActivity.this, "Necessidade cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ManterNecessidadeActivity.this, "Necessidade alterada com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Fechar", null)
                    .show();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void btnFecharSalvarNecessidadeOnClick(){

        finish();

    }

    private void btnExcluirNecessidadeOnClick(View view) {

        try{

            AlertDialog alertDialog = new AlertDialog.Builder(ManterNecessidadeActivity.this)
                    //.setTitle(alertaDiaTitulo)
                    .setMessage("Deseja excluir necessidade?")
                    .setCancelable(false)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            SQLiteConnection bdEstoqueHelper = new SQLiteConnection(ManterNecessidadeActivity.this);
                            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                            bd.delete("RELATORIO","_id = ?", new String[] {Long.toString(idRelatorio)});
                            bd.close();
                            finish();

                            Toast.makeText(ManterNecessidadeActivity.this, "Necessidade excluída com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Fechar", null)
                    .show();


        } catch (SQLiteException e) {
            Toast.makeText(ManterNecessidadeActivity.this, "Deleção falhou", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(ManterNecessidadeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private long insereNecessidade(Boolean updateRow) {

        String nomeNecessidade = "";
        if(rbNecessidade1.isChecked()){
            if(rbNecessiadeEspontanea.isChecked()){
                nomeNecessidade = "Urina Espontânea";
            }else{
                nomeNecessidade = "Urina Sondagem";
            }
        }else{
            if(rbNecessiadeEspontanea.isChecked()){
                nomeNecessidade = "Fezes Espontânea";
            }else{
                nomeNecessidade = "Fezes Flit";
            }
        }

        if(updateRow){
            try {
                ContentValues cv = new ContentValues();
                cv.put("NOME", nomeNecessidade);
                cv.put("OBSERVACAO", edtObservacaoNecessidade.getText().toString());

                //pegar data atual
                //cv.put("DATA_INICIO", Calendar.getInstance(Locale.getDefault()).getTime().toString());

                SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
                SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
                return (int) bd.update("RELATORIO", cv, "_id = ?", new String[]{Long.toString(idRelatorio)});

            } catch (SQLiteException e) {
                Toast.makeText(this, "Atualização falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else {
            try {
                //SQLiteConnection = new SQLiteConnection(this);
//                bd = SQLiteConnection.getWritableDatabase();
//                ContentValues cv = new ContentValues();
//                cv.put("NOME", nomeNecessidade);
//                cv.put("OBSERVACAO", edtObservacaoNecessidade.getText().toString());
//                cv.put("DATA_INICIO", Calendar.getInstance(Locale.getDefault()).getTime().toString());
//                cv.put("CATEGORIA", "Necessidades Fisiológicas");
//                return (int) bd.insert("RELATORIO", null, cv);

                long idNecessidade = insereRelatorio();
                return idNecessidade;

            } catch (SQLiteException e) {
                Toast.makeText(this, "Criação falhou", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        SQLiteDatabase.close();
        return -1;
    }

    private Relatorio getNecessidadeActivity(){

        String nomeNecessidade = "";
        if(rbNecessidade1.isChecked()){
            if(rbNecessiadeEspontanea.isChecked()){
                nomeNecessidade = "Urina Espontânea";
            }else{
                nomeNecessidade = "Urina Sondagem";
            }
        }else{
            if(rbNecessiadeEspontanea.isChecked()){
                nomeNecessidade = "Fezes Espontânea";
            }else{
                nomeNecessidade = "Fezes Flit";
            }
        }

        this.relatorio = new Relatorio();

        this.relatorio.setNOME(nomeNecessidade);
        this.relatorio.setCATEGORIA("Necessidades Fisiológicas");
        this.relatorio.setOBSERVACAO(edtObservacaoNecessidade.getText().toString());
        this.relatorio.setDATA_INICIO(Calendar.getInstance(Locale.getDefault()).getTime().toString());

        return relatorio;
    }

    private long insereRelatorio(){
        RelatorioController relatorioController =  new RelatorioController(SQLiteConnection);
        return relatorioController.createRelatorioController(getNecessidadeActivity());
    }

}