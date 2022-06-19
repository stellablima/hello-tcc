package com.example.v1tcc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v1tcc.BDHelper.SQLiteConnection;
import com.example.v1tcc.R;

import java.util.Calendar;
import java.util.Locale;

public class TarefaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        txtNomeTarefa=findViewById(R.id.txtNomeTarefa);
        txtObservacaoTarefa=findViewById(R.id.txtObservacaoTarefa);
    }

    public static final String EXTRA_ID = "idprocedimento";

    private TextView txtNomeTarefa;
    private TextView txtObservacaoTarefa;
    private Long idProcedimento;
    private SQLiteConnection SQLiteConnection;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private Button btnManterTarefa;
    private Button btnFecharTarefa;
    private ContentValues cv;
    SimpleCursorAdapter cursorAdapter;


    @Override
    protected void onStart() {
        super.onStart();
        configuraCampos();
        carregaDados();

    }

    private void carregaDados() {
        TextView txt;
        String s;
        int valor; //txtHoraProcedimento
        try {
            idProcedimento = getIntent().getExtras().getLong(EXTRA_ID);
            SQLiteConnection = new SQLiteConnection(this);
            bd = SQLiteConnection.getReadableDatabase();
            cursor = bd.query("PROCEDIMENTO",
                    new String[] {"_id", "NOME", "OBSERVACAO"},
                    "_id = ?",
                    new String[] {Long.toString(idProcedimento)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                txt = findViewById(R.id.txtNomeTarefa);
                txt.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
                txt = findViewById(R.id.txtObservacaoTarefa);
                txt.setText(cursor.getString(cursor.getColumnIndexOrThrow("OBSERVACAO")));
            }
            else
                Toast.makeText(this, "Tarefa não encontrada", Toast.LENGTH_SHORT).show();
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Falha no acesso ao Banco de Dados", Toast.LENGTH_LONG).show();
        }
    }

    private void configuraCampos(){
        txtNomeTarefa = findViewById(R.id.txtNomeTarefa);
        txtObservacaoTarefa = findViewById(R.id.txtObservacaoTarefa);

        btnManterTarefa = findViewById(R.id.btnManterTarefa);
        btnFecharTarefa = findViewById(R.id.btnFecharTarefa);

        btnFecharTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnManterTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realizarTarefa();
            }
        });
    }

    private void realizarTarefa(){
        //preencher data inicio na tabela prcedimento em hora? dedsnecessario ao meu ver, pelo menos pora gora, mas prencher em relatorio fundamental

        try{
            //concluir tarefa na tabela procedimentos
            cv = new ContentValues();
            cv.put("FLAG", "4");

            SQLiteConnection = new SQLiteConnection(this);
            bd = SQLiteConnection.getWritableDatabase();

            bd.update("PROCEDIMENTO", cv, "_id = ?", new String[] {Long.toString(idProcedimento)});

            if (cursor.moveToFirst())
                Toast.makeText(this, "Tarefa concluída", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(this, "Não foi possível concluir a tarefa", Toast.LENGTH_SHORT).show();
                //tem que dar exeption, provavelmente o sql de?
            }
            bd.close();

            //gravar relatório //utilizar errumar em POO depois esses trechos
            cv = new ContentValues();
            cv.put("_id_PROCEDIMENTO", idProcedimento);
            cv.put("CATEGORIA", "Tarefa");
            cv.put("DATA_INICIO", Calendar.getInstance(Locale.getDefault()).getTime().toString());
            //cv.put("DATA_INICIO", Calendar.getInstance(Locale.BRAZIL).getTime().toString());
            cv.put("NOME", txtNomeTarefa.getText().toString());
            //cv.put("OBSERVACAO", txtObservacaoTarefa.getText().toString());

            SQLiteConnection = new SQLiteConnection(this);
            bd = SQLiteConnection.getWritableDatabase();
            int idInserted = (int) bd.insert("RELATORIO", null, cv);

            if(idInserted > 1)
                Toast.makeText(this,"Gravado em relatório", Toast.LENGTH_LONG).show();
            else //////////////////ERRO AO GRAVAR<--
                Toast.makeText(this, "Erro ao gravar(idInserted):"+idInserted, Toast.LENGTH_LONG).show();

            bd.close();


        } catch (SQLiteException e) {
            Toast.makeText(this, "Conclusão de tarefa falhou: "+e, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        finish();
    }

}