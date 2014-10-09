package com.example.Journey;

/**
 * Created by Megan on 8/10/2014.
 */
public class tblPhoto {
    private int ID;
    private String imgURL;
    private String comment;
    private String timeStamp;

    // constructors
    public tblPhoto() {
        imgURL = "default";
        comment = "default";
        timeStamp = "";
        ID = 0;
    }
    public tblPhoto(String timeStamp, String comment, String imgURL, int ID)  {
        this.timeStamp = timeStamp;
        this.imgURL = imgURL;
        this.comment = comment;
        this.ID = ID;
    }

    public String gettimeStamp() {
        return timeStamp;
    }
    public String getimgURL() {
        return imgURL;
    }
    public String getComment() {
        return comment;
    }
    public int getID() {
        return ID;
    }

    public void settimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void setimgURL(String imgURL) {
        this.imgURL = imgURL;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
}
