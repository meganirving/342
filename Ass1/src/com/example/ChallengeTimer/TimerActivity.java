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
public class TimerActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // inflate timerfragment & add to container
        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();

        TimerFragment timerFragment = new TimerFragment();
        fragTransaction.add(R.id.frame, timerFragment);
        fragTransaction.commit();
    }
}