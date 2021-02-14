package com.rhrmaincard.todoapp.configs;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.rhrmaincard.todoapp.databases.AppDatabase;
import com.rhrmaincard.todoapp.services.BackgroundService;
import com.rhrmaincard.todoapp.utils.CommonConstant;
import com.rhrmaincard.todoapp.utils.CommonTask;

public class AppConfig extends Application {

    private static AppConfig appConfig;

    public AppDatabase database;
    public static final String CHANNEL_1_ID = "channel1";
    public static boolean isRunning;

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(this, AppDatabase.class, CommonConstant.DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        createNotificationChannels();
        Toast.makeText(this,"Your app runs now",Toast.LENGTH_LONG).show();

//        startOrStopService();

        SharedPreferences sharedPreferences = getSharedPreferences("APP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!sharedPreferences.contains("isBackgroundServiceRunning")){
            CommonTask.addDataToSharedPreferences(this,"isBackgroundServiceRunning","true");
            startOrStopService();
        }
    }

    public void startOrStopService() {

        if(BackgroundService.isRunning){
            Intent intent = new Intent(this,BackgroundService.class);
            stopService(intent);
            BackgroundService.backService.doSomeThing();
            Log.e("service stopped from appconfig","background service stopped");
        }else{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, BackgroundService.class));
            } else {
                startService(new Intent(this, BackgroundService.class));
            }
//            Intent intent = new Intent(this,BackgroundService.class);
//            startService(intent);
            Log.e("service started from appconfig"," service started");
        }
    }


    public AppDatabase getDatabase(){
        if(database != null)
            return database;
        else
            return Room.databaseBuilder(this,AppDatabase.class, CommonConstant.DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
    }

    public static synchronized AppConfig getInstance(){
        if(appConfig == null){
            appConfig = new AppConfig();
        }
        return appConfig;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is Channel 1");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            Log.e("notification channel","Created");
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e("TERMINATED","From appconfig class");
        Toast.makeText(this,"App terminated",Toast.LENGTH_LONG).show();
    }



}
