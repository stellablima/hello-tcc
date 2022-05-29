package com.example.v1tcc;

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

public class MenuProcedimentosActivity extends AppCompatActivity {

    private ListView lvProcedimentosMenu;
    private SimpleCursorAdapter cursorAdapter;
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
        Intent intent = new Intent(this, AdicionarProcedimentoActivity.class);
        intent.putExtra(AdicionarProcedimentoActivity.EXTRA_PROCEDIMENTO, "ADICIONAR_PROCEDIMENTO");
        startActivity(intent);

    }

    private void setLvProcedimentosMenuAdapter() {
        try {
            BDRotinaHelper bdRotinaHelper = new BDRotinaHelper(this);
            SQLiteDatabase bd = bdRotinaHelper.getReadableDatabase();

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
        } catch (SQLiteException e) {
            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void configurarCampos(){

        btnAdicionarProcedimento = findViewById(R.id.btnAdicionarProcedimento);
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
                Intent intent = new Intent(MenuProcedimentosActivity.this, ProcedimentosActivity.class);

                Cursor cursor = (Cursor) cursorAdapter.getItem(position);
                intent.putExtra(ProcedimentosActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));
                startActivity(intent);
            }
        });
    }
}
//public static final String EXTRA_PROCEDIMENTO = "extraProcedimento";
//intent.putExtra(AlarmReceiver.EXTRA_PROCEDIMENTO, "ALTERAR_PROCEDIMENTO");
//intent.getExtras().getString(EXTRA_PROCEDIMENTO)
