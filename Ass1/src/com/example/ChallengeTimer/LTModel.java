package com.example.ChallengeTimer;

import com.example.ChallengeTimer.LTChallenge;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Megan on 26/8/2014.
 */

public class LTModel implements Serializable{

    // FUNCTIONS
    public LTModel()
    {
        challenges = new ArrayList<LTChallenge>();
    }
    // get the amount of challenges
    public int getChallengeAmt()
    {
        return challenges.size();
    }
    // add a challenge to the list
    public void addChallenge(LTChallenge newChallenge)
    {
        challenges.add(newChallenge);
    }
    // retrieve a challenge from the list
    public LTChallenge getChallengeAt(int i)
    {
        return challenges.get(i);
    }
    public ArrayList<LTChallenge> getList() { return challenges; }


    // DATA
    private ArrayList<LTChallenge> challenges;
}
