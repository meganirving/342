package com.example.Journey;

import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Megan on 29/9/2014.
 */
public class fragList extends Fragment {

    private MySQLHelper mySql;
    private ArrayList<tblJourney> journeys;
    private View root;
    private LinearLayout textframe;
    private ListView listframe;
    private FrameLayout imgframe;
    private journeyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.list, container, false);

        // get the db helper
        actJourney activity = (actJourney)getActivity();
        mySql = activity.getMySql();

        // get the frames
        listframe = (ListView) root.findViewById(R.id.list);
        textframe = (LinearLayout) root.findViewById(R.id.textframe);
        imgframe = (FrameLayout) root.findViewById(R.id.imgframe);
        
        // get all journeys
        journeys = mySql.getAllJourneys();

        // create the adapter
        adapter = new journeyAdapter(journeys, getActivity());

        // attach the adapter to the frame
        listframe.setAdapter(adapter);

        // register for context menu
        registerForContextMenu(listframe);

        // set the regular onclick listener to view a journey
        listframe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // view the selected journey
                tblJourney journey = (tblJourney) parent.getItemAtPosition(position);
                viewJourney(journey);
            }
        });

        return root;
    }

    // view the passed-in journey
    public void viewJourney(tblJourney journey) {
        // hide the list
        listframe.setVisibility(View.GONE);

        // show the picture frame + map frame
        imgframe.setVisibility(View.VISIBLE);
        textframe.setVisibility(View.VISIBLE);

        // fill the information
        TextView text = (TextView) textframe.getChildAt(0);
        text.setText(journey.getTitle());
    }

    // delete the passed-in journey
    public void deleteJourney(tblJourney journey) {
        // delete it from the database
        mySql.deleteJourney(journey.getID());

        // delete it from the list and update the adapter
        adapter.remove(journey);
        adapter.notifyDataSetChanged();
    }

    // context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.list) {
            // get the current journey
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            tblJourney currJourney = journeys.get(info.position);
            // set the menu title
            menu.setHeaderTitle(currJourney.getTitle());
            // set menu items
            menu.add(0, v.getId(), 0, "Select");
            menu.add(0, v.getId(), 0, "Delete");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        // get current journey
        tblJourney currJourney = journeys.get(info.position);

        // handle menu choice
        if (item.getTitle().equals("Select")) {
            // view the selected journey
            viewJourney(currJourney);
        } else if (item.getTitle().equals("Delete")) {
            // delete the selected journey
            deleteJourney(currJourney);
        } else {
            // handle error
            return false;
        }
        return true;
    }
}