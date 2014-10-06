package com.example.Journey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Megan on 6/10/2014.
 */
public class MySQLHelper extends SQLiteOpenHelper {

    public static final String TABLE_JOURNEYS = "journeys";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITlE = "title";
    private String[] allColumns = { MySQLHelper.COLUMN_ID,
            MySQLHelper.COLUMN_TITlE };

    private static final String DATABASE_NAME = "journeys.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase database;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_JOURNEYS
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITlE + " text not null);";

    public MySQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    // opening and closing
    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }
    public void closeDB() {
        if (database != null && database.isOpen())
            database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNEYS);
        onCreate(db);
    }

    // translates cursors into journeys/points
    private tblJourney cursorToJourney(Cursor cursor) {
        tblJourney journey = new tblJourney();
        journey.setID(cursor.getInt(0));
        journey.setTitle(cursor.getString(1));
        return journey;
    }
    private tblPoint cursorToPoint(Cursor cursor) {
        tblPoint point = new tblPoint();
        point.setTimeStamp(cursor.getString(0));
        point.setLat(cursor.getLong(1));
        point.setLong(cursor.getLong(2));
        point.setimgURL(cursor.getString(3));
        point.setComment(cursor.getString(4));
        return point;
    }

    // adds a journey to the table
    public void createJourney(tblJourney journey) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, journey.getID());
        values.put(COLUMN_TITlE, journey.getTitle());
    }

    // gets journey/s from the table
    public tblJourney getJourney(int ID){
        // make the query
        String selectQuery = "SELECT  * FROM " + TABLE_JOURNEYS + " WHERE "
                + COLUMN_ID + " = " + ID;

        // get the cursor
        Cursor c = database.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();

        // get journey
        tblJourney journey = new tblJourney();
        journey.setID(c.getInt(c.getColumnIndex(COLUMN_ID)));
        journey.setTitle(c.getString(c.getColumnIndex(COLUMN_TITlE)));
        return journey;
    }
    public ArrayList<tblJourney> getAllJourneys() {
        ArrayList<tblJourney> journeys = new ArrayList<tblJourney>();

        // get the cursor from the query
        Cursor cursor = database.query(MySQLHelper.TABLE_JOURNEYS,
                allColumns, null, null, null, null, null);

        // loop through
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // create a journey, add it to the list
            tblJourney newJourney = cursorToJourney(cursor);
            journeys.add(newJourney);
            cursor.moveToNext();
        }
        // make sure to close the cursor and return the list
        cursor.close();
        return journeys;
    }

    // deletes a journey from the table
    public void deleteJourney(int ID) {
        database.delete(MySQLHelper.TABLE_JOURNEYS, MySQLHelper.COLUMN_ID + " = " + ID, null);
    }
}
