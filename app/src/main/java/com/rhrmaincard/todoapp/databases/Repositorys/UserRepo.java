package com.rhrmaincard.todoapp.databases.Repositorys;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rhrmaincard.todoapp.databases.AppDatabase;
import com.rhrmaincard.todoapp.databases.entitys.User;
import com.rhrmaincard.todoapp.utils.CommonConstant;

import java.util.List;

public class UserRepo {

    AppDatabase database;
    Context context;

    public UserRepo(Context context){
        this.context = context;
        database = Room.databaseBuilder(context,AppDatabase.class, CommonConstant.DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

   public List<User> getAll(){
        return database.userDao().getAll();
    }

    public  User getBy(long id){
        return database.userDao().getById(id);
    }

    public User getByUserName(String username){
        return database.userDao().getByUserName(username);
    }

    public User getByEmail(String email){
        return database.userDao().getByEmail(email);
    }

    public void insertUser(User user){
        database.userDao().insertUser(user);
    }

    public void deleteUser(User user){
        database.userDao().deleteUser(user);
    }

    public void updateUser(User user){
        database.userDao().updateUser(user);
    }
}
