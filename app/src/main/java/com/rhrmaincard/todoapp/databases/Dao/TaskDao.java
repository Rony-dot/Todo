package com.rhrmaincard.todoapp.databases.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rhrmaincard.todoapp.databases.entitys.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM TASK")
    List<Task> getAll();

    @Insert
    void insert(List<Task> tasks);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);
}
