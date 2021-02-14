package com.rhrmaincard.todoapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.services.BackgroundService;
import com.rhrmaincard.todoapp.utils.CommonTask;

public class SettingFragment extends Fragment implements View.OnClickListener {

    Button tipsAndTricksBtn, rateUsBtn, profileViewBtn, removeAdsBtn;
    CheckBox isBackgroundRunning;

    public SettingFragment() {
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
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isBackgroundRunning = view.findViewById(R.id.isBackgroundRunning);
        isBackgroundRunning.setChecked(BackgroundService.isRunning);

        tipsAndTricksBtn= view.findViewById(R.id.tipsAndTricksBtn);
        profileViewBtn= view.findViewById(R.id.profileViewBtn);
        removeAdsBtn= view.findViewById(R.id.removeAdsBtn);
        rateUsBtn= view.findViewById(R.id.rateUsBtn);

        tipsAndTricksBtn.setOnClickListener(this);
        profileViewBtn.setOnClickListener(this);
        removeAdsBtn.setOnClickListener(this);
        rateUsBtn.setOnClickListener(this);
        isBackgroundRunning.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tipsAndTricksBtn:

                break;
            case R.id.profileViewBtn:
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_settingFragment_to_userProfile);
                break;
            case R.id.rateUsBtn:

                break;
            case R.id.removeAdsBtn:

                break;
            case R.id.isBackgroundRunning:
                if (BackgroundService.isRunning) {
                    isBackgroundRunning.setChecked(!BackgroundService.isRunning);
                    CommonTask.addDataToSharedPreferences(getContext(), "isBackgroundServiceRunning", "false");
                    CommonTask.startOrStopService(getContext());
                } else {
                    isBackgroundRunning.setChecked(!BackgroundService.isRunning);
                    CommonTask.startOrStopService(getContext());
                    CommonTask.addDataToSharedPreferences(getContext(), "isBackgroundServiceRunning", "true");
                }
                break;
        }
    }
}