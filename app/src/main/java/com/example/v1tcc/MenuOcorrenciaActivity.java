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

public class MenuOcorrenciaActivity extends AppCompatActivity {

    private ListView lvOcorrenciasMenu;
    private SimpleCursorAdapter cursorAdapter;
    private Button btnAdicionarOcorrencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ocorrencia);
    }

    @Override
    protected void onStart() {
        super.onStart();

        configurarCampos();
        setLvOcorrenciasMenuAdapter();
    }

    private void configurarCampos(){

        btnAdicionarOcorrencia = findViewById(R.id.btnAdicionarOcorrencia);
        btnAdicionarOcorrencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAdicionarOcorrenciaOnClick(view);
            }
        });

        lvOcorrenciasMenu = findViewById(R.id.lvOcorrenciasMenu);
        lvOcorrenciasMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MenuOcorrenciaActivity.this, AdicionarOcorrenciaActivity.class);

                Cursor cursor = (Cursor) cursorAdapter.getItem(position);
                intent.putExtra(AdicionarOcorrenciaActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));
                intent.putExtra(AdicionarOcorrenciaActivity.EXTRA_OCORRENCIA, "EDITAR_OCORRENCIA");
                startActivity(intent);
            }
        });
    }

    private void btnAdicionarOcorrenciaOnClick(View view){
        Intent intent = new Intent(this, AdicionarOcorrenciaActivity.class);
        intent.putExtra(AdicionarOcorrenciaActivity.EXTRA_OCORRENCIA, "ADICIONAR_OCORRENCIA");
        startActivity(intent);

    }

    private void setLvOcorrenciasMenuAdapter() {
        try {
            BDRotinaHelper bdRotinaHelper = new BDRotinaHelper(this);
            SQLiteDatabase bd = bdRotinaHelper.getReadableDatabase();

            Cursor cursor = bd.query(
                    "RELATORIO",
                    new String[]{"_id", "NOME","OBSERVACAO","CATEGORIA", "DATA_INICIO"},
                    "CATEGORIA = ?",
                    new String[]{"Ocorrência"},
                    null,
                    null,
                    "_id DESC"
            );
            cursorAdapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{"NOME", "DATA_INICIO"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            lvOcorrenciasMenu.setAdapter(cursorAdapter);
            bd.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
        }
    }
}