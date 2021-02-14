package com.rhrmaincard.todoapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.presenter.BasePresenter;
import com.rhrmaincard.todoapp.presenter.LoginPresenter;
import com.rhrmaincard.todoapp.presenter.RegistrationPresenter;
import com.rhrmaincard.todoapp.presenterImpl.LoginPresenterImpl;
import com.rhrmaincard.todoapp.presenterImpl.RegistrationPresenterImpl;
import com.rhrmaincard.todoapp.utils.CommonTask;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, BasePresenter.View {

    EditText firstName, lastName, email, username, password;
    Button signIn;
    RegistrationPresenter.Model registrationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registrationPresenter = new RegistrationPresenterImpl(this,this);
        firstName = findViewById(R.id.et_first_name);
        lastName = findViewById(R.id.et_last_name);
        email = findViewById(R.id.et_email);
        username = findViewById(R.id.et_username_reg);
        password = findViewById(R.id.et_password_reg);
        signIn = findViewById(R.id.sign_in_reg);
        signIn.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    String first_name,last_name ,user_email ,user_name ,user_password;
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_reg) {
            getAllData();

            registrationPresenter.registration(first_name, last_name, user_email, user_name, user_password);
        }
    }

    private void getAllData() {
        first_name = firstName.getText().toString();
        if(first_name.trim().isEmpty()){
            firstName.setError("first name is empty");
        }
        // TODO set error for all below
        last_name = lastName.getText().toString();
        user_email = email.getText().toString();
        user_name = username.getText().toString();
        user_password = password.getText().toString();

        /**
         * saving data in shared preferences
         */

    }


    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressbar() {

    }

    @Override
    public void onSuccess() {
        CommonTask.addDataToSharedPreferences(this,"First_Name",first_name);
        CommonTask.addDataToSharedPreferences(this,"Last_Name",last_name);
        CommonTask.addDataToSharedPreferences(this,"Email",user_email);
        CommonTask.addDataToSharedPreferences(this,"Username",user_name);
        CommonTask.addDataToSharedPreferences(this,"Password",user_password);

        finish();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onError(String... msg) {
        CommonTask.printLog("TAG",msg[0]);
    }
}