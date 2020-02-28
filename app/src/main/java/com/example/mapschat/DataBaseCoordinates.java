package com.example.mapschat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseCoordinates extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Coordinates.db";
    public static final String TABLE_USER_LOCATION = "user_location";
    public static final String TABLE_XY = "coordinates_table_xy";
    public static final String TABLE_XYZ = "coordinates_table_xyz";
    public static final String X = "X";
    public static final String Y = "Y";
    public static final String Z = "Z";


    public DataBaseCoordinates(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER_LOCATION + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, X DOUBLE, Y DOUBLE)");
        db.execSQL("CREATE TABLE " + TABLE_XY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, X DOUBLE, Y DOUBLE)");
        db.execSQL("CREATE TABLE " + TABLE_XYZ + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, X DOUBLE, Y DOUBLE, Z DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_XY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_XYZ);
        onCreate(db);
    }

    /**
     *  Is inserting data for location in user's database
     * @param xCoordinate represents Latitude
     * @param yCoordinate represents Longitude
     * @return true if
     */
    public boolean insertDataUserLocation(double xCoordinate, double yCoordinate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(X, xCoordinate);
        contentValues.put(Y, yCoordinate);
        double result = db.insert(TABLE_USER_LOCATION, null, contentValues);
        if(result == -1) {
            return false;
        }else {
            return true;
        }
    }

    /**
     *   It's inserting data in the table with xyz(TABLE_XY).
     * @param xCoordinate represents Latitude
     * @param yCoordinate represents Longitude
     * @return
     */
    public boolean insertDataInTableXY(double xCoordinate, double yCoordinate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(X, xCoordinate);
        contentValues.put(Y, yCoordinate);
        double result = db.insert(TABLE_XY, null, contentValues);
        if(result == -1) {
            return false;
        }else {
            return true;
        }
    }

    /**
     *  It's inserting data in the table with xyz(TABLE_XYZ).
     * @param xCoordinate represents Latitude
     * @param yCoordinate represents Longitude
     * @param zCoordinate
     * @return
     */
    public boolean insertDataInTableXYZ(double xCoordinate, double yCoordinate, double zCoordinate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(X, xCoordinate);
        contentValues.put(Y, yCoordinate);
        contentValues.put(Z, zCoordinate);
        double result = db.insert(TABLE_XYZ, null, contentValues);
        if(result == -1) {
            return false;
        }else {
            return true;
        }
    }

    /**
     *  Is looking if the coordinates are in database.
     * @param xCoordinate represents Latitude
     * @param yCoordinate represents Longitude
     * @return true if it's found and false if isn't.
     */
    public boolean isCoordinatesInUserLocation(double xCoordinate, double yCoordinate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER_LOCATION + " WHERE "+ X + " like " + xCoordinate + " AND " +
                Y + " like " + yCoordinate +";",null);
        if(cursor.getCount() == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *  Updates user location in database by changing it's coordinates.
     * @param xCoordinates represents Latitude
     * @param yCoordinates represents Longitude
     */
    public void updateUserLocation(double xCoordinates, double yCoordinates) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_USER_LOCATION + " SET " + X +" = " + xCoordinates + ", " + Y + " = " + yCoordinates + " WHERE  ID = 1;");
    }

    /**
     *   Verifies if the are any coordinates in database.
     * @param xCoordinates represents Latitude
     * @param yCoordinates represents Longitude
     * @return true if are and false if isn't any coordinates in user's database.
     */
    public boolean isCoordinatesInTableUserLocation(double xCoordinates, double yCoordinates) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER_LOCATION + " WHERE "+ X + " like " + xCoordinates + " AND " +
                Y + " like " + yCoordinates +";",null);
        if(cursor.getCount() == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *   Verifies if the passing coordinates are in XY database.
     * @param xCoordinates represents Latitude
     * @param yCoordinates represents Longitude
     * @return true if are and false if isn't any coordinates in XY's database.
     */
    public boolean isCoordinatesInTableXY(double xCoordinates, double yCoordinates) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_XY + " WHERE "+ X + " like " + xCoordinates + " AND " +
                Y + " like " + yCoordinates +";",null);
        if(cursor.getCount() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteCoordinatesFromTableXY(double xCoordinates, double yCoordinates) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + TABLE_XY + " WHERE "+ X + " like " + xCoordinates + " AND " + Y + " like " + yCoordinates + ";");
    }

    public Cursor readAllCoordinatesFromDatabese(double xCoordinates, double yCoordinates) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("DELETE FROM " + TABLE_XY + " WHERE "+ X + " like " + xCoordinates + " AND " + Y + " like " + yCoordinates, null);
        return cursor;
    }
}
