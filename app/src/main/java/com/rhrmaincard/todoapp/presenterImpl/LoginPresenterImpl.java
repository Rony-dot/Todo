package com.rhrmaincard.todoapp.presenterImpl;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.rhrmaincard.todoapp.activitys.LoginActivity;
import com.rhrmaincard.todoapp.databases.Repositorys.UserRepo;
import com.rhrmaincard.todoapp.databases.entitys.User;
import com.rhrmaincard.todoapp.presenter.BasePresenter;
import com.rhrmaincard.todoapp.presenter.LoginPresenter;
import com.rhrmaincard.todoapp.utils.CommonTask;

import java.util.List;

public class LoginPresenterImpl implements LoginPresenter.Model {
    BasePresenter.View view;
    Context context;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 999;


    public LoginPresenterImpl(BasePresenter.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void connectWithRemoteServer(String username, String password) {
        if(TextUtils.isEmpty(username)){
            view.onError("Username not found!");
            return;
        }

//        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
//            view.onError("Invalid username!");
//            return;
//        }

        if(password.length()<6){
            view.onError("Password must be 6 character long!");
            return;
        }

        UserRepo userRepo = new UserRepo(context);
        List<User> userList = userRepo.getAll();
        for(User user: userList){
            if(username.equals(user.getUser_Name())  && password.equals(user.getPassword())){
                view.onSuccess();
            }
        }

//        if(username.toLowerCase().equals("RONY".toLowerCase()) &&
//        password.equals("123456"))
//        view.onSuccess();

    }

    @Override
    public void connectWithGoogle() {
        view.onSuccess();
    }

    @Override
    public void connectWithFacebook() {
        view.onSuccess();
    }

    @Override
    public void signUp(String username, String password) {
        view.onSuccess();
    }
}
