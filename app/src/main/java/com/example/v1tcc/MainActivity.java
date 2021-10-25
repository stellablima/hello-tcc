package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView lvRotinaMain;
    SimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvRotinaMain = findViewById(R.id.lvRotinaMain);
        setLvEstoqueOnItemClick();
    }

    private void setLvEstoqueOnItemClick(){
        lvRotinaMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProcedimentosActivity.class);

                Cursor cursor = (Cursor) cursorAdapter.getItem(position); // :D EBAAAAA
                intent.putExtra(ProcedimentosActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));

                //intent.putExtra(ProcedimentosActivity.EXTRA_ID, "_id");//passar o id aqui <-
                //intent.putExtra(ProcedimentosActivity.EXTRA_TIPO, "procedimento");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLvRotinaMainAdapter();
    }

    public void btnMenuMainOnClick(View view){
        Intent intent = new Intent(this, MenuMain.class);
        startActivity(intent);
    }

    private void setLvRotinaMainAdapter() {
        try {
            BDRotinaHelper bdRotinaHelper = new BDRotinaHelper(this);
            SQLiteDatabase bd = bdRotinaHelper.getReadableDatabase();

            Cursor cursor = bd.query(
                    "PROCEDIMENTO",
                    new String[]{"_id", "NOME", "DATA_PREVISAO"},
                    null,
                    null,
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
            lvRotinaMain.setAdapter(cursorAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
        }
    }
}