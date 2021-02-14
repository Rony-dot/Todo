package com.rhrmaincard.todoapp.presenterImpl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import com.rhrmaincard.todoapp.databases.Repositorys.UserRepo;
import com.rhrmaincard.todoapp.databases.entitys.User;
import com.rhrmaincard.todoapp.presenter.BasePresenter;
import com.rhrmaincard.todoapp.presenter.RegistrationPresenter;

import java.util.List;
import java.util.regex.Pattern;

public class RegistrationPresenterImpl implements RegistrationPresenter.Model {
    BasePresenter.View view;
    Context context;

    public RegistrationPresenterImpl(BasePresenter.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void registration(String firstName, String lastName, String email, String username, String password) {
        if(TextUtils.isEmpty(firstName)){
            view.onError("firstname is empty");
            return;
        }
        if(TextUtils.isEmpty(lastName)){
            view.onError("lastname is empty");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            view.onError("email is not valid");
            return;
        }
        if(!email.contains("@")) {
            return ;
        }
        if(password.length()<6){
            view.onError("password must be 6 chars long");
            return;
        }


        UserRepo userRepo = new UserRepo(context);

        List<User> usersList = userRepo.getAll();
        for(User user: usersList){
           if(username.equals(user.getUser_Name())){
               view.onError("Username already exists");
               return;
           }
            if(email.equals(user.getEmail())){
                view.onError("Email already exists");
                return;
            }
        }

        User user = new User();
        user.setFirst_name(firstName);
        user.setLast_name(lastName);
        user.setEmail(email);
        user.setUser_Name(username);
        user.setPassword(password);
        userRepo.insertUser(user);
        view.onSuccess();
    }
}
