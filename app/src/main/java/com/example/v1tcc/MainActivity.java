package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView lvRotinaMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvRotinaMain = findViewById(R.id.lvRotinaMain);
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
                    new String[]{"_id, NOME", "DATA_PREVISAO"},
                    null,
                    null,
                    null,
                    null,
                    "DATA_PREVISAO"
            );
            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
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