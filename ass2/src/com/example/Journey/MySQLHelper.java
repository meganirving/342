package com.example.Journey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Megan on 6/10/2014.
 */
public class MySQLHelper extends SQLiteOpenHelper {

    // points
    public static final String TABLE_POINTS = "points";
    public static final String COLUMN_PCOMP = "p_comp";
    public static final String COLUMN_PID = "p_id";
    public static final String COLUMN_PTIME = "p_time";
    public static final String COLUMN_PLAT = "p_lat";
    public static final String COLUMN_PLONG = "p_long";

    // photos
    public static final String TABLE_PHOTOS = "photos";
    public static final String COLUMN_PHID = "ph_id";
    public static final String COLUMN_PHTIME = "ph_time";
    public static final String COLUMN_COMMENT = "ph_comment";
    public static final String COLUMN_URL = "ph_url";

    // journeys
    public static final String TABLE_JOURNEYS = "journeys";
    public static final String COLUMN_JID = "j_id";
    public static final String COLUMN_TITlE = "j_title";
    public static final String COLUMN_DATE = "j_date";

    // journey/point relationship
    public static final String TABLE_JPREL = "JOURNEY_POINTS";
    public static final String COLUMN_JPRID = "jpr_id";

    // journey/photo relationship
    public static final String TABLE_JPHREL = "JOURNEY_PHOTOS";
    public static final String COLUMN_JPHRID = "jphr_id";

    // all columns
    private String[] allJColumns = {MySQLHelper.COLUMN_JID, MySQLHelper.COLUMN_TITlE, MySQLHelper.COLUMN_DATE};
    private String[] allPColumns = {MySQLHelper.COLUMN_PCOMP, MySQLHelper.COLUMN_PTIME, MySQLHelper.COLUMN_PID, MySQLHelper.COLUMN_PLAT, MySQLHelper.COLUMN_PLONG};
    private String[] allJPRColumns = {MySQLHelper.COLUMN_JPRID, MySQLHelper.COLUMN_PTIME, MySQLHelper.COLUMN_JID};
    private String[] allJPHRColumns = {MySQLHelper.COLUMN_JPHRID, MySQLHelper.COLUMN_URL, MySQLHelper.COLUMN_JID};
    private String[] allPHColumns = {MySQLHelper.COLUMN_URL, MySQLHelper.COLUMN_PHID, MySQLHelper.COLUMN_PHTIME, MySQLHelper.COLUMN_COMMENT};

    // database stuff
    private static final String DATABASE_NAME = "journeys.db";
    private static final int DATABASE_VERSION = 22;
    private SQLiteDatabase database;

    // create tables
    private static final String CREATE_TABLE_JOURNEY = "create table " + TABLE_JOURNEYS
            + "(" + COLUMN_JID + " integer primary key, " + COLUMN_TITlE + " text not null, "
            + COLUMN_DATE + " text not null);";
    private static final String CREATE_TABLE_POINT = "create table " + TABLE_POINTS
            + "(" + COLUMN_PCOMP + " text primary key, " + COLUMN_PTIME + " text, " + COLUMN_PID + " integer, "
            + COLUMN_PLAT + " real, " + COLUMN_PLONG + " real)";
    private static final String CREATE_TABLE_JPREL = "create table " + MySQLHelper.TABLE_JPREL
            + "(" + COLUMN_JPRID + " integer primary key autoincrement, " + COLUMN_PTIME + " text, "
            + COLUMN_JID + " integer)";
    private static final String CREATE_TABLE_JPHREL = "create table " + MySQLHelper.TABLE_JPHREL
            + "(" + COLUMN_JPHRID + " integer primary key autoincrement, " + COLUMN_PHID + " integer, "
            + COLUMN_JID + " integer)";
    private static final String CREATE_TABLE_PHOTO = "create table " + TABLE_PHOTOS
            + "(" + COLUMN_URL + " text primary key, " + COLUMN_PHID + " integer, " + COLUMN_COMMENT + " text, "
            + COLUMN_PHTIME + " text not null)";

