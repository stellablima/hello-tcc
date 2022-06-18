package com.example.v1tcc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.v1tcc.R;
import com.example.v1tcc.RelatoriosActivity;

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

    public void btnTarefasOnClick(View view){
        Intent intent = new Intent(this, MenuTarefaActivity.class);
        startActivity(intent);
    }

    public void btnAlertaOnClickOnClick(View view){
        Intent intent = new Intent(this, ManterAlertaActivity.class);
        startActivity(intent);
    }

    public void btnVencimentosOnClick(View view){
        Intent intent = new Intent(this, MenuVencimentosActivity.class);
        startActivity(intent);
    }

    public void btnInstrucoesOnClick(View view){
        Intent intent = new Intent(this, MenuInstrucoesActivity.class);
        startActivity(intent);
    }

    public void btnOcorrenciasOnClick(View view){
        Intent intent = new Intent(this, MenuOcorrenciaActivity.class);
        startActivity(intent);
    }

    public void btnNecessidadesOnClick(View view){
        Intent intent = new Intent(this, MenuOcorrenciaActivity.class);
        startActivity(intent);
    }

}