package com.example.ChallengeTimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.sax.RootElement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.example.ChallengeTimer.LTModel;


/**
 * Created by Megan on 26/8/2014.
 */
public class ChallengeFragment extends Fragment {

    public LTModel model;
    public EditText input;
    public Button button;

    // overload onAttach
    public void onAttach(Activity activity)
    {
        // call the regular one
        super.onAttach(activity);
        // set up the listener
        listener = (ChallengeListener)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.challenge, container, false);

        // create model
        model = new LTModel();
        model.addChallenge(new LTChallenge("Clap 20 Times Challenge"));
        model.addChallenge(new LTChallenge("Say the Alphabet Challenge"));
        model.addChallenge(new LTChallenge("100 Meter Sprint Challenge"));
        model.addChallenge(new LTChallenge("Read the CSCI342 A1 spec Challenge"));

        // attach data to listview
        final ListView list = (ListView) rootView.findViewById(R.id.list);
        final ArrayAdapter<LTChallenge> adapter = new ArrayAdapter<LTChallenge>(getActivity(), android.R.layout.simple_list_item_1, model.getList());
        list.setAdapter(adapter);

        // onClickListener for list items
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // pass through the selected challenge to the listener
                listener.selectChallenge((LTChallenge) parent.getItemAtPosition(position));
            }});

        // create alert dialogue
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        // set a title and a message
        alertBuilder.setTitle("New Challenge");
        alertBuilder.setMessage("Please enter a name for your challenge:");
        // create an editText and add it to the alert
        input = new EditText(getActivity());
        alertBuilder.setView(input);
        // save button
        alertBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // create new challenge if the user entered a name
                if (!input.getText().toString().isEmpty()) {
                    model.addChallenge(new LTChallenge(input.getText().toString()));
                    // update list
                    adapter.notifyDataSetChanged();
                }
            }});
        // cancel button
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { /* do absolutely nothing :)*/ }});
        // create the final alert
        final AlertDialog alert = alertBuilder.create();

        // set up "new" button
        button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show new challenge alert
                alert.show();
            }
        });

        return rootView;
    }

    // for interacting with the parent activity
    public interface ChallengeListener{
        void selectChallenge(LTChallenge challenge);
    }
    private ChallengeListener listener;
}