package com.rhrmaincard.todoapp.presenter;

public interface RegistrationPresenter extends  BasePresenter.View{
    interface Model{
        void registration(String firstName, String lastName, String email, String username,String password);

    }
}
