package com.example.v1tcc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

//https://thestreamliners.in/blog/implement-alarm-manager/
/*
BroadcastReceiveré uma classe para receber e manipular a transmissão enviada ao seu aplicativo.
No caso de alarme do sistema, ele recebe uma transmissão do serviço de alarme do sistema quando o alarme dispara.
*/
public class AlarmReceiver extends BroadcastReceiver {

    private static long intervalMillis;
    public static final String EXTRA_ID = "idprocedimento";


    @Override // ao encerrar o app ou reiniciar o celular o que irá acontecer ao abrir de novo? ira mantar os alarmes que estão no banco?
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "id"+ (intent.getExtras().getInt(EXTRA_ID)), Toast.LENGTH_LONG).show();

        Intent intent2 = new Intent(context, AlarmReceiverActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra(ProcedimentosActivity.EXTRA_ID, intent.getExtras().getInt(EXTRA_ID));
        context.startActivity(intent2);
    }

    public static void updateAlarmProcedimento(Context context, Calendar c, int reqcod){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);                         //FLAG_UPDATE_CURRENT vou ter que passar menos parametro? tipo só o que mudou
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reqcod, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public static void startAlarmProcedimento(Context context, ArrayList<Calendar> c, int reqcod, String spnRepeticao){
        //opção 1 quando disparar programe o proximo baseado nas variaveis atuais
        //opção 2 repeat
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);// RecebedorDeAlerta.class); //intent pendente que vai gerar o alerta
        intent.putExtra(AlarmReceiver.EXTRA_ID, reqcod);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reqcod, intent, 0);

        if (c.get(0).before(Calendar.getInstance())) {
            c.get(0).add(Calendar.DATE, 1);
        }

        intervalMillis = getInterval(spnRepeticao);

        //aqui só permite proporcional //proporcional so tem 1 cal tbm
        alarmManager.setRepeating( //setInexactRepeating pra task deve funcionnar
                AlarmManager.RTC_WAKEUP,
                c.get(0).getTimeInMillis(),
                intervalMillis, //posso passar 2 param? pra nao proporcional
                pendingIntent
        );

        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private static long getInterval(String spnRepeticao){

        //24*(hora) 60*(min) 1000*(sec)
        //if(spnRepeticao == "Diário") //implementar /um dia sim um dia nao/ no spinner
        return (long) ((24*60*1000)/Integer.parseInt(spnRepeticao));
    }

    public static void cancelAlarmDef(Context context, int reqcod) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reqcod, intent, 0);
        Toast.makeText(context, "Alarme cancelado", Toast.LENGTH_SHORT).show();
        alarmManager.cancel(pendingIntent);
    }
}

/*
next alarm
https://www.programcreek.com/java-api-examples/?class=android.app.AlarmManager&method=getNextAlarmClock

if(pendingIntent) //ele retorna null se o alarme nao existir, nao pode ser criado com FLAG_ONE_SHOT tem que ser default
pendingIntent = PendingIntent.getBroadcast(context,0, myIntent, PendingIntent.FLAG_NO_CREATE);

FLAG_CANCEL_CURRENT // e resetar recebendo tudo de novo
filterEquals(Intent))

    private static long getNextAlarm(Context context, int reqcod) {
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager.AlarmClockInfo info = alarms.getNextAlarmClock(reqcod);
        return info != null ? info.getTriggerTime() : 0;
    }
* */


    //private static final String ALARM_RECEIVER_ACTION = "";
    //private static int requestCode;
    //private static final int requestCode; // tentar receber como aquele extra das aulas

//    public AlarmReceiver() {}
//    public AlarmReceiver(int idRequestCode) {
//        requestCode = idRequestCode;
//    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
    //reset all alarms?
