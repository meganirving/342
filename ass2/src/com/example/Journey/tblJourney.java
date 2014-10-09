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
    private ArrayList<tblPhoto> photos;

    // constructors
    public tblJourney() {
        ID = 0;
        title = "untitled";
        points = new ArrayList<tblPoint>();
        photos = new ArrayList<tblPhoto>();
    }
    public tblJourney(String title, String date, ArrayList<tblPoint> points, ArrayList<tblPhoto> photos, int ID) {
        this.ID = ID;
        this.title = title;
        this.points = points;
        this.date = date;
        this.photos = photos;
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
    public ArrayList<tblPhoto> getPhotos() {
        return photos;
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
    public void setPhotos(ArrayList<tblPhoto> photos) {
        this.photos = photos;
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
    // add new photo
    public void addPhoto(tblPhoto newPhoto) {
        photos.add(newPhoto);
    }

    // get biggest photo ID
    public int nextPhotoID() {
        // default value
        int largest = 0;
        // loop through
        for (tblPhoto photo : photos) {
            // get new biggest value
            if (photo.getID() > largest) {
                largest = photo.getID();
            }
        }
        return largest+1;
    }
    public int nextPointID() {
        // default value
        int largest = 0;
        // loop through
        for (tblPoint point : points) {
            // get new biggest value
            if (point.getID() > largest) {
                largest = point.getID();
            }
        }
        return largest+1;
    }
}
