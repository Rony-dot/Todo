package com.rhrmaincard.todoapp.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.rhrmaincard.todoapp.databases.Dao.TaskDao;
import com.rhrmaincard.todoapp.databases.Dao.UserDao;
import com.rhrmaincard.todoapp.databases.entitys.Task;
import com.rhrmaincard.todoapp.databases.entitys.User;

@Database(entities = {Task.class, User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract UserDao userDao();
}