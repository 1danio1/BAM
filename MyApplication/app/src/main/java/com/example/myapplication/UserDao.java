package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("select count(*) from " + User.TABLE_NAME)
    long countAll();

    @Insert
    List<Long> insert(User... users);

    @Delete
    void delete(User user);
}
