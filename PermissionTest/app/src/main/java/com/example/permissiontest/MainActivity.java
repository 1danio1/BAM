package com.example.permissiontest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver networkStateReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkStateReceiver = new NetworkStateReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        if(networkStateReceiver != null) {
            unregisterReceiver(networkStateReceiver);
            networkStateReceiver = null;
        }

        super.onDestroy();
    }

    public void executeGetRequest(View view) {
        final String LOG_TAG = "GET_REQUEST";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://jsonplaceholder.typicode.com/posts/");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);

                    int status = con.getResponseCode();

                    BufferedReader in;
                    if(status > 299) {
                        in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    else {
                        in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    }

                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    con.disconnect();

                    Log.i(LOG_TAG, content.toString());
                }
                catch(Exception ex) {
                    Log.e(LOG_TAG, ex.getMessage());
                }
            }
        }).start();
    }
}