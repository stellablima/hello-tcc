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

public class MenuVencimentosActivity extends AppCompatActivity {

    private ListView lvVencimentosMenu;
    private SimpleCursorAdapter cursorAdapter;
    private Button btnAdicionarVencimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_vencimentos);

    }

    @Override
    protected void onStart() {
        super.onStart();

        configurarCampos();
        setLvVencimentosMenuAdapter();
    }

    private void configurarCampos(){

        btnAdicionarVencimento = findViewById(R.id.btnAdicionarVencimento);
        btnAdicionarVencimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAdicionarVencimentoOnClick(view);
            }
        });

        lvVencimentosMenu = findViewById(R.id.lvVencimentosMenu);
        lvVencimentosMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MenuVencimentosActivity.this, ManterVencimentoActivity.class);

                Cursor cursor = (Cursor) cursorAdapter.getItem(position);
                intent.putExtra(ManterVencimentoActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));
                intent.putExtra(ManterVencimentoActivity.EXTRA_VENCIMENTO, "EDITAR_VENCIMENTO");
                startActivity(intent);
            }
        });
    }

    private void btnAdicionarVencimentoOnClick(View view){
        Intent intent = new Intent(this, ManterVencimentoActivity.class);
        intent.putExtra(ManterVencimentoActivity.EXTRA_VENCIMENTO, "ADICIONAR_VENCIMENTO");
        startActivity(intent);

    }

    private void setLvVencimentosMenuAdapter() {
        try {
            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getReadableDatabase();

            Cursor cursor = bd.query(
                    "ESTADO",
                    new String[]{"_id", "TITULO","TEXTO","DATA_ULTIMA_OCORRENCIA","FLAG"},
                    "Categoria = ?",
                    new String[]{"Vencimento"},
                    null,
                    null,
                    "_id DESC"
            );
            cursorAdapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{"TITULO", "DATA_ULTIMA_OCORRENCIA"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            lvVencimentosMenu.setAdapter(cursorAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
        }
    }
}