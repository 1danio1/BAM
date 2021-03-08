package com.example.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "MyApplication_Database.db";
    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(final Context context) {
        AppDatabase result = instance;
        if(result != null) {
            return result;
        }

        synchronized(AppDatabase.class) {
            if(instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
            }
            return instance;
        }
    }

    public static void closeConnection() {
        synchronized(AppDatabase.class) {
            if(instance != null) {
                instance.close();
                instance = null;
            }
        }
    }

    public abstract UserDao userDao();
}
