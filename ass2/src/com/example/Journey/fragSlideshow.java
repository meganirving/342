package com.example.Journey;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Megan on 29/9/2014.
 */
public class fragSlideshow extends Fragment {

    View root;
    ArrayList<tblJourney> journeys;
    LinearLayout frameOptions;
    FrameLayout frameBtn;
    RelativeLayout frameShow;
    Button btnStart;
    Button btnStop;
    Button btnResume;
    TextView comment;
    ViewFlipper flipper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.slideshow, container, false);

        // get UI elements
        frameOptions = (LinearLayout) root.findViewById(R.id.frameOptions);
        frameBtn = (FrameLayout) root.findViewById(R.id.frameBtn);
        frameShow = (RelativeLayout) root.findViewById(R.id.frameShow);
        btnStart = (Button) root.findViewById(R.id.btnStart);
        btnStop = (Button) root.findViewById(R.id.butStop);
        btnResume = (Button) root.findViewById(R.id.btnResume);
        comment = (TextView) root.findViewById(R.id.comment);
        flipper = (ViewFlipper) root.findViewById(R.id.flipper);

        // create duration list
        ArrayList<Integer> durations = new ArrayList<Integer>();
        durations.add(new Integer(1));
        durations.add(new Integer(2));
        durations.add(new Integer(3));
        durations.add(new Integer(4));
        durations.add(new Integer(5));

        // create title list and add the first "all" element
        ArrayList<String> journeyList = new ArrayList<String>();
        journeyList.add("All Journeys");

        // fill journey list
        actJourney activity = (actJourney)getActivity();
        journeys = activity.getMySql().getAllJourneys();

        // fill title list
        for (tblJourney journey : journeys) {
            journeyList.add(journey.getTitle());
        }

        // get the spinners
        final Spinner spinJourn = (Spinner) root.findViewById(R.id.spinJourney);
        final Spinner spinNum = (Spinner) root.findViewById(R.id.spinTime);

        // Create an ArrayAdapters
        ArrayAdapter<String> adaptJourn = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, journeyList);
        ArrayAdapter<Integer> adaptNum = new ArrayAdapter<Integer>(activity, android.R.layout.simple_spinner_item, durations);
        // Specify layouts
        adaptJourn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaptNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapters to the spinners
        spinJourn.setAdapter(adaptJourn);
        spinNum.setAdapter(adaptNum);

        // start button
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get currently selected values
                Integer secs = (Integer)spinNum.getSelectedItem();
                int index = spinJourn.getSelectedItemPosition();
                // if "all journeys" was selected
                if (index == 0) {
                    // reset flipper and add all the pictures from all the journeys
                    clearFlipper();
                    for (tblJourney journey : journeys) {
                        addToFlipper(journey);
                    }
                } else {
                    // otherwise just reset and add this journey
                    clearFlipper();
                    addToFlipper(journeys.get( index + 1 ));
                }

                // change views
                frameBtn.setVisibility(View.GONE);
                frameOptions.setVisibility(View.GONE);
                frameShow.setVisibility(View.VISIBLE);
                // hide comment and control buttons for now
                comment.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);

                // start flipping
                flipper.setAutoStart(true);
                flipper.setFlipInterval(secs * 1000);
                flipper.startFlipping();
            }
        });

        // tap the flipper
        flipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // pause
                flipper.stopFlipping();
                // show the control buttons
                btnResume.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);
                return false;
            }
        });
        // resume button
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // resume
                flipper.startFlipping();
                // just hide the control buttons again
                btnResume.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
            }
        });
        // stop button
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change views
                frameShow.setVisibility(View.GONE);
                frameBtn.setVisibility(View.VISIBLE);
                frameOptions.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

    public void addToFlipper(tblJourney journey) {
        // loop through photos
        for (tblPhoto photo : journey.getPhotos()) {
            ImageView imageView = new ImageView(getActivity());
            // TODO: add photo to imageview
            flipper.addView(imageView);
        }
    }
    public void clearFlipper() {
        flipper.removeAllViews();
    }
}