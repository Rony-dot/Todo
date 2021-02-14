package com.rhrmaincard.todoapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.utils.CommonTask;

import java.util.Calendar;


public class NotificationFragment extends Fragment {


    private static final int NOTIFICATION_ID = 999;
    Button notiBtn;
    public NotificationFragment() {
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
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notiBtn = view.findViewById(R.id.noti_btn);
        notiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (int) System.currentTimeMillis();
                CommonTask.addNotification(v.getContext().getApplicationContext(),ID,"Demo","A An The Demo");
            }
        });
    }





}