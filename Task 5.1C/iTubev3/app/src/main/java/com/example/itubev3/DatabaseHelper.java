package com.example.itubev3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "itube.db";
    public static final String TABLE_NAME = "users";
    public static final String COL_1 = "username";
    public static final String COL_2 = "fullname";
    public static final String COL_3 = "password";

    public static final String TABLE_NAME_PLAYLIST = "playlist";
    public static final String COL_1_PLAYLIST = "username";
    public static final String COL_2_PLAYLIST = "url";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;
    }

    public String login(String username, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from "+TABLE_NAME+" where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            cursor.close();
            return "success";
        }else {
            cursor.close();
            return "fail";
        }
    }

    public String register(String fullname, String username, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullname", fullname);
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDatabase.insert(TABLE_NAME, null, contentValues);
        Log.i("MAIN_LOG","Registering result: "+result);
        if (result == -1) {
            return "fail";
        } else {
            return "success";
        }
    }

    public String addToPlaylist(String username, String url) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.i("MAIN_LOG","Adding to playlist: "+username+" | url: "+url);
        contentValues.put(COL_1_PLAYLIST, username);
        contentValues.put(COL_2_PLAYLIST, url);
        long result = MyDatabase.insert(TABLE_NAME_PLAYLIST, null, contentValues);
        Log.i("MAIN_LOG","Adding to playlist result: "+result);
        if (result == -1) {
            return "fail";
        } else {
            return "success";
        }
    }

    public List<Playlist> retrievePlaylist(String username) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        List<Playlist> result = new ArrayList<Playlist>();
        Log.i("MAIN_LOG","Retrieving playlist");
        Cursor cursor = MyDatabase.rawQuery("Select * from "+TABLE_NAME_PLAYLIST+" where username = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            Log.i("MAIN_LOG","Retrieved in databasehelper: "+cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    Playlist row = new Playlist(
                            cursor.getString(0),
                            cursor.getString(1)
                    );
                    result.add(row);
                } while (cursor.moveToNext());
            }
            cursor.close();
            Log.i("MAIN_LOG","Added to retrieval result: "+result.size());
            return result;
        } else {
            cursor.close();
            Log.i("MAIN_LOG","Failed to retrieve in databasehelper: "+result.size());
            return result;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("MAIN_LOG","creating table users.");
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COL_1+" TEXT PRIMARY KEY, "+COL_2+" TEXT, "+COL_3+" TEXT) ;");
        Log.i("MAIN_LOG","table users created.");

        Log.i("MAIN_LOG","creating table playlist.");
        db.execSQL("CREATE TABLE "+TABLE_NAME_PLAYLIST+" ("+COL_1_PLAYLIST+" TEXT, "+COL_2_PLAYLIST+" TEXT) ;");
        Log.i("MAIN_LOG","table playlist created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NAME+" ;");
        db.execSQL("drop table if exists "+TABLE_NAME_PLAYLIST+" ;");
        onCreate(db);
    }
}
