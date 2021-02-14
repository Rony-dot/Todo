package com.rhrmaincard.todoapp.presenter;

public interface BasePresenter {
    interface View{
        void showProgressBar();
        void hideProgressbar();
        void onSuccess();
        void onError(String... msg);
    }
}
