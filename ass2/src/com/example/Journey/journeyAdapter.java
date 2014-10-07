package com.example.Journey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Megan on 6/10/2014.
 */

public class journeyAdapter extends ArrayAdapter<tblJourney> {

    private ArrayList<tblJourney> journeys;
    private Context context;

    public journeyAdapter(ArrayList<tblJourney> journeys, Context ctx) {
        super(ctx, R.layout.listrow, journeys);
        this.journeys = journeys;
        this.context = ctx;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        journeyHolder holder = new journeyHolder();

        if (convertView == null) {
            // inflate layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listrow, parent, false);

            // get views
            TextView title = (TextView) convertView.findViewById(R.id.listTitle);
            TextView date = (TextView) convertView.findViewById(R.id.listDate);
            TextView dist = (TextView) convertView.findViewById(R.id.listDist);

            // set static holder
            holder.title = title;
            holder.date = date;
            holder.distance = dist;
            convertView.setTag(holder);
        }
        else {
            holder = (journeyHolder) convertView.getTag();
        }

        // get the journey
        tblJourney journey = journeys.get(position);

        // fill the views
        holder.title.setText(journey.getTitle());
        holder.date.setText("6/10/2014");
        holder.distance.setText("5km");

        return convertView;
    }
}