package com.example.v1tcc;

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

import com.example.v1tcc.BDHelper.SQLiteConnection;

public class MenuInstrucoesActivity extends AppCompatActivity {

    private ListView lvInstrucoesMenu;
    private SimpleCursorAdapter cursorAdapter;
    private Button btnAdicionarInstrucao;

    /*um alerta é uma linha de estado de instrução que fica fixado na tela principal*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_instrucoes);
    }

    @Override
    protected void onStart() {
        super.onStart();

        configurarCampos();
        setLvInstrucoesMenuAdapter();

//        BDRotinaHelper bdEstoqueHelper = new BDRotinaHelper(this);
//        SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
//        BDRotinaHelper.insereDadosInstrucaoDia(bd);

    //{
//            ContentValues cv = new ContentValues();
//            cv.put("CATEGORIA", "Alerta"); //a categoria é como se fosse um agrupamento mesmo
//            cv.put("TITULO", "Clique para adicionar");
//            cv.put("FLAG", "1");//default ativo
//            cv.put("TEXTO", "Verificar a real necessidade de existtir um alerta na tela principal, e se isso seria manual, verificar a viabilidade e uso disso no app, pode edixar na verdade e na semana teste uso ver s situaçãoo e o uso disso, ao meu ver talvez seria manual nao sei \n melhore revisao o escopo, lorem ipsun loren ispsuj lotens ispsum");
//            bd.insert("ESTADO", null, cv);
      //  }

    }

    private void configurarCampos(){

        btnAdicionarInstrucao = findViewById(R.id.btnAdicionarInstrucao);
        btnAdicionarInstrucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAdicionarInstrucaoOnClick(view);
            }
        });

        lvInstrucoesMenu = findViewById(R.id.lvInstrucoesMenu);
        lvInstrucoesMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MenuInstrucoesActivity.this, AdicionarInstrucaoActivity.class);

                Cursor cursor = (Cursor) cursorAdapter.getItem(position);
                intent.putExtra(AdicionarInstrucaoActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));
                intent.putExtra(AdicionarInstrucaoActivity.EXTRA_ESTADO, "EDITAR_INSTRUCAO");
                startActivity(intent);
            }
        });
    }

    private void btnAdicionarInstrucaoOnClick(View view){
        Intent intent = new Intent(this, AdicionarInstrucaoActivity.class);
        intent.putExtra(AdicionarInstrucaoActivity.EXTRA_ESTADO, "ADICIONAR_INSTRUCAO");
        startActivity(intent);
    }

    private void setLvInstrucoesMenuAdapter(){
        try {
            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getReadableDatabase();

            Cursor cursor = bd.query(
                    "ESTADO",
                    new String[]{"_id", "TITULO","TEXTO","CATEGORIA"},
                    "CATEGORIA = ?",
                    new String[]{"Instrucao"},
                    null,
                    null,
                    "_id DESC"
            );
            cursorAdapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"TITULO", "TITULO"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            lvInstrucoesMenu.setAdapter(cursorAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
        }
    }


    /*
    pode ser a lista de instruções na tela principal com clique para expandir e ver
    e no menu botao de adicionar e clique de edição(no expandido)
    * */
}