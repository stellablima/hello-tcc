package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class AdicionarNecessidadeActivity extends AppCompatActivity {

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
    private BDRotinaHelper bdRotinaHelper;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private long idRelatorio;
    private TextView txtDataInicioNecessidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_necessidade);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getExtras().getString(EXTRA_NECESSIDADE).equals("ADICIONAR_NECESSIDADE")) {
            configurarCampos(true);

        } else if (getIntent().getExtras().getString(EXTRA_NECESSIDADE).equals("EDITAR_NECESSIDADE")) {

            configurarCampos(false);
            carregaDados();
            Toast.makeText(this, "EXTRA_ID" + getIntent().getExtras().getLong(EXTRA_ID), Toast.LENGTH_SHORT).show();

        }
    }

    private void configurarCampos(Boolean cadastrarOcorrencia) {
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
                //edtNomeNecessidade.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
                /*devolver os checkeds por split ou... nao deixar editar*/
                edtObservacaoNecessidade.setText(cursor.getString(cursor.getColumnIndexOrThrow("OBSERVACAO")));
                //txtDataInicioNecessidade.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATA_INICIO")));
            } else
                Toast.makeText(this, "Não encontrado", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Falha no acesso ao Banco de Dados " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void btnSalvarNecessidadeOnClick(View view, Boolean updateRow){
        try {

            //Helpers.preenchimentoValido(edtNomeOcorrencia);

            int idInserted = insereNecessidade(updateRow);
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

    private void btnFecharSalvarNecessidadeOnClick(){

        finish();

    }

    private void btnExcluirNecessidadeOnClick(View view) {
        BDRotinaHelper bdEstoqueHelper = new BDRotinaHelper(this);
        SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
        bd.delete("RELATORIO","_id = ?", new String[] {Long.toString(idRelatorio)});
        bd.close();
        finish();
    }

    private int insereNecessidade(Boolean updateRow) {

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
                nomeNecessidade = "Fezes Sondagem";
            }
        }

        if(updateRow){
            try {
                ContentValues cv = new ContentValues();
                cv.put("NOME", nomeNecessidade);
                cv.put("OBSERVACAO", edtObservacaoNecessidade.getText().toString());

                //pegar data atual
                //cv.put("DATA_INICIO", Calendar.getInstance(Locale.getDefault()).getTime().toString());

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
                cv.put("NOME", nomeNecessidade);
                cv.put("OBSERVACAO", edtObservacaoNecessidade.getText().toString());
                cv.put("DATA_INICIO", Calendar.getInstance(Locale.getDefault()).getTime().toString());
                cv.put("CATEGORIA", "Necessidades Fisiológicas");
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