package com.example.ChallengeTimer;

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

import java.sql.Time;
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
    public LTChallenge challenge;
    public Button button;
    public TextView label;
    public TextView bTime;
    public TextView wTime;
    public TextView aTime;
    public TextView cTime;
    public LinearLayout best;
    public LinearLayout worst;
    public LinearLayout average;
    public LinearLayout current;
    public buttonStates currState;
    public Timer timer;
    public long startTime;
    public long currTime;
    public EditText input;

    public void updateTimes()
    {
        LTTime Best = challenge.getTime(0);
        LTTime Worst = challenge.getTime(1);
        // TODO get average time
        LinearLayout.LayoutParams lParams;

        // update labels
        bTime.setText("Best Time: " + LTTime.convertToString(Best));
        wTime.setText("Worst Time: " + LTTime.convertToString(Worst));
        cTime.setText("Current Time: ");
        // TODO average text

        // update worst graph
        lParams = (LinearLayout.LayoutParams) worst.getLayoutParams();
        lParams.weight = 1;
        worst.requestLayout();
        // update best graph
        lParams = (LinearLayout.LayoutParams) best.getLayoutParams();
        lParams.weight = (Best.getTime() / Worst.getTime());
        best.requestLayout();
        lParams = (LinearLayout.LayoutParams) current.getLayoutParams();
        lParams.weight = 0.0f;
        // update current graph
        current.requestLayout();

        // TODO average graph
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.timer, container, false);

        // init data
        challenge = new LTChallenge();
        challenge.init();
        currState = buttonStates.start;
        startTime = 0;
        currTime = 0;
        label = (TextView) rootView.findViewById(R.id.timer);
        best = (LinearLayout) rootView.findViewById(R.id.bestgraph);
        bTime = (TextView) rootView.findViewById(R.id.bestlabel);
        worst = (LinearLayout) rootView.findViewById(R.id.worstgraph);
        wTime = (TextView) rootView.findViewById(R.id.worstlabel);
        average = (LinearLayout) rootView.findViewById(R.id.avgraph);
        aTime = (TextView) rootView.findViewById(R.id.avLabel);
        current = (LinearLayout) rootView.findViewById(R.id.curr);

        // update (in this case, set) the time graphs
        //updateTimes();

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
                newTime.setComment(input.getText().toString());
                challenge.addChallenge(newTime);
            }});
        // cancel button
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { /* do absolutely nothing :)*/ }});
        // create the final alert
        final AlertDialog alert = alertBuilder.create();

        // set up button
        button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // button says "start"
                if (currState == buttonStates.start)
                {
                    // start timer
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
                   // updateTimes();
                }
            }
        });

        return rootView;
    }

    private Runnable update = new Runnable() {
        public void run() {
            // get current time
            currTime = SystemClock.currentThreadTimeMillis() - startTime;

            // update text
            label.setText(LTTime.convertToString(currTime));

            // update graphs
           /* LinearLayout.LayoutParams lParams;
            LTTime Worst = challenge.getTime(1);
            // if the current time is still better than the worst time
            if (currTime < Worst.getTime())
            {
                // just update the current time's graph
                lParams = (LinearLayout.LayoutParams) current.getLayoutParams();
                lParams.weight = (currTime / Worst.getTime());
                current.requestLayout();
            }
            // if the current time is the new worst time
            else
            {
                // get the best and average times
                LTTime Best = challenge.getTime(0);
                // TODO average time

                // update curr graph
                lParams = (LinearLayout.LayoutParams) current.getLayoutParams();
                lParams.weight = 1;
                current.requestLayout();
                // update "worst" graph
                lParams = (LinearLayout.LayoutParams) worst.getLayoutParams();
                lParams.weight = (Worst.getTime() / currTime);
                worst.requestLayout();
                // update best graph
                lParams = (LinearLayout.LayoutParams) best.getLayoutParams();
                lParams.weight = (Best.getTime() / currTime);
                best.requestLayout();
                // TODO update average graph
            }*/
        }
    };
}