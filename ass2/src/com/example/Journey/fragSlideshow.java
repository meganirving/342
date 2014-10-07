package com.example.Journey;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Megan on 29/9/2014.
 */
public class fragSlideshow extends Fragment {

    ArrayList<tblJourney> journeys;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.slideshow, container, false);

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
        Spinner spinJourn = (Spinner) root.findViewById(R.id.spinJourney);
        Spinner spinNum = (Spinner) root.findViewById(R.id.spinTime);

        // Create an ArrayAdapters
        ArrayAdapter<String> adaptJourn = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, journeyList);
        ArrayAdapter<Integer> adaptNum = new ArrayAdapter<Integer>(activity, android.R.layout.simple_spinner_item, durations);
        // Specify layouts
        adaptJourn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaptNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapters to the spinners
        spinJourn.setAdapter(adaptJourn);
        //spinJourn.setPromptId(0);
        spinNum.setAdapter(adaptNum);
        //spinNum.setPromptId(0);

        return root;
    }
}