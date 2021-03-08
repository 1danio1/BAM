package com.example.usescontentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String ID_COLUMN = "uid";
    private static final String NAME_COLUMN = "name";
    private static final String NUMBER_COLUMN = "number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getDataFromProvider(View view) {
        final String LOG_TAG = "Content Provider";
        try {
            Cursor cursor = getContentResolver().
                    query(Uri.parse("com.example.myapplication.provider/users"), null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    long id;
                    String name;
                    long number;
                    while (!cursor.isAfterLast()) {
                        id = cursor.getLong(cursor.getColumnIndex(ID_COLUMN));
                        name = cursor.getString(cursor.getColumnIndex(NAME_COLUMN));
                        number = cursor.getLong(cursor.getColumnIndex(NUMBER_COLUMN));
                        Log.i(LOG_TAG, "ID: " + id + "\tName: " + name + "\tNumber: " + number);
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            }
        }
        catch(final Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}