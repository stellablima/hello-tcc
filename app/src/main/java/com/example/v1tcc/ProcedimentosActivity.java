package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ProcedimentosActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "idestoque";
    //public static final String EXTRA_TIPO = "tipoevento";

    private TextView txtNomeProcedimento;
    private TextView txtHoraProcedimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedimentos);
        txtNomeProcedimento=findViewById(R.id.txtNomeProcedimento);
        txtHoraProcedimento=findViewById(R.id.txtHoraProcedimento);
        //teste
        txtNomeProcedimento.setText(String.valueOf(getIntent().getExtras().getLong(EXTRA_ID)));

    }

    @Override
    protected void onStart() {
        super.onStart();
        carregaDados();
    }

    private void carregaDados() {
        TextView txt;
        String s;
        int valor; //txtHoraProcedimento
//        try {
//            idEstq = getIntent().getExtras().getLong(EXTRA_ID);
//
//            Toast.makeText(this, "id: "+ idEstq, Toast.LENGTH_SHORT).show();
//
//            bdEstoqueHelper = new BDEstoqueHelper(this);
//            bd = bdEstoqueHelper.getReadableDatabase();
//            // Podemos criar o cursor com rawQuery()
//            //cursor = bd.rawQuery("select _id, CODIGO, NOME, UNID, QTDE, PCUNIT from ESTOQUE where _id = ?",
//            // new String[] {Long.toString(idEstq)} );
//            // ou com query(). O rawQuery foi usado na app anterior (Biblioteca) e aqui usamos query()
//            cursor = bd.query("PROCEDIMENTO",
//                    new String[] {"_id", "CODIGO", "NOME", "UNID", "QTDE", "PCUNIT"},
//                    "_id = ?",
//                    new String[] {Long.toString(idEstq)},
//                    null,
//                    null,
//                    null
//            );
//            if (cursor.moveToFirst()) {
//                txt = findViewById(R.id.txtCodigo);
//                txt.setText(cursor.getString(cursor.getColumnIndexOrThrow("CODIGO")));
//                txt = findViewById(R.id.txtNome);
//                txt.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
//                txt = findViewById(R.id.txtUnid);
//                String unid = cursor.getString(cursor.getColumnIndexOrThrow("UNID"));
//                txt.setText(unid);
//                valor = cursor.getInt(cursor.getColumnIndexOrThrow("QTDE"));
//                if (unid.equals("Kg"))
//                    s = "Qtde. Estq: " + String.format(Locale.getDefault(), "%,.3f", valor/1000.0);
//                else
//                    s = "Qtde. Estq: " + String.format(Locale.getDefault(), "%,d", valor);
//                txt = findViewById(R.id.txtQtde);
//                txt.setText(s);
//                valor = cursor.getInt(cursor.getColumnIndexOrThrow("PCUNIT"));
//                txt = findViewById(R.id.txtPcUnit);
//                txt.setText("Valor: R$ " + String.format(Locale.getDefault(), "%,.2f", valor/100.0));
//            }
//            else
//                Toast.makeText(this, "Produto não encontrado", Toast.LENGTH_SHORT).show();
//        }
//        catch (SQLiteException e){
//            Toast.makeText(this, "Falha no acesso ao Banco de Dados", Toast.LENGTH_LONG).show();
//        }
    }
}
