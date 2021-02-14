package com.rhrmaincard.todoapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.configs.AppConfig;
import com.rhrmaincard.todoapp.services.BackgroundService;
import com.rhrmaincard.todoapp.utils.CommonTask;


public class TestFragment extends Fragment implements View.OnClickListener {

    Button startOrStopServiceBtn;
    ImageView fbProPic;
    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startOrStopServiceBtn = view.findViewById(R.id.startOrStopService);
        startOrStopServiceBtn.setOnClickListener(this);
        fbProPic = view.findViewById(R.id.fb_pro_pic);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.startOrStopService){
            startOrStopService(v.getContext());
        }
    }

    private void startOrStopService(Context context) {
       CommonTask.startOrStopService(context);

//        if(BackgroundService.isRunning){
//            Intent intent = new Intent(getActivity(),BackgroundService.class);
//            getActivity().stopService(intent);
//            BackgroundService.backService.doSomeThing();
//            Log.e("service stopped from appconfig","background service stopped");
//        }else{
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                getActivity().startForegroundService(new Intent(getActivity(), BackgroundService.class));
//            } else {
//                getActivity().startService(new Intent(getActivity(), BackgroundService.class));
//            }
////            Intent intent = new Intent(this,BackgroundService.class);
////            startService(intent);
//            Log.e("service started from appconfig"," service started");
//        }
    }
}