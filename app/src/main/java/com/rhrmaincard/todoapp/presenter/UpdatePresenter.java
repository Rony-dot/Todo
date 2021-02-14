package com.rhrmaincard.todoapp.presenter;

public interface UpdatePresenter extends BasePresenter.View{
    interface Model{
        void update(String firstName, String lastName, String email, String username, String passwordNew);
    void UpdatePassword(String username, String passOld, String passNew);
    }
}
