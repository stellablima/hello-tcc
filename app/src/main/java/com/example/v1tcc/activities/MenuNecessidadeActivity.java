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

public class MenuNecessidadeActivity extends AppCompatActivity {

    private ListView lvNecessidadesMenu;
    private SimpleCursorAdapter cursorAdapter;
    private Button btnAdicionarNecessidade;
    public static final String EXTRA_ORIGEM_NECESSIDADE = "extraorigemnecessidade";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_necessidade);
    }

    @Override
    protected void onStart() {
        super.onStart();

        configurarCampos();
        setLvNecessidadesMenuAdapter();
    }

    private void configurarCampos(){

        btnAdicionarNecessidade = findViewById(R.id.btnAdicionarNecessidade);
        btnAdicionarNecessidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAdicionarNecessidadeOnClick(view);
            }
        });

        lvNecessidadesMenu = findViewById(R.id.lvNecessidadesMenu);
        lvNecessidadesMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MenuNecessidadeActivity.this, ManterNecessidadeActivity.class);

                Cursor cursor = (Cursor) cursorAdapter.getItem(position);
                intent.putExtra(ManterNecessidadeActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));

                if (getIntent().getExtras().getString(EXTRA_ORIGEM_NECESSIDADE).equals("MAIN"))
                    intent.putExtra(ManterNecessidadeActivity.EXTRA_NECESSIDADE, "CONSULTAR_NECESSIDADE");
                else
                    intent.putExtra(ManterNecessidadeActivity.EXTRA_NECESSIDADE, "EDITAR_NECESSIDADE");

                startActivity(intent);
            }
        });
    }

    private void btnAdicionarNecessidadeOnClick(View view){
        Intent intent = new Intent(this, ManterNecessidadeActivity.class);
        intent.putExtra(ManterNecessidadeActivity.EXTRA_NECESSIDADE, "ADICIONAR_NECESSIDADE");
        startActivity(intent);

    }

    private void setLvNecessidadesMenuAdapter() {
        try {
            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getReadableDatabase();

            Cursor cursor = bd.query(
                    "RELATORIO",
                new String[]{"_id", "NOME","OBSERVACAO","CATEGORIA", "DATA_INICIO"},
                    "CATEGORIA = ?",
                    new String[]{"Necessidades Fisiológicas"},
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
            lvNecessidadesMenu.setAdapter(cursorAdapter);
            bd.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
        }
    }
}