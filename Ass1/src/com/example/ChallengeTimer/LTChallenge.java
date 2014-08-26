package com.example.ChallengeTimer;

import com.example.ChallengeTimer.LTTime;

import java.util.ArrayList;

/**
 * Created by Megan on 26/8/2014.
 */
public class LTChallenge {

    // FUNCTIONS
    // get the amount of time
    public int getTimeAmt()
    {
        return times.size();
    }
    // add a time to the list
    public void addChallenge(LTTime newTime)
    {
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
    // get name
    public String getName()
    {
        return name;
    }


    // DATA
    private String name;
    private ArrayList<LTTime> times;

}
