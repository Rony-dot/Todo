package com.rhrmaincard.todoapp.presenterImpl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import com.rhrmaincard.todoapp.databases.Repositorys.UserRepo;
import com.rhrmaincard.todoapp.databases.entitys.User;
import com.rhrmaincard.todoapp.presenter.BasePresenter;
import com.rhrmaincard.todoapp.presenter.UpdatePresenter;

import java.net.ContentHandler;
import java.util.List;

public class UpdatePresenterImpl implements UpdatePresenter.Model {
    BasePresenter.View view;
    Context context;

    public UpdatePresenterImpl(BasePresenter.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void update(String firstName, String lastName, String email, String username, String passwordNew) {
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
        if(passwordNew.length()<6){
            view.onError("password must be 6 chars long");
            return;
        }


        UserRepo userRepo = new UserRepo(context);

        List<User> usersList = userRepo.getAll();
        User oldUser = userRepo.getByUserName(username);

        for(User user: usersList){
            if(!user.getUser_Name().equals(oldUser.getUser_Name())){

            if(username.equals(user.getUser_Name())){
                view.onError("Username already exists");
                return;
            }
            if(email.equals(user.getEmail())){
                view.onError("Email already exists");
                return;
            }
//            if(!passwordOld.equals(user.getPassword())){
//                view.onError("Wrong password");
//                return;
//            }

            }
        }


//        User user = new User();
        oldUser.setFirst_name(firstName);
        oldUser.setLast_name(lastName);
        oldUser.setEmail(email);
        oldUser.setUser_Name(username);
        oldUser.setPassword(passwordNew);
        userRepo.updateUser(oldUser);
        view.onSuccess();
    }

    @Override
    public void UpdatePassword(String username, String passOld, String passNew) {
        UserRepo userRepo = new UserRepo(context);
        List<User> usersList = userRepo.getAll();
//        User oldUser = userRepo.getByUserName(username);

        for(User user: usersList){
            if(user.getUser_Name().equals(username) &&
            user.getPassword().equals(passOld)){
                user.setPassword(passNew);
            }
        }

    }
}
