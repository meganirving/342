package com.example.Yo2;

import java.io.Serializable;

/**
 * Created by Megan on 5/9/2014.
 */
public class YoCatch extends Object implements Serializable{

    private String Name;
    private String YoMsg;

    public String getName()
    {
        return Name;
    }
    public String getMsg()
    {
        return YoMsg;
    }

    // constructors
    public YoCatch(String name, String msg)
    {
        this.Name = name;
        this.YoMsg = msg;
    }

    public static String defaultYoMsg() {
        return "Yo ";
    }
}
