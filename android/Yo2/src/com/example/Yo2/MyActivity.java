package com.example.Yo2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class MyActivity extends Activity
{
    // UI elements
    TextView messageLabel;
    EditText yoText;
    EditText messageText;
    Button doStuff;
    LinearLayout layout;

    // other stuff
    MediaPlayer mediaPlayer;

    // history
    ArrayList<YoCatch> history;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // connect elements to code
        messageLabel = (TextView) findViewById(R.id.label);
        yoText = (EditText) findViewById(R.id.yo);
        yoText.setHint("Yo");
        messageText = (EditText) findViewById(R.id.name);
        messageText.setHint("Your Name");
        doStuff = (Button) findViewById(R.id.button);
        layout = (LinearLayout) findViewById(R.id.screen);

        // load/create history
        history = loadList();
        if (history == null)
            history = new ArrayList<YoCatch>();

        // text's listener
        messageText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                changeText();
                saveList();
                return true;
            }
        });
        yoText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                return true;
            }
        });

        // set up audio
        try {
            AssetFileDescriptor afd = getAssets().openFd("cartoon019.mp3");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view)
    {
        // passes the list to HistoryActivity and starts it
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra("historyList", history);
        startActivity(intent);
    }

    // updates the text of the label
    public void changeText()
    {
        // changes background colour
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        layout.setBackgroundColor(color);

        // updates text
        String msg = yoText.getText() + "\n" + messageText.getText();
        messageLabel.setText(msg);

        // plays sound
        mediaPlayer.start();

        // updates array
        YoCatch newMsg = new YoCatch(messageText.getText().toString(), yoText.getText().toString());
        history.add(newMsg);
    }

    public ArrayList<YoCatch> loadList()
    {
        ArrayList<YoCatch> list;

        try
        {
            FileInputStream ifile = getApplicationContext().openFileInput("save.bin");
            ObjectInputStream istream = new ObjectInputStream(ifile);
            list = (ArrayList<YoCatch>)istream.readObject();
            return list;
        }
        catch(Exception ex) {
            Log.v("Loading Error: ", ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public void saveList()
    {
        try
        {
            FileOutputStream ofile = getApplicationContext().openFileOutput("save.bin", Context.MODE_PRIVATE);
            ObjectOutputStream ostream = new ObjectOutputStream(ofile);
            ostream.writeObject(history);
            //ostream.flush();
            ostream.close();
        }
        catch(Exception ex)
        {
            Log.v("Saving Error: ",ex.getMessage());
            ex.printStackTrace();
        }
    }

}