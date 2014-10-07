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

    // points
    public static final String TABLE_POINTS = "points";
    public static final String COLUMN_PID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_URL = "url";

    // journeys
    public static final String TABLE_JOURNEYS = "journeys";
    public static final String COLUMN_JID = "_id";
    public static final String COLUMN_TITlE = "title";

    // relationship
    public static final String TABLE_REL = "JOURNEY_POINTS";
    public static final String COLUMN_RID = "_id";

    // all columns
    private String[] allJColumns = {MySQLHelper.COLUMN_JID, MySQLHelper.COLUMN_TITlE};
    private String[] allPColumns = {MySQLHelper.COLUMN_PID, MySQLHelper.COLUMN_DATE, MySQLHelper.COLUMN_COMMENT, MySQLHelper.COLUMN_URL};
    private String[] allRColumns = {MySQLHelper.COLUMN_RID, MySQLHelper.COLUMN_PID, MySQLHelper.COLUMN_JID};

    // database stuff
    private static final String DATABASE_NAME = "journeys.db";
    private static final int DATABASE_VERSION = 2;
    private SQLiteDatabase database;

    // create tables
    private static final String CREATE_TABLE_JOURNEY = "create table " + TABLE_JOURNEYS
            + "(" + COLUMN_JID + " integer primary key autoincrement, " + COLUMN_TITlE + " text not null);";
    private static final String CREATE_TABLE_POINT = "create table " + TABLE_POINTS
            + "(" + COLUMN_PID + " integer primary key, " + COLUMN_COMMENT + " text, "
            + COLUMN_URL + " text not null, " + COLUMN_DATE + " text not null)";
    private static final String CREATE_TABLE_REL = "create table " + MySQLHelper.TABLE_REL
            + "(" + COLUMN_RID + " integer primary key autoincrement, " + COLUMN_PID + " integer, "
            + COLUMN_JID + " integer)";

    // constructor
    public MySQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create the databases
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_JOURNEY);
        db.execSQL(CREATE_TABLE_POINT);
        db.execSQL(CREATE_TABLE_REL);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REL);
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
        //int ID = cursor.getInt(0);
        point.setComment(cursor.getString(1));
        point.setimgURL(cursor.getString(2));
        point.setDate(cursor.getString(3));
        //point.setLat(cursor.getLong(1));
        //point.setLong(cursor.getLong(2));
        return point;
    }

    // adds a journey to the table
    public void createJourney(tblJourney journey) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITlE, journey.getTitle());
    }
    public void createPoint(tblPoint point) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMMENT, point.getComment());
        values.put(COLUMN_URL, point.getimgURL());
        values.put(COLUMN_DATE, point.getDate());
    }

    // gets journey/s from the table
    public tblJourney getJourney(int ID){
        // make the query
        String selectQuery = "SELECT  * FROM " + TABLE_JOURNEYS + " WHERE "
                + COLUMN_JID + " = " + ID;

        // get the cursor
        Cursor c = database.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();

        // get journey
        tblJourney journey = new tblJourney();
        journey.setID(c.getInt(c.getColumnIndex(COLUMN_JID)));
        journey.setTitle(c.getString(c.getColumnIndex(COLUMN_TITlE)));

        // get all the points
        journey.setPoints(getAllPoints(journey.getID()));

        return journey;
    }
    public ArrayList<tblJourney> getAllJourneys() {
        ArrayList<tblJourney> journeys = new ArrayList<tblJourney>();

        // get the cursor from the query
        Cursor cursor = database.query(MySQLHelper.TABLE_JOURNEYS,
                allJColumns, null, null, null, null, null);

        // loop through
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // create a journey and its points, add it to the list
            tblJourney newJourney = cursorToJourney(cursor);
            newJourney.setPoints(getAllPoints(newJourney.getID()));
            journeys.add(newJourney);
            cursor.moveToNext();
        }
        // make sure to close the cursor and return the list
        cursor.close();
        return journeys;
    }

    // get point/s from the table
    public tblPoint getPoint(int ID){
        // make the query
        String selectQuery = "SELECT  * FROM " + TABLE_POINTS + " WHERE "
                + COLUMN_PID + " = " + ID;

        // get the cursor
        Cursor c = database.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();

        // get journey
        tblPoint point = new tblPoint();
        point.setimgURL(c.getString(c.getColumnIndex(COLUMN_URL)));
        point.setComment(c.getString(c.getColumnIndex(COLUMN_COMMENT)));
        point.setDate(c.getString(c.getColumnIndex(COLUMN_DATE)));
        return point;
    }
    // get all the points for an appropriate journey
    public ArrayList<tblPoint> getAllPoints(int ID) {

        // create query
        String selectQuery = "select * from " + TABLE_POINTS + " tp, " + TABLE_REL + " tr where tr." + COLUMN_JID + " = " + ID;

        // create the point array
        ArrayList<tblPoint> points = new ArrayList<tblPoint>();

        // get the cursor from the query
        Cursor cursor = database.query(MySQLHelper.TABLE_JOURNEYS,
                allJColumns, null, null, null, null, null);

        // loop through and add points to the list
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // create a point and add it to the list
            tblPoint newPoint = cursorToPoint(cursor);
            points.add(newPoint);
            cursor.moveToNext();
        }

        // close the cursor and return the list
        cursor.close();
        return points;
    }

    // delete elements from tables
    public void deleteJourney(int ID) {
        database.delete(MySQLHelper.TABLE_JOURNEYS, MySQLHelper.COLUMN_JID + " = " + ID, null);
    }
    public void deletePoint(int ID) {
        database.delete(MySQLHelper.TABLE_POINTS, MySQLHelper.COLUMN_PID + " = " + ID, null);
    }

}
