package com.example.Journey;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Megan on 6/10/2014.
 */
public class tblJourney {
    private String date;
    private int ID;
    private String title;
    private ArrayList<tblPoint> points;

    // constructors
    public tblJourney() {
        ID = 0;
        title = "untitled";
        points = new ArrayList<tblPoint>();
    }
    public tblJourney(String title, String date, ArrayList<tblPoint> points, int ID) {
        this.ID = ID;
        this.title = title;
        this.points = points;
        this.date = date;
    }

    // getters
    public int getID() {
        return ID;
    }
    public String getTitle() {
        return title;
    }
    public ArrayList<tblPoint> getPoints() {
        return points;
    }
    public String getDate() {
        return date;
    }

    // setters
    public void setID(int ID) {
        this.ID = ID;
    }
    public void setPoints(ArrayList<tblPoint> points) {
        this.points = points;
    }
    public void setTitle(String title)  {
        this.title = title;
    }
    public void setDate(String date) {
        this.date = date;
    }

    // add new point
    public void addPoint(tblPoint newPoint) {
        points.add(newPoint);
    }
}
