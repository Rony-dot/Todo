package com.rhrmaincard.todoapp.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.activitys.DeleteActivity;
import com.rhrmaincard.todoapp.activitys.NotificationReceiverActivity;
import com.rhrmaincard.todoapp.activitys.UpdateTaskActivity;
import com.rhrmaincard.todoapp.configs.AppConfig;
import com.rhrmaincard.todoapp.databases.entitys.Task;

import java.io.Serializable;

public class MyAlarm extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Alarm manager in broadcart reveiver", "MyAlarm class extends BroadcastReceiver");
        Toast.makeText(context,"Alarm of your task",Toast.LENGTH_LONG).show();
        int ID =(int) System.currentTimeMillis();

        Bundle args = intent.getBundleExtra("args-obj");
        Task task = (Task) args.getSerializable("task-obj");

//        Log.e("task obj is ",task.getTask());
//        String str = intent.getStringExtra("str");
//        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
//        Task task = (Task) intent.getSerializableExtra("task");
//        Toast.makeText(context,"your task"+task.getTask(),Toast.LENGTH_LONG).show();
        addNotification(context,ID,task);
    }

    private void addNotification(Context context, int NOTIFICATION_ID, Task task) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Intent intent = new Intent(context, UpdateTaskActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("task-obj",(Serializable) task);
//        args.putString("str","STRING");
        intent.putExtra("args-obj",args);
//        intent.putExtra("str","String");
//        intent.putExtra("task-obj",task);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(context,0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);

        /***
         * for deleting call this action
         ***/
        Intent deleteIntent = new Intent(context, DeleteActivity.class);
        Bundle delArgs = new Bundle();
        delArgs.putSerializable("delete-task",(Serializable) task);
        deleteIntent.putExtra("delete-args",delArgs);
        deleteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent deletePendingIntent = PendingIntent.getActivity(context,
                0,deleteIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        /***
         * for showing the task call this action
         * **/
        Intent broadcastIntent = new Intent(context, NotificationReceiverActivity.class);
        Bundle args2 = new Bundle();
        args2.putSerializable("task-obj2",(Serializable) task);
//        broadcastIntent.putExtra("toastMessage", message);
        broadcastIntent.putExtra("args-obj2",args2);
        broadcastIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent actionIntent = PendingIntent.getActivity(context,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_add,
//                "action", actionIntent)
////                .addRemoteInput(remoteInput)
//                .build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AppConfig.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_noti)
                .setContentTitle(task.getTask()+"")
                .setContentText(task.getDesc()+"")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setContentIntent(pIntent)
//                .addAction(R.drawable.ic_noti, "Call", pIntent)
                .addAction(R.drawable.ic_noti, "show", actionIntent)
//                .addAction(action)
                .addAction(R.drawable.ic_noti, "Edit", pIntent)
                .addAction(R.drawable.ic_noti, "Delete", deletePendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
        Log.e("notification ","Created");
    }


}
