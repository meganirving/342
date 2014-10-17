package com.example.Yo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Megan on 17/10/2014.
 */
public class YoAdapter extends ArrayAdapter<YoCatch> {

    private ArrayList<YoCatch> messages;
    private Context context;

    public YoAdapter(ArrayList<YoCatch> messages, Context ctx) {
        super(ctx, R.layout.row, messages);
        this.messages = messages;
        this.context = ctx;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        YoHolder holder = new YoHolder();

        if (convertView == null) {
            // inflate layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, parent, false);

            // get views
            TextView name = (TextView) convertView.findViewById(R.id.txtName);
            TextView msg = (TextView) convertView.findViewById(R.id.txtMsg);
            ImageView thumb = (ImageView) convertView.findViewById(R.id.img);

            // set static holder
            holder.txtName = name;
            holder.txtMsg = msg;
            holder.img = thumb;
            convertView.setTag(holder);
        }
        else {
            holder = (YoHolder) convertView.getTag();
        }

        // get the journey
        YoCatch msg = messages.get(position);

        // fill the views
        holder.txtName.setText(msg.getName());
        holder.txtMsg.setText(msg.getMsg());

        if (msg.getImg() != null) {
            //int padding = (66 - msg.getImg().getWidth()) / 2;
            //holder.img.setPadding(padding, 0, padding, 0);
            holder.img.setImageBitmap(msg.getImg());
        }

        return convertView;
    }
}
