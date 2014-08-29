package com.example.ChallengeTimer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.ChallengeTimer.LTModel;


/**
 * Created by Megan on 26/8/2014.
 */
public class ChallengeFragment extends Fragment {

    public LTModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.challenge, container, false);

        model = new LTModel();
        model.addChallenge(new LTChallenge("Clap 20 Times Challenge"));
        model.addChallenge(new LTChallenge("Say the Alphabet Challenge"));
        model.addChallenge(new LTChallenge("100 Meter Sprint Challenge"));
        model.addChallenge(new LTChallenge("Read the CSCI342 A1 spec Challenge"));

        ListView list = (ListView) rootView.findViewById(R.id.list);

        ArrayAdapter<LTChallenge> adapter = new ArrayAdapter<LTChallenge>(getActivity(), android.R.layout.simple_list_item_1, model.getList());
        list.setAdapter(adapter);


        return rootView;
    }
}