//        if (intent.getAction() == "FOO_ACTION") {
//            String fooString = intent.getStringExtra("KEY_FOO_STRING");
//            Toast.makeText(context, fooString, Toast.LENGTH_LONG).show();
//        }
//
//        /*Sempre que seu dispositivo for reiniciado, o BroadcastReceiveré chamado e com a ajuda de Intent
//        podemos registrar novamente todos os alarmes usando a ação ACTION_BOOT_COMPLETED.*/
//        if ((Intent.ACTION_BOOT_COMPLETED).equals(intent.getAction())){
//            // reset all alarms
//        }
//        else{
//            // perform your scheduled task here (eg. send alarm notification)
//        }

    //}

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static void setupAlarm(Context context, Bundle extras, Calendar dateCal, long dateMilis, int id){
//        // Get AlarmManager instance
//        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//
//        // Intent part
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        //intent.setAction("FOO_ACTION");
//        //intent.putExtra("KEY_FOO_STRING", "Medium AlarmManager Demo");
//        //intent.putExtra(AlarmClock.EXTRA_MESSAGE, "jeito2");
//
////        Calendar cal = Calendar.getInstance();
////        cal.set(Calendar.HOUR_OF_DAY, 18);  // set hour
////        cal.set(Calendar.MINUTE, 46);          // set minute
////        cal.set(Calendar.SECOND, 0);               // set seconds
//        //cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//        //cal.getTimeInMillis()
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);//PendingIntent.FLAG_UPDATE_CURRENT); // ultimo parametro?
//        //https://stackoverflow.com/questions/1082437/android-how-to-use-alarmmanager
//        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        //long interval = 7 * 24 * 60 * 60 * 1000;   // interval of 7 days // posso usar para definir um enterva-lo de 5h em 5h para sondagem?
//        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), interval , pendingIntent);
//        //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
//
//
//        String tag = "TAG2"; // colocar o id do banco
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateMilis, tag, new AlarmManager.OnAlarmListener() {
//                @Override
//                public void onAlarm() {
//                    Toast.makeText(context, "uio uio uio uio", Toast.LENGTH_SHORT).show();
////                    Intent intent2 = new Intent(context, AlarmReceiverActivity.class);
////                    context.startActivity(intent2);
//                }
//            }, null);
//        }
//
//        // Alarm time
//        //int ALARM_DELAY_IN_SECOND = 10;
//        //long alarmTimeAtUTC = System.currentTimeMillis() + ALARM_DELAY_IN_SECOND * 1_000L;
//
//        // Set with system Alarm Service
//        // Other possible functions: setExact() / setRepeating() / setWindow(), etc
//        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        //    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeAtUTC, pendingIntent);
//        //}
//        /*
//        val timeInterval = 5 * 1_000L
//        val alarmTime = System.currentTimeMillis()
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, timeInterval , fooPendingIntent)
//        */
//
//        /*
//        * // Real Time Clock (Real time since 1st January, 1970)
//        val alarmTimeAtUTC = System.currentTimeMillis() + 1 * 1_000L
//        alarmManager.setExact(AlarmManager.RTC, alarmTimeAtUTC, pendingIntent)
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeAtUTC, pendingIntent)
//
//        // Elapsed Real Time (since device is booted)
//        val alarmTimeAfterDeviceIsBooted = 1 * 1_000L
//        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, alarmTimeAfterDeviceIsBooted, pendingIntent)
//        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmTimeAfterDeviceIsBooted, pendingIntent)
//        * */
//    }
//
//    public static void startarAlarme(Context context, Calendar c) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
//
//        if (c.before(Calendar.getInstance())) {
//            c.add(Calendar.DATE, 1);
//        }
//
//        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//        String tag = "TAG2"; // colocar o id do banco
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), tag, new AlarmManager.OnAlarmListener() {
//                @Override
//                public void onAlarm() {
//                    Intent intent2 = new Intent(context, AlarmReceiverActivity.class);
//                    context.startActivity(intent2);
//                }
//            }, null);
//        }
//    }
//
//    public Intent getAlarmIntent(Context context) {
//        Intent intent =  new Intent(context, AlarmReceiver.class);
//        intent.setAction(ALARM_RECEIVER_ACTION);
//        return intent;
//    }
//
//    public static void cancelAlarm(Context context, int id) {
//        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
////
////        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
////                requestCode, getAlarmIntent(context), 0);
////        alarmManager.cancel(pendingIntent);
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
//                id , intent, 0);// PendingIntent.FLAG_UPDATE_CURRENT); //o que seria o ultimo id
//        try {
//            alarmManager.cancel(pendingIntent);
//            Toast.makeText(context, "Alarme apagado", Toast.LENGTH_SHORT).show();
//
//        }catch (Exception e){
//            Toast.makeText(context, "Falha em apagar alarme", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public static void cancelarAlarme(Context context, int requestCode) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
//
//        Toast.makeText(context, "Alarme cancelado", Toast.LENGTH_SHORT).show();
//        alarmManager.cancel(pendingIntent);
//
//    }

//    final PendingIntent pIntent(Intent alarmIntent) {
//        // Create a PendingIntent to be triggered when the alarm goes off
//        return PendingIntent.getBroadcast(getApplicationContext(), AlarmReceiver.REQUEST_CODE,
//                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }
//   https://stackoverflow.com/questions/31766010/stop-alarmmanager-from-other-activity
//    public void cancelAlarm2(Intent alarmIntent, Context context) {
//        try {
//
//            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            /** use flag cancel here */
//            PendingIntent pIntent = PendingIntent.getService(context, AlarmReceiver.REQUEST_CODE, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//            /** cancel alarm */
//            alarm.cancel(pIntent);
//
//        } catch (Exception e) {
//            // handle exception here
//        }
//    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void cancelListener(Context context) {
//
////        AlarmManager alarmManager =  (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
////
////        AlarmManager onAlarmListener = new AlarmManager.OnAlarmListener() {
////            @Override
////            public void onAlarm() {
////                  Toast.makeText(context, "AlarmManager.OnAlarmListener", Toast.LENGTH_SHORT).show();
////            }
////        }
////        alarmManager.cancel(onAlarmListener);
//
////        val onAlarmListener = object : AlarmManager.OnAlarmListener {
////            override fun onAlarm() {
////                toast("Alarm goes off.")
////            }
////        }
////        alarmManager.cancel(onAlarmListener)
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void AlarmManagerListener(Context context, Bundle extras, int timeoutInSeconds){
//
//        AlarmManager alarmManager =  (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        long alarmTime = System.currentTimeMillis() + 4000;
//        String tagStr = "TAG";
//        Handler handler = null; // `null` means the callback will be run at the Main thread
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
//            alarmTime,
//            tagStr,
//            new AlarmManager.OnAlarmListener() {
//                @Override
//                public void onAlarm() {
//                    Toast.makeText(context, "AlarmManager.OnAlarmListener", Toast.LENGTH_SHORT).show();
//                }
//            }, null);
//    }


    //            NotificationHelper notificationHelper = new NotificationHelper(context);
//            NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
//            notificationHelper.getManager().notify(1, nb.build());
//

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void AlarmManagerListener(Context context, Bundle extras, int timeoutInSeconds){
//
//        AlarmManager alarmManager =  (AlarmManager)AlarmReceiverActivity.this.getSystemService(Context.ALARM_SERVICE);
//        long alarmTime = System.currentTimeMillis() + 4000;
//        String tagStr = "TAG";
//        Handler handler = null; // `null` means the callback will be run at the Main thread
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
//                alarmTime,
//                tagStr,
//                new AlarmManager.OnAlarmListener() {
//                    @Override
//                    public void onAlarm() {
//                        Toast.makeText(AlarmReceiverActivity.this, "AlarmManager.OnAlarmListener", Toast.LENGTH_SHORT).show();
//                    }
//                }, null);
//    }
