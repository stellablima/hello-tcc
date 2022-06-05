package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.v1tcc.BDHelper.SQLiteConnection;

public class RelatoriosActivity extends AppCompatActivity {

    private ListView lvRelatorio;
    private SimpleCursorAdapter cursorAdapter;
    private Spinner spnCategoriaRelatorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);
        lvRelatorio = findViewById(R.id.lvRelatorios);
        spnCategoriaRelatorio = findViewById(R.id.spnCategoriaRelatorio);
        setLvRelatorioOnItemClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLvRelatorioAdapter("Medicação");

        spnCategoriaRelatorio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                setLvRelatorioAdapter(spnCategoriaRelatorio.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setLvRelatorioOnItemClick(){
//        lvRelatorio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Intent intent = new Intent(RelatoriosActivity.this, ProcedimentosActivity.class);
//
//                Cursor cursor = (Cursor) cursorAdapter.getItem(position); // :D EBAAAAA, nao pera se o id é sequencial id é pode ser var mesmo https://www.rlsystem.com.br/forum/android/649-filtrar-dados-da-listview-com-uso-de-sqlite-e-simplecursoradapter-
//                intent.putExtra(ProcedimentosActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));
//                //Toast.makeText(MainActivity.this, "id:"+cursor.getLong(cursor.getColumnIndex("_id")), Toast.LENGTH_LONG).show();
//                //cursor.getLong(cursor.getColumnIndex("_id"))
//                //intent.putExtra(ProcedimentosActivity.EXTRA_TIPO, "procedimento");
//                startActivity(intent);
//            }
//        });
    }

    private void setLvRelatorioAdapter(String categoria) {
        try {
            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getReadableDatabase();

            Cursor cursor = bd.query(
                    "RELATORIO",
                    new String[]{"_id", "_id_PROCEDIMENTO", "CATEGORIA", "DATA_INICIO", "DATA_PREVISAO", "NOME"},
                    "CATEGORIA = ?",
                    new String[]{categoria},
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
            lvRelatorio.setAdapter(cursorAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
        }
    }

}