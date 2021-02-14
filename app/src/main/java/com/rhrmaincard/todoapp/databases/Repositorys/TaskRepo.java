package com.rhrmaincard.todoapp.databases.Repositorys;

import android.content.Context;

import androidx.room.Room;

import com.rhrmaincard.todoapp.databases.AppDatabase;
import com.rhrmaincard.todoapp.databases.entitys.Task;
import com.rhrmaincard.todoapp.utils.CommonConstant;

import java.util.List;

public class TaskRepo {
    public AppDatabase database;
    public Context context;

    public TaskRepo(Context context) {
        this.context = context;
        database = Room.databaseBuilder(context,AppDatabase.class, CommonConstant.DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public void addTask(List<Task> tasks){
        database.taskDao().insert(tasks);
    }

    public List<Task> getAll(){
        return database.taskDao().getAll();
    }

    public void deleteTask(Task task){
        database.taskDao().delete(task);
    }
    public void updateTask(Task task){
        database.taskDao().update(task);
    }

}

