package com.rhrmaincard.todoapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class TaskService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendData();
        return START_STICKY;
    }

    private void sendData() {
        Handler handler = new Handler();
        handler.postAtTime(new Runnable() {
            @Override
            public void run() {

            }
        },1*1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
