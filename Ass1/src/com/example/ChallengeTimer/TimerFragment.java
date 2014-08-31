package com.example.ChallengeTimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.ChallengeTimer.LTChallenge;
import org.w3c.dom.Text;

import java.lang.reflect.Modifier;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Megan on 26/8/2014.
 */
public class TimerFragment extends Fragment {

    // states for the button
    public enum buttonStates
    {
        start,
        stop,
        clear
    };

    // data
    private LTModel model;
    private int challengeID;
    private LTChallenge challenge;
    private Button button;
    private Button otherButton;
    private TextView label;
    private TextView bTime;
    private TextView wTime;
    private TextView aTime;
    private TextView cTime;
    private LinearLayout best;
    private LinearLayout worst;
    private LinearLayout average;
    private LinearLayout current;
    private buttonStates currState;
    private Timer timer;
    private long startTime;
    private long currTime;
    private EditText input;
    private LTTime bestTime;
    private LTTime worstTime;
    private long avgTime;

    // overload onAttach
    public void onAttach(Activity activity)
    {
        // call the regular one
        super.onAttach(activity);
        // set up the listener
        listener = (TimerListener)activity;
    }

    public void setChallenge(LTModel newModel, int newID)
    {
        // need to keep track of the whole model for the parent activity
        model = newModel;
        challengeID = newID;
        // keep track of the current challenge to speed up processing in this fragment
        challenge = model.getChallengeAt(challengeID);
    }

