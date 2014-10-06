package com.example.Journey;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Megan on 6/10/2014.
 */
public class tblJourney {
    private int ID;
    private String title;
    //private ArrayList<tblPoint> points;

    // constructors
    public tblJourney() {
        ID = 0;
        title = "untitled";
        //points = new ArrayList<tblPoint>();
    }
    public tblJourney(String title, /*ArrayList<tblPoint> points,*/ int ID) {
        this.ID = ID;
        this.title = title;
        //this.points = points;
    }

    // getters
    public int getID() {
        return ID;
    }
    public String getTitle() {
        return title;
    }
   /* public ArrayList<tblPoint> getPoints() {
        return points;
    }*/

    // setters
    public void setID(int ID) {
        this.ID = ID;
    }
   /* public void setPoints(ArrayList<tblPoint> points) {
        this.points = points;
    }*/
    public void setTitle(String title)  {
        this.title = title;
    }

    // add new point
   /* public void addPoint(tblPoint newPoint) {
        points.add(newPoint);
    }*/
}
