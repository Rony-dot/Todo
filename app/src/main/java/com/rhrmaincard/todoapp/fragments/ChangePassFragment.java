package com.rhrmaincard.todoapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.presenter.BasePresenter;
import com.rhrmaincard.todoapp.presenter.UpdatePresenter;
import com.rhrmaincard.todoapp.presenterImpl.UpdatePresenterImpl;

public class ChangePassFragment extends Fragment implements BasePresenter.View, View.OnClickListener {

    EditText passwordNew, passwordOld;
    Button updatePass;
    UpdatePresenter.Model updatePresenter;

    String passNew, passOld;

    public ChangePassFragment() {
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
        return inflater.inflate(R.layout.fragment_change_pass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updatePresenter = new UpdatePresenterImpl(this,requireContext());

        passwordNew = view.findViewById(R.id.edit_pass_new);
        passwordOld = view.findViewById(R.id.edit_pass_old);
        updatePass = view.findViewById(R.id.update_pass);

        passOld = passwordNew.getText().toString();
        passNew = passwordNew.getText().toString();

        updatePass.setOnClickListener(this);

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressbar() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String... msg) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.update_pass){
            updatePassNow();
        }
    }

    private void updatePassNow() {

    }
}