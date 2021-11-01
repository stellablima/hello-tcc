package com.example.v1tcc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class SetupAlarm {

    public SetupAlarm(Context context, Bundle extras, int timeoutInSeconds){
        // Get AlarmManager instance
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        // Intent part
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("FOO_ACTION");
        intent.putExtra("KEY_FOO_STRING", "Medium AlarmManager Demo");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        //https://stackoverflow.com/questions/1082437/android-how-to-use-alarmmanager
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Alarm time
        int ALARM_DELAY_IN_SECOND = 10;
        long alarmTimeAtUTC = System.currentTimeMillis() + ALARM_DELAY_IN_SECOND * 1_000L;

        // Set with system Alarm Service
        // Other possible functions: setExact() / setRepeating() / setWindow(), etc
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeAtUTC, pendingIntent);
        }
    }
}


