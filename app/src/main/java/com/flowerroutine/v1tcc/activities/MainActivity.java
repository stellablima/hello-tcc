package com.flowerroutine.v1tcc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.flowerroutine.v1tcc.BDHelper.SQLiteConnection;
import com.flowerroutine.v1tcc.R;

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
    private TextView txDataMain;
    private TextView txtAviso;
    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean runnableStopped = false;
    private String alertaDiaTitulo;
    private String alertaDiaTexto;
    private ImageButton btnMenuMain;
    private ImageButton btnMenuVencimentos;
    private ImageButton btnMenuInstrucoes;
    private ImageButton btnMenuNecessidades;
    private ImageButton btnMenuOcorrencias;

    public static final String CHANNEL_ID = "ALARM_CHANNEL_NOTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BDRotinaHelper SQLiteConection = BDRotinaHelper.getInstanciaConexao(this);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); para manter tela sempre acesa no app

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Channel Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            // channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
        txDataMain = findViewById(R.id.txDataMain);

        btnMenuMain = findViewById(R.id.btnMenuMain);
        btnMenuMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMenuMainOnClick(view);
            }
        });

        btnMenuVencimentos = findViewById(R.id.btnMenuVencimentos);
        btnMenuVencimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMenuVencimentosOnClick(view);
                //btnMenuMainOnClick(view);
                /*

                btnMenuVencimentos
                btnMenuInstrucoes
                btnMenuAnortarAlimentacao
                btnMenuAnortarNecessidades
                btnMenuAnotarRelatorio

                 */
            }
        });

        btnMenuInstrucoes = findViewById(R.id.btnMenuInstrucoes);
        btnMenuInstrucoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMenuInstrucoesOnClick(view);
            }
        });

        btnMenuNecessidades = findViewById(R.id.btnMenuAnortarNecessidades);
        btnMenuNecessidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMenuNecessidadesOnClick(view);
            }
        });

        btnMenuOcorrencias = findViewById(R.id.btnMenuAnotarRelatorio);
        btnMenuOcorrencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMenuOcorrenciasOnClick(view);
            }
        });



        txtAviso = findViewById(R.id.txtAviso);
        txtAviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abre um txt com uma mensagem editavel, bot??es salvar, fechar
                //provisorio enquanto nao crio activit apropriada pra isso


                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(alertaDiaTitulo)
                        .setMessage(alertaDiaTexto)
                        .setCancelable(false)
                        .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //fecha modal
                                //abre activit de editar alerta do dia
                                //ou tentar mudar no proprio alerta
                                Intent intent = new Intent(MainActivity.this, ManterAlertaActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Fechar", null)
                    .show();

            }
        });

        lvRotinaMain = findViewById(R.id.lvProcedimentosMenuMain);
        lvRotinaMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProcedimentosActivity.class);

                Cursor cursor = (Cursor) cursorAdapterProcedimentos.getItem(position); // :D EBAAAAA, nao pera se o id ?? sequencial id ?? pode ser var mesmo https://www.rlsystem.com.br/forum/android/649-filtrar-dados-da-listview-com-uso-de-sqlite-e-simplecursoradapter-
                intent.putExtra(ProcedimentosActivity.EXTRA_ID,cursor.getLong(cursor.getColumnIndex("_id")));
                //Toast.makeText(MainActivity.this, "id:"+cursor.getLong(cursor.getColumnIndex("_id")), Toast.LENGTH_LONG).show();
                //cursor.getLong(cursor.getColumnIndex("_id"))
                //intent.putExtra(ProcedimentosActivity.EXTRA_TIPO, "procedimento");
                startActivity(intent);
            }
        });

        lvTarefasMain = findViewById(R.id.lvTarefasMain);
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
            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getReadableDatabase();
            Cursor cursor = bd.query("ESTADO",
                    new String[] {"_id", "TITULO", "TEXTO", "CATEGORIA"},
                    "CATEGORIA = ?",//CATEGORIA="Destaque" FLAG="1" (ATIVO)
                    new String[] {"Destaque"},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                alertaDiaTitulo = cursor.getString(cursor.getColumnIndexOrThrow("TITULO"));
                alertaDiaTexto = cursor.getString(cursor.getColumnIndexOrThrow("TEXTO"));

                ///Clique em editar para adicionar
                ///Adicionar alerta
                txt = findViewById(R.id.txtAviso);
                if(alertaDiaTitulo == "")
                    alertaDiaTitulo = "Adicionar destaque";
                if(alertaDiaTexto == "")
                    alertaDiaTexto = "Clique em editar para adicionar";

                txt.setText(alertaDiaTitulo);
            }
            else
                Toast.makeText(this, "Destaque n??o encontrado", Toast.LENGTH_SHORT).show();
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Falha no acesso ao Banco de Dados", Toast.LENGTH_LONG).show();
        }
    }

    private void atualizarHora(){
        //https://www.youtube.com/watch?v=Pj9XEimUCMM
        runnable = new Runnable() {
            @Override
            public void run() {
                if (runnableStopped)
                    return;
                Calendar calendar =  Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                String horaMinuto = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
                txtHoraMain.setText(horaMinuto);

                String dataAno = String.format("%02d/%02d/%04d", calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.YEAR));
                txDataMain.setText(dataAno);

                long agora = SystemClock.uptimeMillis();
                long proximo = agora + (1000-(agora%1000));

                handler.postAtTime(runnable,proximo);
            }
        };
        runnable.run();
    }

    private void btnMenuMainOnClick(View view){
        Intent intent = new Intent(this, MenuMainActivity.class);
        startActivity(intent);
    }

    private void btnMenuVencimentosOnClick(View view){
        Intent intent = new Intent(this, MenuVencimentoActivity.class);
        intent.putExtra(MenuVencimentoActivity.EXTRA_ORIGEM_VENCIMENTO, "MAIN");
        startActivity(intent);
    }

    private void btnMenuOcorrenciasOnClick(View view){
        Intent intent = new Intent(this, MenuOcorrenciaActivity.class);
        intent.putExtra(MenuOcorrenciaActivity.EXTRA_ORIGEM_OCORRENCIA, "MAIN");
        startActivity(intent);
    }

    private void btnMenuNecessidadesOnClick(View view){
        Intent intent = new Intent(this, MenuNecessidadeActivity.class);
        intent.putExtra(MenuNecessidadeActivity.EXTRA_ORIGEM_NECESSIDADE, "MAIN");
        startActivity(intent);
    }

    private void btnMenuInstrucoesOnClick(View view){
        Intent intent = new Intent(this, MenuInstrucaoActivity.class);
        intent.putExtra(MenuInstrucaoActivity.EXTRA_ORIGEM_INSTRUCAO, "MAIN");
        startActivity(intent);
    }

    private void setLvRotinaMainAdapter() {
        try {
            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getReadableDatabase();

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
            SQLiteConnection SQLiteConnection = new SQLiteConnection(this);
            SQLiteDatabase bd = SQLiteConnection.getReadableDatabase();

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

/*
Vai ser um caderninho
vai ter os defaults, o ideal seria unina alimenta????o, decubito ec
mas vai ter que deixar pra depois ainda sim fazer pensando nisso
ainda nos defaults vai ter os de expira????o pra datas de aparelho


CATEGORIAS: defaults, expira????o, toDos (protocolos de saida por ex e nao lembretes)

anotar e ver a melhor forma de compilar a principio
um caderno com os estados e filtro por categoria, como o relatorio
podendo clicar e expandir para edi????o

interface pra adicionar
> primeiro seleciona categoria confirma esconde tudo e abre a tela pra edi????o
> ao editar nao ?? possivel trocar a categoria/tipo de indica????o de estado
> alguns estados default podem ser fixados na tela inicial, a principio o escopo
> iria cobrir os defaults mas agora pensar em que fixar...
> ajudaria abrir uma tela de registro no caso a os botoes default seria pra inclus??o manual
> nos registros, adicionar trocar de fralda manualmente e registra no registro, adicionando categoria
> de higiene especifica talvez... tipo urina: e grava no relatorio a ultima fralda, mas como
> seria a sinaliza????o e intera????o com a tela principal?
*/