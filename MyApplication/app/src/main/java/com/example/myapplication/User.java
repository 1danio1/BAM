package com.example.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = User.TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "User";

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "number")
    public long number;

    public User() { }

    @Ignore
    public User(String name, long number) {
        this.name = name;
        this.number = number;
    }
}
