package com.example.ChallengeTimer;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Megan on 26/8/2014.
 */
public class LTTime {

    // FUNCTIONS
    // setters
    public void init()
    {
        comment = "";
    }
    public void setComment(String newComment)
    {
        comment = newComment;
    }
    public void setTime(long newTime)
    {
        time = newTime;
    }
    public void setDateRecorded(Date newDate)
    {
        dateRecorded = newDate;
    }
    // getters
    public String getComment()
    {
        return comment;
    }
    public long getTime()
    {
        return time;
    }
    public Date getDate()
    {
        return dateRecorded;
    }
    public String toString()
    {
        String temp = LTTime.convertToString(time);
        if (!comment.isEmpty())
        {
            temp += " (" + comment + ")";
        }

        return temp;
    }

    // compare two "times"
    // returns 0 if a < b, 1 if a > b, and -1 if a == b
    static public int Compare(LTTime time1, LTTime time2)
    {
        long t1 = time1.getTime();
        long t2 = time2.getTime();

        if (t1 < t2)
            return 0;
        else if (t1 > t2)
            return 1;
        else
            return -1;
    }
    // converts LTTime to a formatted string
    static public String convertToString(LTTime time)
    {
        // start with the time
        String temp = convertToString(time.getTime());
        // if there's a comment, add it
        if (!time.getComment().isEmpty())
            temp = temp + time.getComment();
        // and return
        return temp;
    }
    // converts milliseconds to a formatted string
    static public String convertToString(long time)
    {
        int secs = (int)(time / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int tens = (int) (time / 100) % 10;

        String temp = String.format("%02d", mins) + ":" + String.format("%02d", secs) + "." + tens;
        return temp;
    }

    // DATA
    private String comment;
    private long time;
    private Date dateRecorded;

}
