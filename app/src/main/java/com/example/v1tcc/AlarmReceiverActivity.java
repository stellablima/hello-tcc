package com.example.v1tcc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class AlarmReceiverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_receiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void AlarmManagerListener(Context context, Bundle extras, int timeoutInSeconds){

        AlarmManager alarmManager =  (AlarmManager)AlarmReceiverActivity.this.getSystemService(Context.ALARM_SERVICE);
        long alarmTime = System.currentTimeMillis() + 4000;
        String tagStr = "TAG";
        Handler handler = null; // `null` means the callback will be run at the Main thread
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                alarmTime,
                tagStr,
                new AlarmManager.OnAlarmListener() {
                    @Override
                    public void onAlarm() {
                        Toast.makeText(AlarmReceiverActivity.this, "AlarmManager.OnAlarmListener", Toast.LENGTH_SHORT).show();
                    }
                }, null);
    }
}