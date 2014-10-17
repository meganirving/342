package com.example.Yo2;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Megan on 5/9/2014.
 */

public class YoCatch extends Object implements Serializable{

    private String Name;
    private String YoMsg;
    private String imgURL;
    transient private Bitmap Img;
    private String audioURL;

    public String getName()
    {
        return Name;
    }
    public String getMsg()
    {
        return YoMsg;
    }
    public Bitmap getImg()
    {
        return Img;
    }
    public String getImgURL()
    {
        return imgURL;
    }
    public String getAudioURL()
    {
        return audioURL;
    }

    public void setName(String name)
    {
        Name = name;
    }
    public void setMsg(String msg)
    {
        YoMsg = msg;
    }
    public void setImg(Bitmap img)
    {
        Img = img;
    }
    public void setImgURL(String url)
    {
        imgURL = url;
    }
    public void setAudioURL(String url)
    {
        audioURL = url;
    }

    // constructors
    public YoCatch()
    {
        this.Name = "";
        this.YoMsg = "";
        this.audioURL = "";
    }

    public static String defaultYoMsg() {
        return "Yo ";
    }
}
