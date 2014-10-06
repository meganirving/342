package com.example.Journey;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Megan on 29/9/2014.
 */
public class fragList extends Fragment {

    private MySQLHelper mySql;
    private ArrayList<tblJourney> journeys;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.list, container, false);

        // get the list frame
        ListView list = (ListView) root.findViewById(R.id.list);

        // get all journeys
        mySql = new MySQLHelper(getActivity());
        mySql.open();
        journeys = mySql.getAllJourneys();

        // create the adapter
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, journeys) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);

                text1.setText(journeys.get(position).getTitle());
                return view;
            }
        };

        // attach the adapter to the frame
        list.setAdapter(adapter);

        return root;
    }
}