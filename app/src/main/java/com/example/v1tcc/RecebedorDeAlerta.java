package com.example.v1tcc;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RecebedorDeAlerta extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            NotificationHelper notificationHelper = new NotificationHelper(context);
//            NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
//            notificationHelper.getManager().notify(1, nb.build());

            Intent intent2 = new Intent(context, AlarmReceiverActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }
    }