package com.rhrmaincard.todoapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Random;

public class ImageDownloaderService extends Service {

    public IBinder binder  = new LocalBindService();
    Random  random = new Random();

    public ImageDownloaderService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public int getRandom(){
        return random.nextInt(100);
    }

    public class LocalBindService extends Binder {
        public ImageDownloaderService getInstance(){
            return ImageDownloaderService.this;
        }
    }

}
