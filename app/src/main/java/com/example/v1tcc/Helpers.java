package com.example.v1tcc;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Helpers {

//    private static TimePickerDialog mTimePicker;

    public static void spinnerNumero(Context context, int array, Spinner spinner){

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) context);
    }

    public static void lvDinamico(Context context, String[] itens, ListView lv){

        ArrayAdapter<String> adapter = new ArrayAdapter<String> ( context,
                android.R.layout.simple_list_item_1, itens );

        lv.setAdapter(null); // adapter tem que resetar mesmo
        lv.setAdapter(adapter);
        //lv.setOnItemClickListener(null); evento ele sobrescreve
        lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Helpers.lvHoraConfig(context, itens, lv,arg2);
            }
        } );
    }

    protected static void lvHoraConfig(Context context, String[] itens, ListView lv, int position){

        String[] txtHora =  itens[position].toString().split(":");
        int hour = Integer.parseInt(txtHora[0]);
        int minute = Integer.parseInt(txtHora[1]);

        TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                itens[position]=( String.format("%02d",selectedHour) + ":" + String.format("%02d",selectedMinute));
                //Toast.makeText( context, ":"+  itens[position]+"_"+itens.toString(), Toast.LENGTH_SHORT ).show();
                lvDinamico( context, itens, lv);
            }
        }, hour, minute, true);//tem como pegar o padrão corernte no dispositivo?

        mTimePicker.show();
    }

    public static void preenchimentoValido(EditText edtNomeProcedimento) throws Exception {
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

    public static void txtHoraConfig(Context context, TextView txtHoraProcedimento, boolean flagAtualizaCampo){

        if(flagAtualizaCampo){
            Calendar calHoraAlarm = Calendar.getInstance();
            String horaAtual = String.format("%02d", calHoraAlarm.get(Calendar.HOUR_OF_DAY));
            String minAtual = String.format("%02d", calHoraAlarm.get(Calendar.MINUTE));
            txtHoraProcedimento.setText(horaAtual+":"+minAtual);
        }

        txtHoraProcedimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] txtHora =  txtHoraProcedimento.getText().toString().split(":");
                int hour = Integer.parseInt(txtHora[0]);
                int minute = Integer.parseInt(txtHora[1]);

                TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txtHoraProcedimento.setText( String.format("%02d",selectedHour) + ":" + String.format("%02d",selectedMinute));
                    }
                }, hour, minute, true);//tem como pegar o padrão corernte no dispositivo?
                mTimePicker.show();
            }
        });
    }
}
    /*getInt()  Integer.parseInt() .intValue()*/
//    public void txtHoraProcedimentoOnClick(View view){
//        Toast.makeText(this, "clicado",Toast.LENGTH_LONG).show();
//
//        calHoraAlarm = Calendar.getInstance();
//        horaAtual = Integer.toString(calHoraAlarm.get(Calendar.HOUR_OF_DAY));
//        minAtual = Integer.toString(calHoraAlarm.get(Calendar.MINUTE));
//       /* tmpHoraAlarme = new TimePickerDialog(
//                this,
//                (TimePicker timePicker, int hourOfDay, int minutes) => {
//                    txtHoraProcedimento.setText(String.format("%02d", hourOfDay));
//                    txtHoraProcedimento.setText(String.format("%02d", hourOfDay));
//                },
//                horaAtual, minAtual, true);
//        tmpHoraAlarme.show();
//        );*/
//        //bom exemplo https://stackoverflow.com/questions/17901946/timepicker-dialog-from-clicking-edittext
//        //txtHoraProcedimento.setOnClickListener(new OnClickListener() {
//        // @Override
//        //public void onClick(View v) {
//        Calendar mcurrentTime = Calendar.getInstance();
//        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = mcurrentTime.get(Calendar.MINUTE);
//
//        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                txtHoraProcedimento.setText( selectedHour + ":" + selectedMinute);
//            }
//        }, hour, minute, true);//tem como pegar o padrão corernte no dispositivo?
//        mTimePicker.setTitle("Select Time");
//        mTimePicker.show();
//        // }
//        //  });
//
////        //mTimePicker.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
////        //    @Override
////        //    public void onClick(View view) {
////                String[] txtHora =  txtHoraProcedimento.getText().toString().split(":");
////                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM); //valor.substring(0,valor.length-2);
////                intent.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(txtHora[0]));//(int) txtHoraProcedimento.getText().toString().indexOf(0,2));//int
////                intent.putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(txtHora[1]));//txtHora.substring(3,5));// (int) txtHoraProcedimento.toString().indexOf(2,4));//int
////                intent.putExtra(AlarmClock.EXTRA_MESSAGE,"Teste");
////                if(intent.resolveActivity(getPackageManager())!=null)
////                    startActivity(intent);
////        //    }
////        //});
//
//    }

    //mTimePicker.setTitle("Select Time");
    //mTimePicker.updateTime(0,0);

