package com.example.permissiontest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "NetworkStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Checking network state");

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                Log.d(LOG_TAG, "Is connected: " + networkInfo.isConnected());
                Log.d(LOG_TAG, "Type: " + networkInfo.getType() + " " + ConnectivityManager.TYPE_WIFI);
            }
        }
        catch(final Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}