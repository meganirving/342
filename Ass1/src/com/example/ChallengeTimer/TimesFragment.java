package com.example.ChallengeTimer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Megan on 26/8/2014.
 */
public class TimesFragment extends Fragment {

    private LTChallenge challenge;

    public void setChallenge(LTChallenge newChallenge)
    {
        challenge = newChallenge;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.times, container, false);

        // create adapter
        LTAdapter adapter = new LTAdapter(getActivity(), R.layout.row, challenge.getTimes());
        // get list view and set its adapter
        final ListView list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(adapter);

        return rootView;

    }
}