package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;

public class MyService extends Service {
    public static final String USERNAME = "com.example.myapplication.USERNAME";
    public static final String COUNTER_VALUE = "com.example.myapplication.COUNTER_VALUE";
    private String username = "";
    ArrayList<MyServiceThread> threads = new ArrayList<MyServiceThread>();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        username = intent.getStringExtra(UserActivity.USER_ACTIVITY_NAME);
        MyServiceThread service = new MyServiceThread(startId);
        service.start();
        threads.add(service);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        long counterValue;
        Intent intent = null;
        for(MyServiceThread thread : threads) {
            counterValue = thread.stopCounting();
            intent = new Intent(this, NumberReceiver.class);
            intent.putExtra(USERNAME, username);
            intent.putExtra(COUNTER_VALUE, counterValue);
            sendBroadcast(intent);
        }

        super.onDestroy();
    }
}