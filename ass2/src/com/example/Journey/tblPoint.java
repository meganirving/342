package com.example.Journey;

import android.text.format.Time;

import java.util.Timer;

/**
 * Created by Megan on 6/10/2014.
 */
public class tblPoint {
    private int ID;
    private String timeStamp;
    private String composite;
    private double Lat;
    private double Long;

    // constructors
    public tblPoint() {
        Lat = 0;
        Long = 0;
        timeStamp = "";
        ID = 0;
        composite = "";
    }
    public tblPoint(String timeStamp, double Lat, double Long, int ID, String composite)  {
        this.timeStamp = timeStamp;
        this.Lat = Lat;
        this.Long = Long;
        this.ID = ID;
        this.composite = composite;
    }

    // getters
    public String gettimeStamp() {
        return timeStamp;
    }
    public double getLat() {
        return Lat;
    }
    public double getLong() {
        return Long;
    }
    public int getID() {
        return ID;
    }
    public String getComposite() {
        return composite;
    }

    // setters
    public void settimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void setLat(double Lat) {
        this.Lat = Lat;
    }
    public void setLong(double Long) {
        this.Long = Long;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public void setComposite(String composite) {
        this.composite = composite;
    }

}
