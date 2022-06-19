package com.example.v1tcc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.v1tcc.BDHelper.SQLiteConnection;
import com.example.v1tcc.R;
import com.example.v1tcc.adapters.AdapterProcedimentos;
import com.example.v1tcc.controller.ProcedimentoController;
import com.example.v1tcc.models.Procedimento;

import java.util.ArrayList;
import java.util.List;

public class MenuProcedimentoActivity extends AppCompatActivity {

    private ListView lvProcedimentosMenu;
    private List<Procedimento> procedimentos = new ArrayList<>();
    private SimpleCursorAdapter cursorAdapter;
    private AdapterProcedimentos adapterProcedimentos;
    private Button btnAdicionarProcedimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_procedimentos);
    }

    @Override
    protected void onStart() {
        super.onStart();

        configurarCampos();
        setLvProcedimentosMenuAdapter();
    }

    private void btnAdicionarProcedimentoOnClick(View view){
        Intent intent = new Intent(this, ManterProcedimentoActivity.class);
        intent.putExtra(ManterProcedimentoActivity.EXTRA_PROCEDIMENTO, "ADICIONAR_PROCEDIMENTO");
        startActivity(intent);

    }

    private void setLvProcedimentosMenuAdapter() {
        try {

            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getReadableDatabase();

            Cursor cursor = bd.query(
                    "PROCEDIMENTO",
                    new String[]{"_id", "NOME","DATA_PREVISAO","FLAG"},
                    "FLAG = ?",
                    new String[]{"1"},
                    null,
                    null,
                    "DATA_PREVISAO"
            );
            cursorAdapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{"NOME", "DATA_PREVISAO"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            lvProcedimentosMenu.setAdapter(cursorAdapter);



            //lv personalizado para uso do DAO, futuramente* consultar material do Banin app exemplo
            /*adapterProcedimentos = new AdapterProcedimentos(MenuProcedimentoActivity.this, getProcedimentos());
            lvProcedimentosMenu.setAdapter(adapterProcedimentos);*/

        } catch (SQLiteException e) {
            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void configurarCampos(){

        btnAdicionarProcedimento = findViewById(R.id.btnProcedimento);
        btnAdicionarProcedimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAdicionarProcedimentoOnClick(view);
            }
        });

        lvProcedimentosMenu = findViewById(R.id.lvProcedimentosMenu);
        lvProcedimentosMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MenuProcedimentoActivity.this, ProcedimentosActivity.class);

                Cursor cursor = (Cursor) cursorAdapter.getItem(position);
                intent.putExtra(ProcedimentosActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));
                startActivity(intent);
            }
        });
    }

    private List<Procedimento> getProcedimentos(){

        //List<Procedimento> procedimentos = new ArrayList<>();
        ProcedimentoController procedimentoController = new ProcedimentoController(SQLiteConnection.getInstanciaConexao(MenuProcedimentoActivity.this));
        //procedimentos = procedimentoController.listProcedimentoController();

        //return procedimentos;
        return procedimentoController.listProcedimentoController();
    }
}
//public static final String EXTRA_PROCEDIMENTO = "extraProcedimento";
//intent.putExtra(AlarmReceiver.EXTRA_PROCEDIMENTO, "ALTERAR_PROCEDIMENTO");
//intent.getExtras().getString(EXTRA_PROCEDIMENTO)
