package com.example.ChallengeTimer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import com.example.ChallengeTimer.TimerFragment;
import com.example.ChallengeTimer.ChallengeFragment;
import com.example.ChallengeTimer.TimesFragment;

import java.io.*;

/**
 * Created by Megan on 26/8/2014.
 */
public class TimerActivity extends Activity
        implements ChallengeFragment.ChallengeListener, TimerFragment.TimerListener {

    private LTModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // load model, otherwise create model
        model = LoadModel();
        if (model == null) {
            model = new LTModel();
        }
        else
            Log.v("test", "definitely loaded??");

        // inflate timerfragment & add to container
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();

        // create fragment and set model
        ChallengeFragment challengeFragment = new ChallengeFragment();
        challengeFragment.setModel(model);
        fragTransaction.add(R.id.frame, challengeFragment);
        fragTransaction.commit();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        // save model
       try
        {
            FileOutputStream ofile = getApplicationContext().openFileOutput("save.bin", Context.MODE_PRIVATE);
            ObjectOutputStream ostream = new ObjectOutputStream(ofile);
            ostream.writeObject(model);
            //ostream.flush();
            ostream.close();
            Log.v("test", "saved??");
        }
        catch(Exception ex)
        {
            Log.v("Saving Error: ",ex.getMessage());
            ex.printStackTrace();
        }
    }
   public LTModel LoadModel()
    {
        try
        {
            FileInputStream ifile = getApplicationContext().openFileInput("save.bin");
            ObjectInputStream istream = new ObjectInputStream(ifile);
            model = (LTModel)istream.readObject();
            Log.v("test", model.getList().toString());
            return model;
        }
        catch(Exception ex) {
            Log.v("Loading Error: ", ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public  void onStart()
    {
        super.onStart();

        model = LoadModel();
        if (model == null)
            model = new LTModel();
    }

    // implementing listeners
    @Override
    public void selectChallenge(LTModel newModel, int newID) {

        // update model
        model = newModel;

        // create new fragment and set its challenge
        TimerFragment timerFragment = new TimerFragment();
        timerFragment.setChallenge(model, newID);

        // create transaction and swap new fragment in
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, timerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void updateModel(LTModel newModel)
    {
        model = newModel;
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