    // constructor
    public MySQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create the databases
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_JOURNEY);
        db.execSQL(CREATE_TABLE_POINT);
        db.execSQL(CREATE_TABLE_PHOTO);
        db.execSQL(CREATE_TABLE_JPREL);
        db.execSQL(CREATE_TABLE_JPHREL);
    }

    // opening and closing
    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }
    public void closeDB() {
        Log.v("journey", "closed");
        if (database != null && database.isOpen())
            database.close();
    }
    // calls the open function if the database is closed
    public void checkOpen() {
        if (!database.isOpen())
            open();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNEYS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JPREL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JPHREL);
        onCreate(db);
    }

    // translates cursors into journeys/points
    private tblJourney cursorToJourney(Cursor cursor) {
        tblJourney journey = new tblJourney();
        journey.setID(cursor.getInt(0));
        journey.setTitle(cursor.getString(1));
        journey.setDate(cursor.getString(2));
        return journey;
    }
    private tblPoint cursorToPoint(Cursor cursor) {
        tblPoint point = new tblPoint();
        point.setComposite(cursor.getString(0));
        point.settimeStamp(cursor.getString(1));
        point.setID(cursor.getInt(2));
        point.setLat(cursor.getDouble(3));
        point.setLong(cursor.getDouble(4));
        return point;
    }
    private tblPhoto cursorToPhoto(Cursor cursor) {
        tblPhoto photo = new tblPhoto();
        photo.setimgURL(cursor.getString(0));
        photo.setID(cursor.getInt(1));
        photo.setComment(cursor.getString(2));
        photo.settimeStamp(cursor.getString(3));
        return photo;
    }

    // add things to the tables
    public void createJourney(tblJourney journey) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITlE, journey.getTitle());
        values.put(COLUMN_DATE, journey.getDate());

        // go through all the points in this journey
        for (tblPoint point : journey.getPoints()) {
            // add each point and the relationship between it + the journey to the database
            createPoint(point);
            createJPRel(journey, point);
        }
        // go through all the photos in the journey
        for (tblPhoto photo : journey.getPhotos()) {
            // add each photo and also the relationship
            createPhoto(photo);
            createJPHRel(journey, photo);
        }
        // add the journey
        database.insert(TABLE_JOURNEYS, null, values);
    }
    public void createPoint(tblPoint point) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PCOMP, point.getComposite());
        values.put(COLUMN_PTIME, point.gettimeStamp());
        values.put(COLUMN_PID, point.getID());
        values.put(COLUMN_PLAT, point.getLat());
        values.put(COLUMN_PLONG, point.getLong());
        database.insert(TABLE_POINTS, null, values);
    }
    public void createPhoto(tblPhoto photo) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_URL, photo.getimgURL());
        values.put(COLUMN_PHID, photo.getID());
        values.put(COLUMN_COMMENT, photo.getComment());
        values.put(COLUMN_PHTIME, photo.gettimeStamp());
        database.insert(TABLE_PHOTOS, null, values);
    }
    public void createJPRel(tblJourney journey, tblPoint point) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_JID, journey.getID());
        values.put(COLUMN_PTIME, point.gettimeStamp());
        database.insert(TABLE_JPREL, null, values);
    }
    public void createJPHRel(tblJourney journey, tblPhoto photo) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_JID, journey.getID());
        values.put(COLUMN_PHID, photo.getID());
        database.insert(TABLE_JPHREL, null, values);
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
        journey.setDate(c.getString(c.getColumnIndex(COLUMN_DATE)));

        // get all the points and photos
        journey.setPoints(getAllPoints(journey.getID()));
        journey.setPhotos(getAllPhotos(journey.getID()));

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
            // create a journey and its points and photos, add it to the list
            tblJourney newJourney = cursorToJourney(cursor);
            newJourney.setPoints(getAllPoints(newJourney.getID()));
            newJourney.setPhotos(getAllPhotos(newJourney.getID()));
            journeys.add(newJourney);
            cursor.moveToNext();
        }
        // make sure to close the cursor and return the list
        cursor.close();
        return journeys;
    }
    // get all the points for an appropriate journey
    public ArrayList<tblPoint> getAllPoints(int ID) {
        // create query and get results
        String selectQuery = "select * from " + TABLE_POINTS + " tp, " + TABLE_JPREL + " tr where tr." + COLUMN_JID + " = " + ID;
        Cursor cursor = database.rawQuery(selectQuery, null);

        // create the point array
        ArrayList<tblPoint> points = new ArrayList<tblPoint>();

        // loop through and add points to the list
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                // create a point and add it to the list
                tblPoint newPoint = cursorToPoint(cursor);
                points.add(newPoint);
                cursor.moveToNext();
            }
        }

        // close the cursor and return the list
        cursor.close();
        return points;
    }
    // get all the photos from a journey
    public ArrayList<tblPhoto> getAllPhotos(int ID) {
        // create query
        String selectQuery = "select * from " + TABLE_PHOTOS + " tp, " + TABLE_JPHREL + " tr where tr." + COLUMN_JID + " = " + ID;
        Cursor cursor = database.rawQuery(selectQuery, null);

        // create the point array
        ArrayList<tblPhoto> photos = new ArrayList<tblPhoto>();

        // loop through and add points to the list
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // create a point and add it to the list
            tblPhoto newPhoto = cursorToPhoto(cursor);
            photos.add(newPhoto);
            cursor.moveToNext();
        }

        // close the cursor and return the list
        cursor.close();
        return photos;
    }

    // delete elements from tables
    public void deleteJourney(tblJourney journey) {
        // delete all the points, phtoos and relationship first
        for (tblPoint point : journey.getPoints()) {
            deletePoint(point.gettimeStamp());
        }
        for (tblPhoto photo : journey.getPhotos()) {
            deletePhoto(photo.getimgURL());
        }
        deleteRelationships(journey);

        // then delete the journey
        database.delete(MySQLHelper.TABLE_JOURNEYS, MySQLHelper.COLUMN_JID + " = " + journey.getID(), null);
    }
    public void deletePoint(String Time) {
        database.delete(MySQLHelper.TABLE_POINTS, MySQLHelper.COLUMN_PTIME + " = '" + Time + "'", null);
    }
    public void deletePhoto(String URL) {
        database.delete(MySQLHelper.TABLE_PHOTOS, MySQLHelper.COLUMN_URL + " = '" + URL + "'", null);
    }
    public void deleteRelationships(tblJourney journey) {
        database.delete(MySQLHelper.TABLE_JPREL, MySQLHelper.COLUMN_JID + " = " + journey.getID(), null);
        database.delete(MySQLHelper.TABLE_JPHREL, MySQLHelper.COLUMN_JID + " = " + journey.getID(), null);
    }

    // get biggest journey ID
    public int getLastID() {
        // get journey with largest ID
        Cursor cursor = database.query(TABLE_JOURNEYS, new String [] {"MAX("+COLUMN_JID+")"}, null, null, null, null, null);
        if (cursor.getCount() == 1) {
            // if there aren't any, return 0
            cursor.close();
            return 0;
        } else {
            // otherwise, return the id
            tblJourney journey = cursorToJourney(cursor);
            cursor.close();
            return journey.getID();
        }
    }
}