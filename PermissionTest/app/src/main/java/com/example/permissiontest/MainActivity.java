package com.example.permissiontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.CalendarContract;
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
    public static final int READ_CALENDAR_PERMISSION_REQUEST_CODE = 2;
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

        switch (requestCode) {
            case READ_CONTACTS_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Read contacts. Permission DENIED", Toast.LENGTH_LONG).show();
                }
                break;
            case READ_CALENDAR_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readCalendar();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Read calendar. Permission DENIED", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void readContacts() {
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

    public void toggleBluetooth(View view) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if(isEnabled) {
            bluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "Bluetooth disabled", Toast.LENGTH_SHORT).show();
        }
        else {
            bluetoothAdapter.enable();
            Toast.makeText(getApplicationContext(), "Bluetooth enabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickReadCalendar(View view) {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALENDAR}, READ_CALENDAR_PERMISSION_REQUEST_CODE);
    }

    private void readCalendar() {
        Toast.makeText(getApplicationContext(), "CALENDAR TEST", Toast.LENGTH_SHORT).show();

        final String LOG_TAG = "READ_CALENDAR";
        String[] projection = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.NAME,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE};

        try {
            Cursor cursor = getContentResolver().query(CalendarContract.Calendars.CONTENT_URI,
                    projection, null, null, null);
            if(cursor.moveToFirst()) {
                do {
                    String calendarId = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars._ID));
                    String name = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.NAME));
                    String accountName = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME));
                    String accountType = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE));
                    Log.d(LOG_TAG, "Calendar " + calendarId + " " + name + " " + accountName + " " + accountType);
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch(final Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}