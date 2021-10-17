package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AdicionarProcedimento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_procedimento);
    }

    public void btnSalvarProcedimentoOnClick(View view){
        Toast.makeText(this, "Salvo", Toast.LENGTH_SHORT).show();
    }
}