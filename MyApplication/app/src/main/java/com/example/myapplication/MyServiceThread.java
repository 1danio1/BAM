package com.example.myapplication;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MyServiceThread extends Thread {
    private static final String LOG_TAG = "Service ";
    private static final long PERIOD_MS = 1000L;

    private long counter = 0;
    private Timer timer = null;
    private int startId = 0;

    public MyServiceThread(int startId) {
        this.startId = startId;
    }

    @Override
    public void run() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i(LOG_TAG + startId, Long.toString(counter));
                counter++;
            }
        }, 0, PERIOD_MS);
    }

    public long stopCounting() {
        if(timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }

        return counter;
    }
}