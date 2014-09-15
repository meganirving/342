package com.example.Yo2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Megan on 5/9/2014.
 */
public class HistoryActivity extends Activity {

    private ArrayList<YoCatch> history;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        Intent intent = getIntent();
        history = (ArrayList<YoCatch>) intent.getSerializableExtra("historyList");

        final ListView list = (ListView) findViewById(R.id.list);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, history) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(history.get(position).getMsg());
                text2.setText(history.get(position).getName());
                return view;
            }
        };

        list.setAdapter(adapter);
    }
}