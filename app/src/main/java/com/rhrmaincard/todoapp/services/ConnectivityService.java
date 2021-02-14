package com.rhrmaincard.todoapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcEvent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.facebook.common.Common;
import com.rhrmaincard.todoapp.utils.CommonTask;

import java.io.IOException;
import java.net.InetAddress;

public class ConnectivityService extends Service {
    public static ConnectivityService conService;
    public static boolean isRunning;
     Handler handler = new Handler();

    public ConnectivityService() {
        super();
    }

    @Override
    public void onCreate() {
        isRunning = true;
        conService = this;
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkConnectivity(); 
        return START_STICKY;
    }

    private void checkConnectivity() {

        handler.postDelayed(connectivityCheck,1000);
    }

    Runnable connectivityCheck = new Runnable() {
        @Override
        public void run() {
            try{
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                if(activeNetwork!=null && activeNetwork.isConnectedOrConnecting()){
                    /**
                     * check for wifi or not clause
                     * */
//                        if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                            // connected to wifi
//                            return true;
//                        } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;

                    CommonTask.printLog("service class","active internet");
                    InetAddress inetAddress = InetAddress.getByName("172.217.194.138");
                    if(!inetAddress.equals("")){
                        sendMessage(Boolean.TRUE);
                        CommonTask.printLog("service class","connected google");
                    }else{
                        CommonTask.printLog("service class","not connected google");
                    }


                    /**
                     * check server connection by address
                     * and a custom function isConnectedToThisServer(..)
                     */
//                        if(isConnectedToThisServer("www.google.com")) {
//                            sendMessage(Boolean.TRUE);
//                            CommonTask.printLog("service class","connected google");
////                            Toast.makeText(this, "Yes, Connected to Google", Toast.LENGTH_SHORT).show();
//                        } else {
//                            sendMessage(Boolean.FALSE);
//                            CommonTask.printLog("service class","not connected google");
////                            Toast.makeText(this, "No Google Connection", Toast.LENGTH_SHORT).show();
//                        }


                }else{
                    sendMessage(Boolean.FALSE);
                    CommonTask.printLog("service class","no Internet");
                }

                CommonTask.printLog("Running","Running continuously");
                handler.postDelayed(connectivityCheck,1000);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    public boolean isConnectedToThisServer(String host) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 "+host);
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void sendMessage(Boolean data) {

        Intent intent = new Intent("Network");
        intent.putExtra("connected",data);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        isRunning =false;
        conService = null;
        super.onDestroy();
    }
}
