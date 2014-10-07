package com.example.Journey;

import android.text.format.Time;

import java.util.Timer;

/**
 * Created by Megan on 6/10/2014.
 */
public class tblPoint {

    private String Date;
    private long Lat;
    private long Long;
    private String imgURL;
    private String comment;

    // constructors
    public tblPoint() {
        Lat = 0;
        Long = 0;
        imgURL = "default";
        comment = "default";
        Date = "";
    }
    public tblPoint(String Date, long Lat, long Long)  {
        this.Date = Date;
        this.Lat = Lat;
        this.Long = Long;
        this.imgURL = "default";
        this.comment = "default";
    }

    // getters
    public String getDate() {
        return Date;
    }
    public long getLat() {
        return Lat;
    }
    public long getLong() {
        return Long;
    }
    public String getimgURL() {
        return imgURL;
    }
    public String getComment() {
        return comment;
    }

    // setters
    public void setDate(String Date) {
        this.Date = Date;
    }
    public void setLat(long Lat) {
        this.Lat = Lat;
    }
    public void setLong(long Long) {
        this.Long = Long;
    }
    public void setimgURL(String imgURL) {
        this.imgURL = imgURL;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
