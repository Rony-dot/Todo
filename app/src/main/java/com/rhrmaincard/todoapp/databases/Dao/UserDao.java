package com.rhrmaincard.todoapp.databases.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rhrmaincard.todoapp.databases.entitys.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("Select * from USER")
    List<User> getAll();

    @Query("Select * from USER where id=:id")
    User getById(long id);

    @Query("Select * from USER where email=:email")
    User getByEmail(String email);

    @Query("Select * from USER where user_Name=:username")
    User getByUserName(String username);

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
