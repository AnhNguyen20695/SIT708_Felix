package com.example.lostandfoundv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.lostandfoundv2.models.FoundModel;
import com.example.lostandfoundv2.models.LostModel;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "lostfound.db";
    public static final String TABLE_NAME_LOST = "lost";
    public static final String COL_1_LOST = "name";
    public static final String COL_2_LOST = "PhoneNumber";
    public static final String COL_3_LOST = "Description";
    public static final String COL_4_LOST = "ReportedDate";
    public static final String COL_5_LOST = "Location";
    public static final String COL_6_LOST = "LocationLatitude";
    public static final String COL_7_LOST = "LocationLongitude";

    public static final String TABLE_NAME_FOUND = "found";
    public static final String COL_1_FOUND = "name";
    public static final String COL_2_FOUND = "PhoneNumber";
    public static final String COL_3_FOUND = "Description";
    public static final String COL_4_FOUND = "ReportedDate";
    public static final String COL_5_FOUND = "Location";
    public static final String COL_6_FOUND = "LocationLatitude";
    public static final String COL_7_FOUND = "LocationLongitude";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;
    }

    public String addToLostItems(String name,
                                 String phoneNumber,
                                 String description,
                                 String reportedDate,
                                 String location,
                                 String locationLat,
                                 String locationLng) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_LOST, name);
        contentValues.put(COL_2_LOST, phoneNumber);
        contentValues.put(COL_3_LOST, description);
        contentValues.put(COL_4_LOST, reportedDate);
        contentValues.put(COL_5_LOST, location);
        contentValues.put(COL_6_LOST, locationLat);
        contentValues.put(COL_7_LOST, locationLng);
        long result = MyDatabase.insert(TABLE_NAME_LOST, null, contentValues);
        if (result == -1) {
            return "fail";
        } else {
            return "success";
        }
    }
    public List<LostModel> retrieveLostItems() {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        List<LostModel> result = new ArrayList<LostModel>();
        Cursor cursor = MyDatabase.rawQuery("Select * from "+TABLE_NAME_LOST+" ;", new String[]{});
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    LostModel row = new LostModel(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7)
                    );
                    result.add(row);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return result;
        } else {
            cursor.close();
            return result;
        }
    }
    public String deleteLostItem(Integer id) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        String result = "fail";
        MyDatabase.execSQL("Delete from "+TABLE_NAME_LOST+" where ID = "+id+" ;");

        return result;
    }

    public String addToFoundItems(String name,
                                  String phoneNumber,
                                  String description,
                                  String reportedDate,
                                  String location,
                                  String locationLat,
                                  String locationLng) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_FOUND, name);
        contentValues.put(COL_2_FOUND, phoneNumber);
        contentValues.put(COL_3_FOUND, description);
        contentValues.put(COL_4_FOUND, reportedDate);
        contentValues.put(COL_5_FOUND, location);
        contentValues.put(COL_6_LOST, locationLat);
        contentValues.put(COL_7_LOST, locationLng);
        long result = MyDatabase.insert(TABLE_NAME_FOUND, null, contentValues);
        if (result == -1) {
            return "fail";
        } else {
            return "success";
        }
    }

    public List<FoundModel> retrieveFoundItems() {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        List<FoundModel> result = new ArrayList<FoundModel>();
        Cursor cursor = MyDatabase.rawQuery("Select * from "+TABLE_NAME_FOUND+" ;", new String[]{});
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    FoundModel row = new FoundModel(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7)
                    );
                    result.add(row);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return result;
        } else {
            cursor.close();
            return result;
        }
    }

    public String deleteFoundItem(Integer id) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        String result = "fail";
        MyDatabase.execSQL("Delete from "+TABLE_NAME_FOUND+" where ID = "+id+" ;");

        return result;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("MAIN_LOG","creating table lost.");
        db.execSQL("CREATE TABLE "+TABLE_NAME_LOST+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_1_LOST+" TEXT,"+
                COL_2_LOST+" TEXT, "+
                COL_3_LOST+" TEXT, "+
                COL_4_LOST+" TEXT, "+
                COL_5_LOST+" TEXT, "+
                COL_6_LOST+" TEXT, "+
                COL_7_LOST+" TEXT) ;");
        Log.i("MAIN_LOG","table lost created.");

        Log.i("MAIN_LOG","creating table found.");
        db.execSQL("CREATE TABLE "+TABLE_NAME_FOUND+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_1_FOUND+" TEXT,"+
                COL_2_FOUND+" TEXT, "+
                COL_3_FOUND+" TEXT, "+
                COL_4_FOUND+" TEXT, "+
                COL_5_FOUND+" TEXT, "+
                COL_6_FOUND+" TEXT, "+
                COL_7_FOUND+" TEXT) ;");
        Log.i("MAIN_LOG","table found created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NAME_LOST+" ;");
        db.execSQL("drop table if exists "+TABLE_NAME_FOUND+" ;");
        onCreate(db);
    }
}
