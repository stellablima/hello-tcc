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
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

//https://thestreamliners.in/blog/implement-alarm-manager/
/*
BroadcastReceiver é uma classe para receber e manipular a transmissão enviada ao seu aplicativo.
No caso de alarme do sistema, ele recebe uma transmissão do serviço de alarme do sistema quando o alarme dispara.
*/
public class AlarmReceiver extends BroadcastReceiver {

    private static long intervalMillis;
    public static final String EXTRA_ID = "idprocedimento";


    @Override // ao encerrar o app ou reiniciar o celular o que irá acontecer ao abrir de novo? ira mantar os alarmes que estão no banco?
    public void onReceive(Context context, Intent intent) {

        /*
         Minha ideia-> defina o alarme para todos os dias, verifique a condição do seu dia no receptor de alarme.
         */
        Toast.makeText(context, "id"+ (intent.getExtras().getInt(EXTRA_ID)), Toast.LENGTH_LONG).show();
        Intent intent2 = new Intent(context, AlarmReceiverActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra(ProcedimentosActivity.EXTRA_ID, intent.getExtras().getInt(EXTRA_ID)); //get extra dias da semana decide aciona ou nao
        context.startActivity(intent2);
    }

    public static void updateAlarmProcedimento(Context context, Calendar c, int reqcod){

        Toast.makeText(context, "verifique se esse id é do banco e se precisa fazer logica de jogar ultimo digito fora para alterar"+reqcod, Toast.LENGTH_LONG).show();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);                         //FLAG_UPDATE_CURRENT vou ter que passar menos parametro? tipo só o que mudou
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reqcod, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public static void snoozeAlarmProcedimento(long intervalMillis) {
        AlarmReceiver.intervalMillis = intervalMillis;
        //setar outro alame de 5min
        //função correta
    }

    public static void startAlarmProcedimento(Context context, ArrayList<Calendar> c, int reqcod, Boolean swtRepete, Boolean swtFrequencia, String  spnPeriodo, String spnPeriodo0){



        if(swtRepete){
            if(swtFrequencia) {

                /* simples   NAO PREVE DURAÇÃO/TIPO/ALARME FLUTUANTTE DIA TOD0(NOTTIFICAÇÃO) OU ALARME BARULHENTO, se tipo diff fazer uma classe mae geral e ramificar por iffs
         escopo o v2 seria uma logica unica generia que atenderia tod0 o app
        1- repetição
            1- 'nao proporcional', de 5 em 5 horas/7 em 7 meses, 2x ao dia, horarios nao proporcionais, 2x terça e 1 quinta? (ainda nao sei fazer é novo, nao tem em lugar nenhum)
            2- proporcional simples
                1- repetições serao anuais, mensais, semanais, diarias
                2- quantidade
                3- duração eterno ou inicio e fim
                    1-calendario e horario incio e fim
        2- alarme unico (nao faz sentido para procedimento porem essa pode evoluir a ser um func genrica para o app tod0)
            1- agendamento de quando seria

            fluxo complexo (começar com defaults e opções desabilitas, ver google, layout bom porem limitado ao nao proporcional)
            1- nome alarme
            2 ano,mes,dia,hora atual passivel setar (espera-se data futura)
            3 unico salvo ou não, se mudar chave nao, e para procedimento pode deixar um default repetido checado, expandir opções
            4 combo repetição: anual, mensal ,semanal,diariamente,horario
            5 quais dias quais horarios expandir filho
            6 quantas vezes nesses dias/hor/meses especificar por default primeiro horario botão de + para outros
            7 fazer fluxo de procedimentos documentar a fim de achar layput melhor e se sera possivel generico mesmo

        * */

                //opção 1 quando disparar programe o proximo baseado nas variaveis atuais
                //opção 2 repeat
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                //AlarmManager alarmManager = SKAlarmManager.getAlarmManager(context);

                Intent intent = new Intent(context, AlarmReceiver.class);// RecebedorDeAlerta.class); //intent pendente que vai gerar o alerta
                intent.putExtra(AlarmReceiver.EXTRA_ID, Integer.parseInt(reqcod+"0")); //ids multiplos??
        /*
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ALARM_UNIQUE_ID, alarmUniqueId);
        intent.putExtra(ALARM_ID, alarmId);
         */

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(reqcod+"0"), intent, 0);

                if (c.get(0).before(Calendar.getInstance())) {
                    c.get(0).add(Calendar.DATE, 1);
                }

                intervalMillis = getInterval(spnPeriodo, spnPeriodo0);
                alarmManager.setRepeating( // setRepeating pra task deve funcionnar
                        AlarmManager.RTC_WAKEUP,  //https://developer.android.com/reference/android/app/AlarmManager#constants
                        c.get(0).getTimeInMillis(), //SystemClock.elapsedRealtime()
                        intervalMillis, //posso passar 2 param? pra nao proporcional, inexact parece aceitar uns caras pre formatados INTERVAL_HOUR
                        pendingIntent
                );
            }else{
                Toast.makeText(context, "Alarme nao proporcional tentando", Toast.LENGTH_SHORT).show();
                //gravar array de calendar no banco
                //programmar alarmmes e gravar os seus ids
                //a principio considerar todos os dias da semana
                //depois os dias da semama serao pickados
                //adicionar id_n manualmente para achar alarmme no banco apenas dar um split em _
                /*
                ALARME QUANDO SEMANAL PICKER IMPLEMENTADO
                em vez de um array de semana, cada horario tera seu proprio picker de dia

                nao faço ideia como adicionar os dias da semana...
                talvez ultimoms 7 zeros poderiam ser dias da semana ou um codigo menor comm dois digitos
                e uma função pra converter todas as possibilidades ou ainda
                mais um extra do alarme, onde o primmeiro digito seria o array e o segundo os valores

                DIAS_SEMANA = 0_STQQQSSD
                DIAS_SEMANA = 1_0000000

                porem seria conveninte ter essa if no banco
                entao criar realmente novo campo no banco aqui ja pensando em limitar o escopo dessa realease
                para fixo dias emm alarmmmes nao proporcionais pois esse codigo teria que abrir mmamis ainda
                o capo configurador

                DIAS_CONFIGURADOR

                ANO_MES_DIA_SEMANARIO
                ?_JANFEVMARETC_123..31_STQQSSD


                novo reqcod para id unico
                e nova variavel no banco para recuperar e configurar facil

                var do banco e possivel extra:
                DIAS_SEMANA = 0_STQQQSSD,1_S00000D

                novo id unico do alarme no sistema:
                reqcod = 9900 id_primmeirohorario_domingo
                reqcod = 9916 id_segundohorario_sabado

                */

                int index = 0;
                for (Calendar calendarHorario: c) {

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(context, AlarmReceiver.class);
                    intent.putExtra(AlarmReceiver.EXTRA_ID, Integer.parseInt(reqcod+""+index)); //index _0..n
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(reqcod+""+index), intent, 0);

                    Toast.makeText(context, "reqcod:"+reqcod+"_EXTRA_ID:"+(reqcod+""+index), Toast.LENGTH_SHORT).show();
                    /*
                    problemma comm index aqui
                    sujestão deixar negativo para sinalizar
                    adicionar 0000 para sinalizar
                    tragico demais isso
                    usar flags?? pesquisa isso pfv
                    seila meee

                    tentativa de implementação de logica
                    caso negativo os 2 ultimos ids é referente a repetição

                    ex
                    BANCO 05
                    INTENT -0500
                    PEDENT INTENT -0500

                    BANCO 05
                    INTENT -0501
                    PEDENT INTENT -0501

                    necessario conversao para acessar ao banco
                    positvar e dar split dos dois ultimos digitos
                    decidir por uma liimtação de até 9 repetições por alarme por agora, ja que é v1 e nao é ideal

                    funfou agora ajuste para ele respeitar a logica de dar split nos ultimmos digitos para recuperar o id e preencher dados de layput de banco na tela


                    nova logica, sempre concatenar 0 a direita e subir caso repetição

                    proximo passo: como gravar essa informação no banco
                    */


                    //testando como vai sobrescrever o outro na vdd por ter os mesmo id
                    //alem de sobrescrever ele nao conseguiu recuperar o id e retornou 0 default

                    if (calendarHorario.before(Calendar.getInstance())) {
                        calendarHorario.add(Calendar.DATE, 1);
                    }

                    intervalMillis = getInterval(spnPeriodo, spnPeriodo0);
                    alarmManager.setRepeating( // setRepeating pra task deve funcionnar
                            AlarmManager.RTC_WAKEUP,  //https://developer.android.com/reference/android/app/AlarmManager#constants
                            calendarHorario.getTimeInMillis(), //SystemClock.elapsedRealtime()
                            intervalMillis, //posso passar 2 param? pra nao proporcional, inexact parece aceitar uns caras pre formatados INTERVAL_HOUR
                            pendingIntent
                    );
                    index++;
                }


            }

        }else{
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(AlarmReceiver.EXTRA_ID, Integer.parseInt(reqcod+"0"));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(reqcod+"0"), intent, 0);

