package com.example.v1tcc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.v1tcc.BDHelper.SQLiteConnection;
import com.example.v1tcc.Helpers;
import com.example.v1tcc.R;
import com.example.v1tcc.controller.EstadoController;
import com.example.v1tcc.models.Estado;

public class ManterAlertaActivity extends AppCompatActivity {

    //public static final String EXTRA_ALERTA = "extratarefa";
    //public static final String EXTRA_ID = "idTarefa";
    private Button btnSalvarAlerta;
    private Button btnExcluirAlerta;
    private Button btnFecharAlerta;
    private EditText edtNomeAlerta;
    private EditText edtObservacaoAlerta;
    private long idProcedimento = 1;

    private SQLiteConnection SQLiteConnection;
    private SQLiteDatabase SQLiteDatabase;
    private Estado estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_alerta);

        SQLiteConnection = SQLiteConnection.getInstanciaConexao(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

       // if (getIntent().getExtras().getString(EXTRA_ALERTA).equals("ADICIONAR_TAREFA")) {
            //configurarCampos(true);

       // } else if (getIntent().getExtras().getString(EXTRA_ALERTA).equals("EDITAR_TAREFA")) {

        configurarCampos();
        carregaDados();
            //carregaDados();
            //Toast.makeText(this, "EXTRA_ID" + getIntent().getExtras().getLong(EXTRA_ID), Toast.LENGTH_SHORT).show();
            //Helpers.lvDinamico(getApplicationContext(),dataPrevisaoSplitado, lvRepeticaoDesproporcinalAlarme);
        //    }
}

    private void configurarCampos() {

        btnSalvarAlerta = findViewById(R.id.btnSalvarAlerta);
        btnFecharAlerta = findViewById(R.id.btnFecharAlerta);
        btnExcluirAlerta = findViewById(R.id.btnExcluirAlerta);
        edtNomeAlerta = findViewById(R.id.edtNomeAlerta);
        edtObservacaoAlerta = findViewById(R.id.edtObservacaoAlerta);

        btnSalvarAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSalvarAlertaOnClick(view);
            }
        });
        btnFecharAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFecharAlertaOnClick(view);
            }
        });
        btnExcluirAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnExcluirAlertaOnClick(view);
            }
        });
    }

    private void carregaDados() {
        try {

            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getReadableDatabase();
            Cursor cursor = bd.query("ESTADO",
                    new String[]{"_id", "TITULO", "TEXTO"},
                    "_id = ?",
                    new String[]{Long.toString(idProcedimento)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {

                edtNomeAlerta.setText(cursor.getString(cursor.getColumnIndexOrThrow("TITULO")));
                edtObservacaoAlerta.setText(cursor.getString(cursor.getColumnIndexOrThrow("TEXTO")));

            } else
                Toast.makeText(this, "Tarefa não encontrada", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Falha no acesso ao Banco de Dados " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void btnSalvarAlertaOnClick(View view){

        try {
            Helpers.preenchimentoValido(edtNomeAlerta);

            ContentValues cv = new ContentValues();
            cv.put("TITULO", edtNomeAlerta.getText().toString());
            cv.put("TEXTO", edtObservacaoAlerta.getText().toString());

            SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
            bd.update("ESTADO", cv, "_id = ?", new String[] {Long.toString(idProcedimento)});
            bd.close();
            finish();
        } catch (Exception e){
            Toast.makeText(this, "Falha ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        finish();

    }

    private void btnFecharAlertaOnClick(View view){

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);

        finish();
    }

    private void btnExcluirAlertaOnClick(View view){

        try {

            ContentValues cv = new ContentValues();
            cv.put("TITULO", "Adicionar aleta");
            cv.put("TEXTO", "Clique em editar para adicionar");
            SQLiteConnection bdEstoqueHelper = new SQLiteConnection(this);
            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
            bd.update("ESTADO", cv, "_id = ?", new String[] {Long.toString(idProcedimento)});
            bd.close();
            finish();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Criação falhou", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Estado getVencimentoActivity(){

        this.estado = new Estado();

//        this.estado.setTITULO(edtTituloVencimento.getText().toString());
//        this.estado.setTEXTO(edtTextoVencimento.getText().toString());
//update
        return estado;
    }

    private long updateEstado(){ //update
        EstadoController estadoController =  new EstadoController(SQLiteConnection);
        return estadoController.createEstadoController(getVencimentoActivity());
    }
}