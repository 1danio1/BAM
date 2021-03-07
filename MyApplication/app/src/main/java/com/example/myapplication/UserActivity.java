package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {
    public static final String USER_ACTIVITY_NAME = "com.example.myapplication.USERNAME";
    private static final String LOG_TAG = "UserActivity";
    private String name = "";
    private BroadcastReceiver numberReceiver = null;
    private MyService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //register receiver
        numberReceiver = new NumberReceiver();
        IntentFilter intentFilter = new IntentFilter();
        registerReceiver(numberReceiver, intentFilter);

        // set username
        name = getIntent().getStringExtra(MainActivity.MAIN_ACTIVITY_NAME);
        if(!name.isEmpty()) {
            TextView textView = findViewById(R.id.textView);
            textView.setText(name);
        }
    }

    @Override
    protected void onDestroy() {
        //unregister receiver
        if(numberReceiver != null) {
            unregisterReceiver(numberReceiver);
            numberReceiver = null;
        }

        super.onDestroy();
    }

    public void startService(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra(USER_ACTIVITY_NAME, name);
        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    public void getRecordsCount(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                UserDao userDao = db.userDao();
                long count = userDao.countAll();

                Log.i(LOG_TAG, "Record count: " + Long.toString(count));
            }
        }).start();
    }
}