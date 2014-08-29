package com.example.ChallengeTimer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Megan on 30/8/2014.
 */
public class LTAdapter extends ArrayAdapter<LTTime> {
    Context context;
    int layoutResourceId;
    ArrayList<LTTime> data = null;

    public LTAdapter(Context context, int layoutResourceId, ArrayList<LTTime> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TimeHolder holder = null;

        if(row == null)
        {
            // inflate xml
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            // get row
            holder = new TimeHolder();
            holder.time = (TextView)row.findViewById(R.id.col1);
            holder.comment = (TextView)row.findViewById(R.id.col2);
            holder.date = (TextView)row.findViewById(R.id.col3);

            row.setTag(holder);
        }
        else
        {
            holder = (TimeHolder)row.getTag();
        }

        // get the time data
        LTTime time = data.get(position);
        holder.time.setText(LTTime.convertToString(time.getTime()));
        holder.comment.setText(time.getComment());
        holder.date.setText(time.getDate().toString());

        return row;
    }

    // cache class
    static class TimeHolder
    {
        TextView time;
        TextView comment;
        TextView date;
    }
}
