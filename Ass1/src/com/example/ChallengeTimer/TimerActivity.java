package com.example.ChallengeTimer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.ChallengeTimer.TimerFragment;
import com.example.ChallengeTimer.ChallengeFragment;
import com.example.ChallengeTimer.TimesFragment;

/**
 * Created by Megan on 26/8/2014.
 */
public class TimerActivity extends Activity
        implements ChallengeFragment.ChallengeListener, TimerFragment.TimerListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // inflate timerfragment & add to container
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();

        ChallengeFragment challengeFragment = new ChallengeFragment();
        fragTransaction.add(R.id.frame, challengeFragment);
        fragTransaction.commit();
    }

    // implementing listeners
    @Override
    public void selectChallenge(LTChallenge challenge) {

        // create new fragment and set its challenge
        TimerFragment timerFragment = new TimerFragment();
        timerFragment.setChallenge(challenge);

        // create transaction and swap new fragment in
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, timerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void buttonPressed(LTChallenge challenge)
    {
        // create new fragment and set its challenge
        TimesFragment timesFragment = new TimesFragment();
        timesFragment.setChallenge(challenge);

        // create transaction and swap new fragment in
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, timesFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
