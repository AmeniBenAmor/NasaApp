package com.example.projectfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

public class UserActivitesDataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG= "UserActivityDBHelper";
    private static final String TABLE_NAME= "user_activity_table";
    private static final String COL1= "ID";
    private static final String COL2= "date";
    private static final String COL3= "activity";

    public UserActivitesDataBaseHelper (Context context){
        super(context, TABLE_NAME, null, 1);


    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( "+COL1+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT,"+COL3 +" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP table "+TABLE_NAME);
        }
        catch (Exception e){
            Log.e(TAG,e.toString());
        }

        onCreate(db);

    }

    public String addData(String activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Date date = new Date();
        contentValues.put(COL2, date.toString());
        contentValues.put(COL3, activity);
        Log.d(TAG, "addData: Adding " + activity + " to " + TABLE_NAME);


        try{
            long result = db.insertOrThrow(TABLE_NAME, null, contentValues);

            //if date as inserted incorrectly it will return -1
            if (result != -1) {
                return "activity saveed";
            } else {
                return "A problem has accured";
            }


        }catch (Exception e){
            Log.e("Inseret Error: ", e.toString() );
            return "A problem has accured";
        }

    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }



}
