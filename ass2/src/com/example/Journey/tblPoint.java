package com.example.Journey;

import android.text.format.Time;

import java.util.Timer;

/**
 * Created by Megan on 6/10/2014.
 */
public class tblPoint {

    private String timeStamp;
    private double Lat;
    private double Long;

    // constructors
    public tblPoint() {
        Lat = 0;
        Long = 0;
        timeStamp = "";
    }
    public tblPoint(String timeStamp, double Lat, double Long)  {
        this.timeStamp = timeStamp;
        this.Lat = Lat;
        this.Long = Long;
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

}
