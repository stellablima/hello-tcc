package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Procedimentos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedimentos);
    }

    public void btnAdicionarProcedimentoOnClick(View view){
        Intent intent = new Intent(this, AdicionarProcedimento.class);
        startActivity(intent);
    }
}