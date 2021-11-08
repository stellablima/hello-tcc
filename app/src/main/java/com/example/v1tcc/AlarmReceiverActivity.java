package com.example.v1tcc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

public class AlarmReceiverActivity extends AppCompatActivity {

    private MediaPlayer mp;
    public static final String EXTRA_ID = "idprocedimento";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_receiver);

        mp=MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mp.start();

        //Toast.makeText(this, "id"+ (getIntent().getExtras().getLong(EXTRA_ID)), Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }

    public void btnFecharAlarmeOnClick(View view){
        finish();
    }
}