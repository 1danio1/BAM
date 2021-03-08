package com.example.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.sqlite.db.SupportSQLiteQueryBuilder;

public class MyContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.myapplication.provider";
    private static final int USERS = 1;
    private static AppDatabase db = null;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, "users", USERS);
    }

    public MyContentProvider() {
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case USERS:
                return "vnd.android.cursor.dir/users";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        db = AppDatabase.getInstance(getContext());
        return db != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch(uriMatcher.match(uri)) {
            case USERS:
                SupportSQLiteQueryBuilder queryBuilder = SupportSQLiteQueryBuilder
                        .builder(User.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);

                cursor = db.getOpenHelper().getReadableDatabase().query(queryBuilder.create());
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}