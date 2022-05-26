package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuTarefaActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tarefa);
    }

    public void btnAdicionarTarefaOnClick(View view){
        Intent intent = new Intent(this, AdicionarTarefaActivity.class);
        intent.putExtra(AdicionarTarefaActivity.EXTRA_TAREFA, "ADICIONAR_TAREFA");
        startActivity(intent);

    }
}