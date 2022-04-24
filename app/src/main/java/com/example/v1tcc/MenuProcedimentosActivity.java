package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuProcedimentosActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_procedimentos);
    }

    public void btnAdicionarProcedimentoOnClick(View view){
        Intent intent = new Intent(this, AdicionarProcedimentoActivity.class);
        intent.putExtra(AdicionarProcedimentoActivity.EXTRA_PROCEDIMENTO, "ADICIONAR_PROCEDIMENTO");
        startActivity(intent);

    }
}
//public static final String EXTRA_PROCEDIMENTO = "extraProcedimento";
//intent.putExtra(AlarmReceiver.EXTRA_PROCEDIMENTO, "ALTERAR_PROCEDIMENTO");
//intent.getExtras().getString(EXTRA_PROCEDIMENTO)