    public void updateTimes()
    {
        // only bother with this if there is at least one time to display
        if (challenge.getTimeAmt() > 0)
        {
            LinearLayout.LayoutParams lParams;

            bestTime = challenge.getTime(0);
            worstTime = challenge.getTime(1);
            avgTime = challenge.getAvgTime();

            // update labels
            bTime.setText("Best Time: " + bestTime.toString());
            wTime.setText("Worst Time: " + worstTime.toString());
            cTime.setText("Current Time: 00:00.0");
            aTime.setText("Average Time: " + LTTime.convertToString(avgTime));

            // update worst graph
            lParams = (LinearLayout.LayoutParams) worst.getLayoutParams();
            lParams.weight = 1.0f;
            worst.setLayoutParams(lParams);
            // update best graph
            lParams = (LinearLayout.LayoutParams) best.getLayoutParams();
            lParams.weight = (float)bestTime.getTime() / (float)worstTime.getTime();
            best.setLayoutParams(lParams);
            lParams = (LinearLayout.LayoutParams) average.getLayoutParams();
            lParams.weight = (float)avgTime / (float)worstTime.getTime();
            average.setLayoutParams(lParams);

            // update current graph
            lParams = (LinearLayout.LayoutParams) current.getLayoutParams();
            lParams.weight = 0.0f;
            current.setLayoutParams(lParams);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.timer, container, false);

        // enums
        currState = buttonStates.start;
        // time data for later
        startTime = 0;
        currTime = 0;
        // labels
        label = (TextView) rootView.findViewById(R.id.timer);
        bTime = (TextView) rootView.findViewById(R.id.bestlabel);
        wTime = (TextView) rootView.findViewById(R.id.worstlabel);
        aTime = (TextView) rootView.findViewById(R.id.avLabel);
        cTime = (TextView) rootView.findViewById(R.id.currLabel);
        // graphs
        best = (LinearLayout) rootView.findViewById(R.id.bestgraph);
        worst = (LinearLayout) rootView.findViewById(R.id.worstgraph);
        average = (LinearLayout) rootView.findViewById(R.id.avgraph);
        current = (LinearLayout) rootView.findViewById(R.id.curr);

        // update (in this case, set) the time graphs
        updateTimes();

        // create alert dialogue
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        // set a title and a message
        alertBuilder.setTitle("Save Time?");
        alertBuilder.setMessage("Message (Optional)");
        // create an editText and add it to the alert
        input = new EditText(getActivity());
        alertBuilder.setView(input);
        // save button
        alertBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // save the current time
                LTTime newTime = new LTTime();
                newTime.setTime(currTime);
                newTime.setDateRecorded(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                newTime.setComment(input.getText().toString());
                // update the model here
                model.getChallengeAt(challengeID).addChallenge(newTime);
                // pass it back to the parent activity
                listener.updateModel(model);
                // update the temp challenge
                challenge = model.getChallengeAt(challengeID);
            }});
        // cancel button
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { /* do absolutely nothing :)*/ }});
        // create the final alert
        final AlertDialog alert = alertBuilder.create();

        // set up main button
        button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // button says "start"
                if (currState == buttonStates.start)
                {
                    // start timer
                    startTimer(); // set current bar to full, if there are no other times
                    startTime = SystemClock.currentThreadTimeMillis();
                    timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(update);
                        }
                    }, 0, 10);

                    // set button text to "stop"
                    button.setText("Stop");
                    currState = buttonStates.stop;
                }
                // button says "stop"
                else if (currState == buttonStates.stop)
                {
                        // stop timer
                        timer.cancel();
                        // set button to "reset"
                        button.setText("Reset");
                        currState = buttonStates.clear;
                        // pop-up that asks for an optional comment and saves data
                        alert.show();
                }
                // button says "clear"
                else
                {
                    // timer goes back to 00:00.0
                    label.setText("00:00.0");
                    // button says "start"
                    button.setText("Start");
                    currState = buttonStates.start;
                    // graphs are updated
                    updateTimes();
                }
            }
        });
        // set up other button
        otherButton = (Button) rootView.findViewById(R.id.all);
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tell the activity what challenge we're in
                listener.buttonPressed(challenge);
            }
        });

        return rootView;
    }

    public void startTimer()
    {
        // if there are no times yet
        if (challenge.getTimeAmt() == 0)
        {
            LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams)current.getLayoutParams();
            lParams.weight = 1.0f;
            current.setLayoutParams(lParams);
        }
    }


    private Runnable update = new Runnable() {
        public void run()
        {
            // get current time
            currTime = SystemClock.currentThreadTimeMillis() - startTime;

            // update text
            String newTime = LTTime.convertToString(currTime);
            label.setText(newTime);
            cTime.setText("Current Time: " + newTime);

            // if there's at least one time
            if (challenge.getTimeAmt() > 0)
            {
                // update graphs
                LinearLayout.LayoutParams lParams;
                // if the current time is still better than the worst time
                if (currTime < worstTime.getTime())
                {
                    // just update the current time's graph
                    lParams = (LinearLayout.LayoutParams) current.getLayoutParams();
                    lParams.weight = (float)currTime / (float)worstTime.getTime();
                    current.setLayoutParams(lParams);
                }
                // if the current time is the new worst time
                else
                {
                    // update curr graph
                    lParams = (LinearLayout.LayoutParams) current.getLayoutParams();
                    lParams.weight = 1;
                    current.setLayoutParams(lParams);
                    // update "worst" graph
                    lParams = (LinearLayout.LayoutParams) worst.getLayoutParams();
                    lParams.weight = (float)worstTime.getTime() / (float)currTime;
                    worst.setLayoutParams(lParams);
                    // update best graph
                    lParams = (LinearLayout.LayoutParams) best.getLayoutParams();
                    lParams.weight = (float)bestTime.getTime() / (float)currTime;
                    best.setLayoutParams(lParams);
                    // update average graph
                    lParams = (LinearLayout.LayoutParams) average.getLayoutParams();
                    lParams.weight = (float)avgTime / (float)currTime;
                    average.setLayoutParams(lParams);
                }
            }
        }
    };

    public interface TimerListener{
        void buttonPressed(LTChallenge newChallenge);
        void updateModel(LTModel newModel);
    }
    private TimerListener listener;
}