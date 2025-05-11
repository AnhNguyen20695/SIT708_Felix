package com.example.learningexpapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.learningexpapp.models.User;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "quiz.db";
    public static final String TABLE_NAME_USERS = "users";
    public static final String COL_1_USERS = "username";
    public static final String COL_2_USERS = "fullname";
    public static final String COL_3_USERS = "password";
    public static final String COL_4_USERS = "records";
    public static final String COL_5_USERS = "recommendedtopic";
    public static final String COL_6_USERS = "taskquestions";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;
    }

    public User login(String username, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from "+TABLE_NAME_USERS+" where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            User row = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            cursor.close();
            return row;
        }else {
            Log.i("MAIN_LOG","Failed to get user "+username+".");
            cursor.close();
            return new User();
        }
    }


    public String register(String fullname, String username, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Log.i("MAIN_LOG","Registering...");
        Cursor cursor = MyDatabase.rawQuery("Select * from "+TABLE_NAME_USERS+" where username = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.close();
            Log.i("MAIN_LOG","User existed. Please select a different username.");
            return "user_existed";
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2_USERS, fullname);
            contentValues.put(COL_1_USERS, username);
            contentValues.put(COL_3_USERS, password);
            contentValues.put(COL_5_USERS, "no_recommend_topic");
            contentValues.put(COL_6_USERS, "no_generated_questions");
            long result = MyDatabase.insert(TABLE_NAME_USERS, null, contentValues);
            Log.i("MAIN_LOG","Registering result: "+result);
            cursor.close();
            if (result == -1) {
                return "fail";
            } else {
                return "success";
            }
        }
    }

    public void registerInterests(String username, String records) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Log.i("MAIN_LOG","Registering interests "+records);
        MyDatabase.execSQL("UPDATE "+TABLE_NAME_USERS+" SET "+COL_4_USERS+" = '"+records+"' " +
                            "WHERE "+COL_1_USERS+" = '"+username+"' ;");
        Log.i("MAIN_LOG","Registered interests for user: "+username);
    }

    public void updateData(String username, String colName, String dataText) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Log.i("MAIN_LOG","SQL: "+"UPDATE "+TABLE_NAME_USERS+" SET "+colName+" = '"+dataText+"' " +
                "WHERE "+COL_1_USERS+" = '"+username+"' ;");
        MyDatabase.execSQL("UPDATE "+TABLE_NAME_USERS+" SET "+colName+" = '"+dataText+"' " +
                "WHERE "+COL_1_USERS+" = '"+username+"' ;");
        Log.i("MAIN_LOG","Data updated for user: "+username+" | at column: +"+colName);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("MAIN_LOG","creating table users.");
        db.execSQL("CREATE TABLE "+TABLE_NAME_USERS+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_1_USERS+" TEXT, "+COL_2_USERS+" TEXT, "+COL_3_USERS+" TEXT, "+COL_4_USERS+" TEXT, "+COL_5_USERS+" TEXT, "+COL_6_USERS+" TEXT) ;");
        Log.i("MAIN_LOG","table users created.");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("MAIN_LOG","upgrading table users.");
        db.execSQL("drop table if exists "+TABLE_NAME_USERS+" ;");
        onCreate(db);
    }
}
