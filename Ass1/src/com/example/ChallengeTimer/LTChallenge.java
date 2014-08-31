package com.example.ChallengeTimer;

import android.text.format.Time;
import com.example.ChallengeTimer.LTTime;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Megan on 26/8/2014.
 */
public class LTChallenge implements Serializable{

    // FUNCTIONS
    public LTChallenge()
    {
        times = new ArrayList<LTTime>();
    }
    public LTChallenge(String newName)
    {
        name = newName;
        times = new ArrayList<LTTime>();
    }
    public void init()
    {
        times = new ArrayList<LTTime>();
    }
    // get the amount of time
    public int getTimeAmt()
    {
        return times.size();
    }
    // add a time to the list
    public ArrayList<LTTime> getTimes()
    {
        return times;
    }
    public void addChallenge(LTTime newTime)
    {
        if (newTime != null)
            times.add(newTime);
    }
    // retrieve a challenge from the list
    public LTTime getTimeAt(int i)
    {
        return times.get(i);
    }
    // set name
    public void setName(String newName)
    {
        name = newName;
    }
    public String toString() { return name; }
    // get name
    public String getName()
    {
        return name;
    }
    // get the average time of the list of times
    public long getAvgTime()
    {
        long time = 0;

        // add all the times
        for (int i = 0; i < times.size(); i++)
        {
            time += times.get(i).getTime();
        }

        // divide by amount of times
        time /= times.size();
        return time;
    }
    // get times (best = 0, worst = 1)
    public LTTime getTime(int type)
    {
        // get first time
        LTTime temp = times.get(0);

        // loop through to get best or worst time
        for (int i = 0; i < times.size(); i++)
        {
            // if found a new best time
            if (type == 0 && LTTime.Compare(times.get(i), temp) == 0)
            {
                temp = times.get(i);
            }
            // if found a new worst time
            else if (type == 1 && LTTime.Compare(times.get(i), temp) == 1)
            {
                temp = times.get(i);
            }

            // idk about average
        }

        // return the best or worst time
        return temp;
    }

    // DATA
    private String name;
    private ArrayList<LTTime> times;

}
