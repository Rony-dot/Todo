package com.rhrmaincard.todoapp.activitys;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import com.google.android.material.navigation.NavigationView;
import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.configs.AppConfig;
import com.rhrmaincard.todoapp.services.BackgroundService;
import com.rhrmaincard.todoapp.services.ConnectivityService;
import com.rhrmaincard.todoapp.services.ImageDownloaderService;
import com.rhrmaincard.todoapp.utils.CommonTask;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.Locale;

public class NavMainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String CHANNEL_ID = "100";
    private static final int PERMISSION_CODE = 999;
    private static final int PERMISSION_CODE_AUDIO = 998;
    private static final int PERMISSION_CODE_MAP = 997;

    private static boolean isServiceConnected = false;
    public ImageDownloaderService imaageDownloadService;

    private AppBarConfiguration mAppBarConfiguration;
    private boolean isConnected;

    EditText tts;

    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkPermission();


//        ttsBtn = findViewById(R.id.tts_Btn);


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.allTaasksFragment,
                R.id.userProfile,
                R.id.googleMapFragment,R.id.settingFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        createNotificationChannel();

        startService();
//        startOrStopService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tts = findViewById(R.id.et_tts);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null){
                    String text = matches.get(0);
                    tts.setText(text);
                    Intent intent = new Intent(NavMainActivity.this,AddTaskActivity.class);
                    intent.putExtra("task-name",text);
                    startActivity(intent);
                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        findViewById(R.id.tts_Btn).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //when the user removed the finger
                        mSpeechRecognizer.stopListening();
                        tts.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        //finger is on the button
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        tts.setText("");
                        tts.setHint("Listening...");
                        break;
                }
                return false;
            }
        });

        Intent intent = new Intent(this, ImageDownloaderService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ImageDownloaderService.LocalBindService localService = (ImageDownloaderService.LocalBindService) service;
            imaageDownloadService = localService.getInstance();

            isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceConnected = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        notificationShown = false;
        IntentFilter filter = new IntentFilter("Network");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(networkReceiver,filter);
//        LocalBroadcastManager.getInstance(this).registerReceiver(networkReceiver,new IntentFilter("Network"));



    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkReceiver);
        unregisterReceiver(networkReceiver);
    }

    private void stopService() {

        if(ConnectivityService.isRunning){
            Intent intent = new Intent(this,ConnectivityService.class);
            stopService(intent);
//            ConnectivityService.backService.doSomeThing();
            Log.e("service stopped from navmain","network service stopped");
        }
    }

    private void startService(){
//        else{

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Log.e("service started from navmain if"," network service started");
//                startForegroundService(new Intent(this, ConnectivityService.class));
//            } else {
            startService(new Intent(this, ConnectivityService.class));
                Log.e("service started from navmain else"," network service started");
//            }
//            Intent intent = new Intent(this,BackgroundService.class);
//            startService(intent);

//        }
    }
    boolean notificationShown= false;
    BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             isConnected = intent.getBooleanExtra("connected", false);
             if(isConnected){
//                 CommonTask.printLog("network","conection established");

             }
             else{
//                 CommonTask.printLog("network","conection llost");
                 if(!notificationShown){
                     CommonTask.showToast(getApplicationContext(),"Connection lost");
                     int ID = (int) System.currentTimeMillis();
                     CommonTask.addNotification(getApplicationContext(),ID,"No Internet","Please check your Internet");
                     notificationShown = true;
                 }

             }

        }
    };

    private void checkPermission() {
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)!=
                PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_WIFI_STATE)!=
                PackageManager.PERMISSION_GRANTED){

        requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE},
                PERMISSION_CODE);

        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_CODE_AUDIO);

        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_CODE_MAP);

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    CommonTask.showToast(this,"Permission Granted");
                }else{
                    checkPermission();
                }
                break;
            case PERMISSION_CODE_AUDIO:
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        startOrStopService();
        unbindService(connection);
    }

    @Override
    public void onClick(View v) {

    }




}// end class