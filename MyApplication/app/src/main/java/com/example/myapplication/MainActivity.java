package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String MAIN_ACTIVITY_NAME = "com.example.myapplication.NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent shareIntent = new Intent(Intent.ACTION_VIEW);
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setData(Uri.parse(MyContentProvider.AUTHORITY));
        //https://developer.android.com/training/basics/intents/package-visibility-use-cases#grant-uri-access
    }

    public void openNewActivity(View view) {
        EditText editText = (EditText)findViewById(R.id.editTextTextPersonName);
        String name = editText.getText().toString().trim();

        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(MAIN_ACTIVITY_NAME, name);
        startActivity(intent);
    }
}