            if (c.get(0).before(Calendar.getInstance())) {
                c.get(0).add(Calendar.DATE, 1);
            }

            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(c.get(0).getTimeInMillis(),
                    null);
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
        }
    }

    private static long getInterval(String spnPeriodo, String spnPeriodo1){
        //24*(1dia) 60*(1hora) 60*(1min) 1000*(1sec)

        //simples sem consderar o array de  nao proporcional, pegar numero D0
        if(spnPeriodo.contains("MINUTO")){
            Log.v("VERBOSE","MINUTO" + ((1000*60)*Integer.parseInt(spnPeriodo1))); //sec nao consigo fazer tocar exato problema para o futuro
            return (long) ((1000*60)*Integer.parseInt(spnPeriodo1)); //6 (1000*10); //60

        }else if(spnPeriodo.contains("HORA")){
            Log.v("VERBOSE","HORA");
            return (long) ((60*60*1000)*Integer.parseInt(spnPeriodo1));

        }else if(spnPeriodo.contains("DIA S/N")){
            Log.v("VERBOSE","DIA S/N");
            return (long) ((48*60*60*1000)*Integer.parseInt(spnPeriodo1));

        }else if(spnPeriodo.contains("DIA")){
            Log.v("VERBOSE","DIA");
            return (long) ((24*60*60*1000)*Integer.parseInt(spnPeriodo1));

        }else if(spnPeriodo.contains("SEMANA")){
            Log.v("VERBOSE","SEMANA");
            return (long) ((7*24*60*60*1000)*Integer.parseInt(spnPeriodo1));

        }else
            Log.v("VERBOSE","ELSE");
            return (long) ((24*60*60*1000)*Integer.parseInt(spnPeriodo1));
    }

    public static void cancelAlarmDef(Context context, int reqcod, int nAlarmes) {

        for (int i = 0; i < nAlarmes; i++) {
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(reqcod+""+i), intent, 0);
            Toast.makeText(context, "Alarme cancelado, id: "+reqcod+""+i, Toast.LENGTH_SHORT).show();
            alarmManager.cancel(pendingIntent);
        }
    }
}

/*
bom exemplo completinho https://learntodroid.com/how-to-create-a-simple-alarm-clock-app-in-android/
https://github.com/Cornholio2108/BestAlarm/issues/24
https://askandroidquestions.com/2021/02/06/android-alarm-snooze-from-notification/
setAndAllowWhileIdle() ou setExactAndAllowWhileIdle()//soneca

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
/*
alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendenteIntent)
alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5*1000, pendantIntent)
alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.get(0).getTimeInMillis(), pendingIntent)
alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
*/