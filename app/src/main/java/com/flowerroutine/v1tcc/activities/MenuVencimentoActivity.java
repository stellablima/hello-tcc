package com.flowerroutine.v1tcc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.flowerroutine.v1tcc.BDHelper.SQLiteConnection;
import com.flowerroutine.v1tcc.R;

public class MenuVencimentoActivity extends AppCompatActivity {

    private ListView lvVencimentosMenu;
    private SimpleCursorAdapter cursorAdapter;
    private Button btnAdicionarVencimento;
    public static final String EXTRA_ORIGEM_VENCIMENTO = "extraorigemvencimento";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_vencimentos);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getExtras().getString(EXTRA_ORIGEM_VENCIMENTO).equals("MAIN")) {
            configurarCampos(false);
        }else{ //getIntent().getExtras().getString(EXTRA_ORIGEM_INSTRUCAO).equals("MENU MAIN")
            configurarCampos(true);
        }

        setLvVencimentosMenuAdapter();
    }

    private void configurarCampos(Boolean origemMenuMain){

        btnAdicionarVencimento = findViewById(R.id.btnAdicionarVencimento);
        if(origemMenuMain){
            btnAdicionarVencimento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnAdicionarVencimentoOnClick(view);
                }
            });
        }else {
            btnAdicionarVencimento.setVisibility(View.GONE);
        }


        lvVencimentosMenu = findViewById(R.id.lvVencimentosMenu);
        lvVencimentosMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MenuVencimentoActivity.this, ManterVencimentoActivity.class);

                Cursor cursor = (Cursor) cursorAdapter.getItem(position);
                intent.putExtra(ManterVencimentoActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));

                if (getIntent().getExtras().getString(EXTRA_ORIGEM_VENCIMENTO).equals("MAIN"))
                    intent.putExtra(ManterVencimentoActivity.EXTRA_VENCIMENTO, "CONSULTAR_VENCIMENTO");
                else
                    intent.putExtra(ManterVencimentoActivity.EXTRA_VENCIMENTO, "EDITAR_VENCIMENTO");

                startActivity(intent);
            }
        });
    }

    private void btnAdicionarVencimentoOnClick(View view){
        Intent intent = new Intent(this, ManterVencimentoActivity.class);
        intent.putExtra(ManterVencimentoActivity.EXTRA_VENCIMENTO, "ADICIONAR_VENCIMENTO");
        startActivity(intent);

    }

    private void setLvVencimentosMenuAdapter() {
        try {
            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getReadableDatabase();

            Cursor cursor = bd.query(
                    "ESTADO",
                    new String[]{"_id", "TITULO","TEXTO","DATA_ULTIMA_OCORRENCIA","FLAG"},
                    "Categoria = ?",
                    new String[]{"Vencimento"},
                    null,
                    null,
                    "_id DESC"
            );
            cursorAdapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{"TITULO", "DATA_ULTIMA_OCORRENCIA"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            lvVencimentosMenu.setAdapter(cursorAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
        }
    }
}