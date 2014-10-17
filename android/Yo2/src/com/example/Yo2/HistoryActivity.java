package com.example.Yo2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
    private ArrayList<YoSound> tracks;
    public int loaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        // get/create arrays
        Intent intent = getIntent();
        history = (ArrayList<YoCatch>) intent.getSerializableExtra("historyList");
        tracks = new ArrayList<YoSound>();

        // load stuff
        loaded = 0;
        new CreateImgs().execute();
        new LoadSounds().execute();
    }

    public YoSound getTrack(String name) {
        // loop through all tracks
        for (YoSound track : tracks) {
            // return the track with a matching name
            if (track.getName() == name) {
                return track;
            }
        }

        // otherwise return null
        return null;
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
                YoCatch item = (YoCatch)parent.getItemAtPosition(position);
                YoSound track = getTrack(item.getName());
                if (track != null) {
                    track.getTrack().start();
                }
            }
        });
    }

    private class LoadSounds extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... v) {
            // loop through all messages
            for (YoCatch msg : history) {
                // if there's an audio track at all
                if (!msg.getAudioURL().isEmpty()) {
                    // if it doesn't already exist
                    if (getTrack(msg.getName()) == null) {
                        // load track
                        try {
                            // create and prepare a mediaplayer
                            MediaPlayer mediaPlayer = new MediaPlayer();
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.setDataSource(msg.getAudioURL());
                            mediaPlayer.prepare();

                            // create a YoSound wrapper
                            YoSound temp = new YoSound();
                            temp.setTrack(mediaPlayer);
                            temp.setName(msg.getName());

                            // add to the list
                            tracks.add(temp);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }

        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Loading sounds...", Toast.LENGTH_SHORT).show();
        }

        // show the list only once the images are loaded
        protected void onPostExecute(Void v) {
            Toast.makeText(getApplicationContext(), "Sounds loaded", Toast.LENGTH_SHORT).show();
            loaded++;
            if (loaded == 2)
                showList();
        }
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
            loaded++;
            if (loaded == 2)
                showList();
        }
    }

}