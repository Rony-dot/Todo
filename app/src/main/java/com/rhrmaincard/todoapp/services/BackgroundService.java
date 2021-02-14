package com.rhrmaincard.todoapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.activitys.NavMainActivity;
import com.rhrmaincard.todoapp.activitys.NotificationReceiverActivity;
import com.rhrmaincard.todoapp.configs.AppConfig;

public class BackgroundService extends Service {

    public static boolean isRunning = false;
    public static BackgroundService backService = null;
    private final int NOTIFICATION_ID = 1;
    private NotificationManagerCompat notificationManager = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public BackgroundService() {
        super();
    }

    @Override
    public void onCreate() {
        isRunning = true;
        backService = this;
//        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager = NotificationManagerCompat.from(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent i = new Intent(this, NavMainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this,0, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AppConfig.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_noti)
                .setContentTitle("ToDo App")
                .setContentText("ToDo app is ongoing....")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setContentIntent(pIntent)
                .setOngoing(true);
        startForeground(NOTIFICATION_ID,builder.build());
        Log.e("notification of background service ","Created");

        return START_STICKY;
    }

    public void doSomeThing(){
        Toast.makeText(this,"doing in background",Toast.LENGTH_LONG).show();
        Log.e("background Service","Running in the backgrouond");

    }

    @Override
    public void onDestroy() {
        isRunning = false;
        backService = null;
        notificationManager.cancel(NOTIFICATION_ID);

        super.onDestroy();
    }
}
