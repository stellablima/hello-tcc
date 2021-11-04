package com.example.v1tcc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EditarProcedimentoActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "idprocedimento";

    private EditText edtNomeProcedimento;
    private TextView txtHoraProcedimento;
    private Long idProcedimento;
    private BDRotinaHelper bdRotinaHelper;
    private SQLiteDatabase bd;
    private Cursor cursor;
    private Calendar calHoraAlarm;
    private String horaAtual;
    private String minAtual;
    private String horaSelecionadaAlarme = "11:11";
    private TimePickerDialog mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_procedimento);

        edtNomeProcedimento = findViewById(R.id.edtNomeProcedimento);
        txtHoraProcedimento = findViewById(R.id.txtHoraProcedimento);
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
        try {
            idProcedimento = getIntent().getExtras().getLong(EXTRA_ID);
//            Toast.makeText(this, "idProcedimento:"+idProcedimento, Toast.LENGTH_SHORT).show();
            bdRotinaHelper = new BDRotinaHelper(this);
            bd = bdRotinaHelper.getReadableDatabase();
//            // Podemos criar o cursor com rawQuery()
////            //cursor = bd.rawQuery("select _id, CODIGO, NOME, UNID, QTDE, PCUNIT from ESTOQUE where _id = ?",
////            // new String[] {Long.toString(idEstq)} );
            // ou com query(). O rawQuery foi usado na app anterior (Biblioteca) e aqui usamos query()
            cursor = bd.query("PROCEDIMENTO",
                    new String[] {"_id", "NOME", "DATA_PREVISAO"},
                    "_id = ?",
                    new String[] {Long.toString(idProcedimento)},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                txt = findViewById(R.id.edtNomeProcedimento);
                txt.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOME")));
                txt = findViewById(R.id.txtHoraProcedimento);
                txt.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATA_PREVISAO")));
//                txt = findViewById(R.id.txtUnid);
//                String unid = cursor.getString(cursor.getColumnIndexOrThrow("UNID"));
////                txt.setText(unid);
////                valor = cursor.getInt(cursor.getColumnIndexOrThrow("QTDE"));
////                if (unid.equals("Kg"))
////                    s = "Qtde. Estq: " + String.format(Locale.getDefault(), "%,.3f", valor/1000.0);
////                else
////                    s = "Qtde. Estq: " + String.format(Locale.getDefault(), "%,d", valor);
////                txt = findViewById(R.id.txtQtde);
////                txt.setText(s);
////                valor = cursor.getInt(cursor.getColumnIndexOrThrow("PCUNIT"));
////                txt = findViewById(R.id.txtPcUnit);
////                txt.setText("Valor: R$ " + String.format(Locale.getDefault(), "%,.2f", valor/100.0));
            }
            else
                Toast.makeText(this, "Produto não encontrado", Toast.LENGTH_SHORT).show();
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Falha no acesso ao Banco de Dados", Toast.LENGTH_LONG).show();
        }
    }

    public void btnAtualizarProcedimentoOnClick(View view){
        try {

            preenchimentoValido();
            String[] txtHora =  txtHoraProcedimento.getText().toString().split(":");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(txtHora[0]));  // set hour
            cal.set(Calendar.MINUTE, Integer.parseInt(txtHora[1]));          // set minute
            cal.set(Calendar.SECOND, 0);               // set seconds

            //gambiarra provisoria sobrescrevendo com o mesmo idReq
            AlarmReceiver.startAlarmDef(this, cal, (idProcedimento).intValue());

            ContentValues cv = new ContentValues();
            cv.put("NOME", edtNomeProcedimento.getText().toString());
            cv.put("DATA_PREVISAO", txtHoraProcedimento.getText().toString());
            BDRotinaHelper bdEstoqueHelper = new BDRotinaHelper(this);
            SQLiteDatabase bd = bdEstoqueHelper.getWritableDatabase();
            bd.update("PROCEDIMENTO", cv, "_id = ?", new String[] {Long.toString(idProcedimento)});
            //AlarmReceiver.cancelAlarmDef(this, (idProcedimento).intValue()); //atualizar a hora que vai tocar

            //Toast.makeText(this, "Id inserido:"+idInserted, Toast.LENGTH_SHORT).show();
            finish();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Atualização falhou", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void txtHoraProcedimentoOnClick(View view){
        Toast.makeText(this, "clicado",Toast.LENGTH_LONG).show();

        calHoraAlarm = Calendar.getInstance();
        horaAtual = Integer.toString(calHoraAlarm.get(Calendar.HOUR_OF_DAY));
        minAtual = Integer.toString(calHoraAlarm.get(Calendar.MINUTE));
       /* tmpHoraAlarme = new TimePickerDialog(
                this,
                (TimePicker timePicker, int hourOfDay, int minutes) => {
                    txtHoraProcedimento.setText(String.format("%02d", hourOfDay));
                    txtHoraProcedimento.setText(String.format("%02d", hourOfDay));
                },
                horaAtual, minAtual, true);
        tmpHoraAlarme.show();
        );*/
        //bom exemplo https://stackoverflow.com/questions/17901946/timepicker-dialog-from-clicking-edittext
        //txtHoraProcedimento.setOnClickListener(new OnClickListener() {
        // @Override
        //public void onClick(View v) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                txtHoraProcedimento.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//tem como pegar o padrão corernte no dispositivo?
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
        // }
        //  });

//        //mTimePicker.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//        //    @Override
//        //    public void onClick(View view) {
//                String[] txtHora =  txtHoraProcedimento.getText().toString().split(":");
//                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM); //valor.substring(0,valor.length-2);
//                intent.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(txtHora[0]));//(int) txtHoraProcedimento.getText().toString().indexOf(0,2));//int
//                intent.putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(txtHora[1]));//txtHora.substring(3,5));// (int) txtHoraProcedimento.toString().indexOf(2,4));//int
//                intent.putExtra(AlarmClock.EXTRA_MESSAGE,"Teste");
//                if(intent.resolveActivity(getPackageManager())!=null)
//                    startActivity(intent);
//        //    }
//        //});

    }
    private void preenchimentoValido() throws Exception {
        String s;
        s = edtNomeProcedimento.getText().toString();
        if (s.equals(""))
            throw new Exception("O Nome deve ser preenchido");
        //else if (operacao == OP_INCLUI && jaExiste(s))
        //    throw new Exception("Código já cadastrado");
        //s = edtNome.getText().toString();
        //if (s.equals(""))
        //    throw new Exception("O Nome do Produto deve ser preenchido");
        //s = spnUnidade.getSelectedItem().toString();
        //if (s.equals(""))
        //    throw new Exception("A Unidade deve ser preenchida");
    }

}