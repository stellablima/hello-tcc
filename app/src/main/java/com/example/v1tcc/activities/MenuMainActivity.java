package com.example.v1tcc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.v1tcc.R;

public class MenuMainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

    }

    public void btnProcedimentosOnClick(View view){
        Intent intent = new Intent(this, MenuProcedimentoActivity.class);
        startActivity(intent);
    }

    public void btnRelatoriosOnClick(View view){
        Intent intent = new Intent(this, MenuRelatorioActivity.class);
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
        Intent intent = new Intent(this, MenuVencimentoActivity.class);
        intent.putExtra(MenuVencimentoActivity.EXTRA_ORIGEM_VENCIMENTO, "MENU MAIN");
        startActivity(intent);
    }

    public void btnInstrucoesOnClick(View view){
        Intent intent = new Intent(this, MenuInstrucaoActivity.class);
        intent.putExtra(MenuInstrucaoActivity.EXTRA_ORIGEM_INSTRUCAO, "MENU MAIN");
        startActivity(intent);
    }

    public void btnOcorrenciasOnClick(View view){
        Intent intent = new Intent(this, MenuOcorrenciaActivity.class);
        intent.putExtra(MenuOcorrenciaActivity.EXTRA_ORIGEM_OCORRENCIA, "MENU MAIN");
        startActivity(intent);
    }

    public void btnNecessidadesOnClick(View view){
        Intent intent = new Intent(this, MenuNecessidadeActivity.class);
        intent.putExtra(MenuNecessidadeActivity.EXTRA_ORIGEM_NECESSIDADE, "MENU MAIN");
        startActivity(intent);
    }

}