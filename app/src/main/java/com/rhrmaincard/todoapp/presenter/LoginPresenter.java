package com.rhrmaincard.todoapp.presenter;

public interface LoginPresenter extends BasePresenter{
    interface Model{
        void connectWithRemoteServer(String username, String password);
        void connectWithGoogle();
        void connectWithFacebook();
        void signUp(String username, String password);
    }

}
