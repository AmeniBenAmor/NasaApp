package com.example.projectfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class WeatherDataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG= "WeatherDataBAseHelper";
    private static final String TABLE_NAME= "weather_table";
    private static final String COL1= "date";
    private static final String COL2= "cloud_score";
    private static final String COL3= "latitude";
    private static final String COL4= "longitude";
    private static final String COL5= "path";

    public WeatherDataBaseHelper(Context context){
        super(context, TABLE_NAME, null, 1);


    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( "+COL1+" TEXT PRIMARY KEY , " +
                COL2 +" TEXT,"+COL3 +" TEXT,"+COL4+" TEXT,"+COL5 +" TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP table weather_table");
        }
        catch (Exception e){
            Log.e("DataBaseHelper",e.toString());
        }

        onCreate(db);

    }

    public String addData(String date,String cloud_score,String latitude,String longitude,String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, date);
        contentValues.put(COL2, cloud_score);
        contentValues.put(COL3, latitude);
        contentValues.put(COL4, longitude);
        contentValues.put(COL5, path);
        Log.d(TAG, "addData: Adding " + date + " to " + TABLE_NAME);


        try{
            long result = db.insertOrThrow(TABLE_NAME, null, contentValues);

            //if date as inserted incorrectly it will return -1
            if (result != -1) {
                return "Image Added";
            } else {
                return "A problem has accured";
            }


        }catch (Exception e){
            Log.e("Inseret Error: ", e.toString() );
            String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                    + COL1 + " = '" + date + "'" ;
            Log.d(TAG, "deleteName: query: " + query);
            Log.d(TAG, "deleteName: Deleting " + date + " from database.");
            db.execSQL(query);
            db.insert(TABLE_NAME, null, contentValues);
            return ("ReSaved");
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
    public Cursor getDataByPath(String path){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME+" where path='"+path+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }



}


