package com.example.v1tcc;

import android.app.TimePickerDialog;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Helpers {

    private Calendar calHoraAlarm;
    private String horaAtual;
    private String minAtual;
    private String horaSelecionadaAlarme = "11:11";
    private TimePickerDialog mTimePicker;


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
}
