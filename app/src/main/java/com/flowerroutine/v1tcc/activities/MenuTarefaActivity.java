package com.flowerroutine.v1tcc.activities;

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

import com.flowerroutine.v1tcc.BDHelper.SQLiteConnection;
import com.flowerroutine.v1tcc.R;

public class MenuTarefaActivity extends AppCompatActivity {

    private ListView lvTarefasMenu;
    private SimpleCursorAdapter cursorAdapter;
    private Button btnAdicionarTarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tarefa);

    }

    @Override
    protected void onStart() {
        super.onStart();

        configurarCampos();
        setLvTarefasMenuAdapter();
    }

    private void configurarCampos(){

        btnAdicionarTarefa = findViewById(R.id.btnAdicionarTarefa);
        btnAdicionarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAdicionarTarefaOnClick(view);
            }
        });

        lvTarefasMenu = findViewById(R.id.lvTarefasMenu);
        lvTarefasMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MenuTarefaActivity.this, ManterTarefaActivity.class);

                Cursor cursor = (Cursor) cursorAdapter.getItem(position);
                intent.putExtra(ManterTarefaActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));
                intent.putExtra(ManterTarefaActivity.EXTRA_TAREFA, "EDITAR_TAREFA");
                startActivity(intent);
            }
        });
    }

    private void btnAdicionarTarefaOnClick(View view){
        Intent intent = new Intent(this, ManterTarefaActivity.class);
        intent.putExtra(ManterTarefaActivity.EXTRA_TAREFA, "ADICIONAR_TAREFA");
        startActivity(intent);

    }

    private void setLvTarefasMenuAdapter() {
        try {
            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getReadableDatabase();

            Cursor cursor = bd.query(
                    "PROCEDIMENTO",
                    new String[]{"_id", "NOME","OBSERVACAO","FLAG"},
                    "FLAG = ?",
                    new String[]{"3"},
                    null,
                    null,
                    "_id DESC"
            );
            cursorAdapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{"NOME", "OBSERVACAO"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            lvTarefasMenu.setAdapter(cursorAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
        }
    }
}