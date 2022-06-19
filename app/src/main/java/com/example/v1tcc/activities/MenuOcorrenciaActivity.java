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

public class MenuOcorrenciaActivity extends AppCompatActivity {

    private ListView lvOcorrenciasMenu;
    private SimpleCursorAdapter cursorAdapter;
    private Button btnAdicionarOcorrencia;
    public static final String EXTRA_ORIGEM_OCORRENCIA = "extraorigemnocorrencia";

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
                Intent intent = new Intent(MenuOcorrenciaActivity.this, ManterOcorrenciaActivity.class);

                Cursor cursor = (Cursor) cursorAdapter.getItem(position);
                intent.putExtra(ManterOcorrenciaActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));

                if (getIntent().getExtras().getString(EXTRA_ORIGEM_OCORRENCIA).equals("MAIN"))
                    intent.putExtra(ManterOcorrenciaActivity.EXTRA_OCORRENCIA, "CONSULTAR_OCORRENCIA");
                else
                    intent.putExtra(ManterOcorrenciaActivity.EXTRA_OCORRENCIA, "EDITAR_OCORRENCIA");

                startActivity(intent);
            }
        });
    }

    private void btnAdicionarOcorrenciaOnClick(View view){
        Intent intent = new Intent(this, ManterOcorrenciaActivity.class);
        intent.putExtra(ManterOcorrenciaActivity.EXTRA_OCORRENCIA, "ADICIONAR_OCORRENCIA");
        startActivity(intent);

    }

    private void setLvOcorrenciasMenuAdapter() {
        try {
            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getReadableDatabase();

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