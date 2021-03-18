package com.example.permissiontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 1;
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

    public void clickReadContacts(View view) {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == READ_CONTACTS_PERMISSION_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            }
            else {
                Toast.makeText(getApplicationContext(), "Read contacts. Permission DENIED", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void readContacts() {
        final String LOG_TAG = "READ_CONTACTS";
        try {
            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            if(cursor.moveToFirst()) {
                do {
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Log.d(LOG_TAG, "Contact " + contactId + " " + displayName);
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch(final Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}