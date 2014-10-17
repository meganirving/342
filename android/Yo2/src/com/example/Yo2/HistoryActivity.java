package com.example.Yo2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Megan on 5/9/2014.
 */
public class HistoryActivity extends Activity {

    public static final int THUMBNAIL_HEIGHT = 100;
    public static final int THUMBNAIL_WIDTH = 100;
    private ArrayList<YoCatch> history;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        // get array
        Intent intent = getIntent();
        history = (ArrayList<YoCatch>) intent.getSerializableExtra("historyList");

        // load images
        new CreateImgs().execute();

    }

    public void showList() {
        final ListView list = (ListView) findViewById(R.id.list);

        // create the adapter
        YoAdapter adapter = new YoAdapter(history, this);

        // attach the adapter to the frame
        list.setAdapter(adapter);

        // set the regular onclick listener to view a journey
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // plays appropriate sound
            }
        });
    }

    private class CreateImgs extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... v) {

            for (YoCatch msg : history) {
                if (!msg.getImgURL().isEmpty()) {
                    InputStream in = null;
                    try {
                        in = new URL(msg.getImgURL()).openStream();
                        Bitmap img = BitmapFactory.decodeStream(in);
                        Float width = new Float(img.getWidth());
                        Float height = new Float(img.getHeight());
                        Float ratio = width / height;
                        img = Bitmap.createScaledBitmap(img, (int) (THUMBNAIL_WIDTH * ratio), THUMBNAIL_HEIGHT, false);
                        msg.setImg(img);
                    } catch (IOException e) {
                        e.printStackTrace();
                        msg.setImg(null);
                    }
                } else {
                    msg.setImg(null);
                }
            }
            return null;
        }

        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Loading images...", Toast.LENGTH_SHORT).show();
        }

        // show the list only once the images are loaded
        protected void onPostExecute(Void v) {
            Toast.makeText(getApplicationContext(), "Images loaded", Toast.LENGTH_SHORT).show();
            showList();
        }
    }

}