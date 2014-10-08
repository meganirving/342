package com.example.Journey;

/**
 * Created by Megan on 8/10/2014.
 */
public class tblPhoto {
    private String imgURL;
    private String comment;
    private String timeStamp;

    // constructors
    public tblPhoto() {
        imgURL = "default";
        comment = "default";
        timeStamp = "";
    }
    public tblPhoto(String timeStamp, String comment, String imgURL)  {
        this.timeStamp = timeStamp;
        this.imgURL = imgURL;
        this.comment = comment;
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

    public void settimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void setimgURL(String imgURL) {
        this.imgURL = imgURL;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
