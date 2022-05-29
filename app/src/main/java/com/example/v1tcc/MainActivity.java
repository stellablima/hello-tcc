package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ListView lvRotinaMain;
    private ListView lvTarefasMain;
    private SimpleCursorAdapter cursorAdapterProcedimentos;
    private SimpleCursorAdapter cursorAdapterTarefas;
//    private BDRotinaHelper bdRotinaHelper;
//    private SQLiteDatabase bd;
//    private Cursor cursor;
    private TextView txtHoraMain;
    private TextView txtAviso;
    private Handler handler= new Handler();
    private Runnable runnable;
    private boolean runnableStopped = false;
    private String alertaDiaTitulo;
    private String alertaDiaTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvRotinaMain = findViewById(R.id.lvProcedimentosMenuMain);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); para manter tela sempre acesa no app
    }

    @Override
    protected void onStart() {
        super.onStart();
        configurarCampos();
        carregaDadosAviso();
        setLvRotinaMainAdapter();
        setLvTarefasMainAdapter();
        //MainActivity.this.deleteDatabase("rotina");
        //bdRotinaHelper.onCreate(bd);
        //bdRotinaHelper.insereDadosAlertaDia(bd);
        //bdRotinaHelper.insereDadosTarefas(bd);
        //bdRotinaHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        runnableStopped = false;
        atualizarHora();
    }

    @Override
    protected void onStop() {
        super.onStop();

        runnableStopped = true;
    }

    private void configurarCampos(){
        txtHoraMain = findViewById(R.id.txtHoraMain);
        txtAviso = findViewById(R.id.txtAviso);
        lvTarefasMain = findViewById(R.id.lvTarefasMain);

        txtAviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abre um txt com uma mensagem editavel, botões salvar, fechar
                //provisorio enquanto nao crio activit apropriada pra isso


                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(alertaDiaTitulo)
                        .setMessage(alertaDiaTexto +
                                "\n lorem ipsum" +
                                "\n lorem ipsum" +
                                "\n lorem ipsum" +
                                "\n lorem ipsum" +
                                "\n lorem ipsum" +
                                "\n lorem ipsum" +
                                "\n lorem ipsum")
                        .setCancelable(false)
                        .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("Fechar", null)
                    .show();

            }
        });

        lvRotinaMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProcedimentosActivity.class);

                Cursor cursor = (Cursor) cursorAdapterProcedimentos.getItem(position); // :D EBAAAAA, nao pera se o id é sequencial id é pode ser var mesmo https://www.rlsystem.com.br/forum/android/649-filtrar-dados-da-listview-com-uso-de-sqlite-e-simplecursoradapter-
                intent.putExtra(ProcedimentosActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));
                //Toast.makeText(MainActivity.this, "id:"+cursor.getLong(cursor.getColumnIndex("_id")), Toast.LENGTH_LONG).show();
                //cursor.getLong(cursor.getColumnIndex("_id"))
                //intent.putExtra(ProcedimentosActivity.EXTRA_TIPO, "procedimento");
                startActivity(intent);
            }
        });

        lvTarefasMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(MainActivity.this, TarefaActivity.class);

                Cursor cursor = (Cursor) cursorAdapterTarefas.getItem(position);
                intent.putExtra(TarefaActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));
                startActivity(intent);
            }
        });
    }

    private void carregaDadosAviso() {
        TextView txt;
        String s;
        int valor; //txtHoraProcedimento
        try {
            int idAlertaDia = 1 ;
            BDRotinaHelper bdRotinaHelper = new BDRotinaHelper(this);
            SQLiteDatabase bd = bdRotinaHelper.getReadableDatabase();
            Cursor cursor = bd.query("ESTADO",
                    new String[] {"_id", "TITULO", "TEXTO"},
                    "_id = ?",
                    new String[] {Long.toString(idAlertaDia)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                alertaDiaTitulo = cursor.getString(cursor.getColumnIndexOrThrow("TITULO"));
                alertaDiaTexto = cursor.getString(cursor.getColumnIndexOrThrow("TEXTO"));
                txt = findViewById(R.id.txtAviso);
                txt.setText(alertaDiaTitulo);

            }
            else
                Toast.makeText(this, "Alerta não encontrado", Toast.LENGTH_SHORT).show();
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Falha no acesso ao Banco de Dados", Toast.LENGTH_LONG).show();
        }
    }

    private void atualizarHora(){
        https://www.youtube.com/watch?v=Pj9XEimUCMM
        runnable = new Runnable() {
            @Override
            public void run() {
                if (runnableStopped)
                    return;
                Calendar calendar =  Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                String horaMinuto = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
                txtHoraMain.setText(horaMinuto);

                long agora = SystemClock.uptimeMillis();
                long proximo = agora + (1000-(agora%1000));

                handler.postAtTime(runnable,proximo);
            }
        };
        runnable.run();
    }

    public void btnMenuMainOnClick(View view){
        Intent intent = new Intent(this, MenuMainActivity.class);
        startActivity(intent);
    }

    private void setLvRotinaMainAdapter() {
        try {
            BDRotinaHelper bdRotinaHelper = new BDRotinaHelper(this);
            SQLiteDatabase bd = bdRotinaHelper.getReadableDatabase();

            Cursor cursor = bd.query(
                    "PROCEDIMENTO",
                    new String[]{"_id", "NOME","DATA_PREVISAO","FLAG"},
                    "FLAG = ?",
                    new String[]{"1"},
                    null,
                    null,
                    "DATA_PREVISAO"
            );
            cursorAdapterProcedimentos = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{"NOME", "DATA_PREVISAO"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            lvRotinaMain.setAdapter(cursorAdapterProcedimentos);
        } catch (SQLiteException e) {
            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void setLvTarefasMainAdapter() {
        try {
            BDRotinaHelper bdRotinaHelper = new BDRotinaHelper(this);
            SQLiteDatabase bd = bdRotinaHelper.getReadableDatabase();

            Cursor cursor = bd.query(
                    "PROCEDIMENTO",
                    new String[]{"_id", "NOME","OBSERVACAO","FLAG"},
                    "FLAG = ?",
                    new String[]{"3"},
                    null,
                    null,
                    "_id DESC"
            );
            cursorAdapterTarefas = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{"NOME", "OBSERVACAO"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            lvTarefasMain.setAdapter(cursorAdapterTarefas);
        } catch (SQLiteException e) {
            Toast.makeText(this, "Erro: " + e, Toast.LENGTH_LONG).show();
        }
    }
}