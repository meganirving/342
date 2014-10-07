package com.example.Journey;

import android.text.format.Time;

import java.util.Timer;

/**
 * Created by Megan on 6/10/2014.
 */
public class tblPoint {

    private String timeStamp;
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
        timeStamp = "";
    }
    public tblPoint(String timeStamp, long Lat, long Long)  {
        this.timeStamp = timeStamp;
        this.Lat = Lat;
        this.Long = Long;
        this.imgURL = "default";
        this.comment = "default";
    }

    // getters
    public String gettimeStamp() {
        return timeStamp;
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
    public void settimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
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
