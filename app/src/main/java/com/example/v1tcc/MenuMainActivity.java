package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuMainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

    }

    public void btnProcedimentosOnClick(View view){
        Intent intent = new Intent(this, MenuProcedimentosActivity.class);
        startActivity(intent);
    }

    public void btnRelatoriosOnClick(View view){
        Intent intent = new Intent(this, RelatoriosActivity.class);
        startActivity(intent);
    }
}