package com.rhrmaincard.todoapp.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.rhrmaincard.todoapp.activitys.NotificationReceiverActivity;
import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.activitys.NavMainActivity;
import com.rhrmaincard.todoapp.activitys.UpdateTaskActivity;
import com.rhrmaincard.todoapp.configs.AppConfig;
import com.rhrmaincard.todoapp.services.BackgroundService;

public class CommonTask {

    public static void addNotification(Context context, int NOTIFICATION_ID, String title, String description) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

//        Intent intent = new Intent(context,NotificationReceiverActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pIntent = PendingIntent.getActivity(context,0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AppConfig.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_noti)
                .setContentTitle(title)
                .setContentText(description)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
//                .setContentIntent(pIntent)
//                .addAction(R.drawable.ic_noti, "Call", pIntent)
//                .addAction(R.drawable.ic_noti, "More", pIntent)
//                .addAction(R.drawable.ic_noti, "And more", pIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
        Log.e("notification ","Created");

    }


    public static void printLog(String tag, String msg){
        Log.e(tag,msg);
    }

    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static void startOrStopService(Context context) {

        if(BackgroundService.isRunning){
            Intent intent = new Intent(context,BackgroundService.class);
            context.stopService(intent);
            BackgroundService.backService.doSomeThing();
            Log.e("service stopped from common task","background service stopped");
        }else{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, BackgroundService.class));
            } else {
                context.startService(new Intent(context, BackgroundService.class));
            }
//            Intent intent = new Intent(this,BackgroundService.class);
//            startService(intent);
            Log.e("service started from common tast"," service started");
        }
    }

    public static void addDataToSharedPreferences(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("APP",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDataFromSharedPreferences(Context context, String key){
        SharedPreferences sharedPreferences =context.getSharedPreferences("APP",Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

}
