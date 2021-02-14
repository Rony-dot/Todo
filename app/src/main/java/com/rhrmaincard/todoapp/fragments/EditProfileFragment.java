package com.rhrmaincard.todoapp.fragments;

import android.app.UiAutomation;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.activitys.NavMainActivity;
import com.rhrmaincard.todoapp.presenter.BasePresenter;
import com.rhrmaincard.todoapp.presenter.UpdatePresenter;
import com.rhrmaincard.todoapp.presenterImpl.UpdatePresenterImpl;
import com.rhrmaincard.todoapp.utils.CommonTask;


public class EditProfileFragment extends Fragment implements View.OnClickListener, BasePresenter.View {

    EditText firstName, lastName, email, password_old, password_new;
    TextView username;
    Button update;

    String first_name, last_name, user_email, user_name, user_password_new;// user_password_old;
    UpdatePresenter.Model updatePresenter;


    public EditProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updatePresenter = new UpdatePresenterImpl(this,requireContext());

        firstName = view.findViewById(R.id.et_first_name_edit);
        lastName = view.findViewById(R.id.et_last_name_edit);
        email = view.findViewById(R.id.et_email_edit);
        username = view.findViewById(R.id.tv_username_edit);
        password_new = view.findViewById(R.id.et_password_new);
//        password_old = view.findViewById(R.id.et_password_old);
        update = view.findViewById(R.id.update_profile);

        firstName.setText(CommonTask.getDataFromSharedPreferences(requireContext(),"First_Name").toString());
        lastName.setText(CommonTask.getDataFromSharedPreferences(requireContext(),"Last_Name").toString());
        email.setText(CommonTask.getDataFromSharedPreferences(requireContext(),"Email").toString());
        username.setText(CommonTask.getDataFromSharedPreferences(requireContext(),"Username").toString());
        password_new.setText(CommonTask.getDataFromSharedPreferences(requireContext(),"Password").toString());

        update.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.update_profile){
            updateProfile();
        }
    }

    private void updateProfile() {
        first_name = firstName.getText().toString();
        last_name =  lastName.getText().toString();
        user_email = email.getText().toString();
        user_name = username.getText().toString();
        user_password_new = password_new.getText().toString();
        //user_password_old = password_old.getText().toString();

        if(first_name.equals("")){
          firstName.setError("cant be empty");
        }

        updatePresenter.update(first_name,last_name,user_email,user_name,user_password_new);

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressbar() {

    }

    @Override
    public void onSuccess() {
        CommonTask.addDataToSharedPreferences(getContext(),"First_Name",first_name);
        CommonTask.addDataToSharedPreferences(getContext(),"Last_Name",last_name);
        CommonTask.addDataToSharedPreferences(getContext(),"Email",user_email);
        CommonTask.addDataToSharedPreferences(getContext(),"Username",user_name);
        CommonTask.addDataToSharedPreferences(getContext(),"Password",user_password_new);

        getActivity().onBackPressed();
    }

    @Override
    public void onError(String... msg) {
        CommonTask.printLog("TAG",msg[0]);

    }
}