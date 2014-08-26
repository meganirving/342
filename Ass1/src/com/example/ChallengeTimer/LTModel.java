package com.example.ChallengeTimer;

import com.example.ChallengeTimer.LTChallenge;

import java.util.ArrayList;

/**
 * Created by Megan on 26/8/2014.
 */

public class LTModel {

    // FUNCTIONS
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


    // DATA
    private ArrayList<LTChallenge> challenges;
}
