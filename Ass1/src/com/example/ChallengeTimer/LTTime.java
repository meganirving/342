package com.example.ChallengeTimer;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Megan on 26/8/2014.
 */
public class LTTime {

    // FUNCTIONS
    // setters
    public void setComment(String newComment)
    {
        comment = newComment;
    }
    public void setTime(Time newTime)
    {
        time = newTime;
    }
    public void  setDateRecorded(Date newDate)
    {
        dateRecorded = newDate;
    }
    // getters
    public String getComment()
    {
        return comment;
    }
    public Time getTime()
    {
        return time;
    }
    public Date getDate()
    {
        return dateRecorded;
    }


    // DATA
    private String comment;
    private Time time;
    private Date dateRecorded;

}
