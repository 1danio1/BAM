package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NumberReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "Broadcast Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra(MyService.USERNAME);
        long number = intent.getLongExtra(MyService.COUNTER_VALUE, 0);

        Log.i(LOG_TAG, name + " " + Long.toString(number));

        new Thread(new Runnable() {
            @Override
            public void run() {
                User userToAdd = new User(name, number);
                AppDatabase db = AppDatabase.getInstance(context);
                UserDao userDao = db.userDao();
                userDao.insert(userToAdd);
            }
        }).start();
    }
}