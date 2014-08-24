package com.example.Yo2;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.Random;

public class MyActivity extends Activity
{
    // UI elements
    TextView messageLabel;
    EditText yoText;
    EditText messageText;
    Button doStuff;
    LinearLayout layout;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //setContentView(R.layout.main);

        // connect elements to code
        //messageLabel = (TextView) findViewById(R.id.label);
        //yoText = (EditText) findViewById(R.id.yo);
        //messageText = (EditText) findViewById(R.id.name);
        //doStuff = (Button) findViewById(R.id.button);
        //layout = (LinearLayout) findViewById(R.id.screen);

        // create label
        messageLabel = new TextView(this);
        messageLabel.setText("Yo \nYour Name");
        messageLabel.setTextSize(40);
        messageLabel.setGravity(Gravity.CENTER);
        // create text editors
        yoText = new EditText(this);
        yoText.setText("Yo");
        messageText = new EditText(this);
        messageText.setText("Your Name");
        // create button
        doStuff = new Button(this);
        doStuff.setText("Show Message");
        // create params
        LinearLayout.LayoutParams center = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.9f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // create linear layout
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        // set params
        layout.setLayoutParams(params);
        messageLabel.setLayoutParams(center);
        // layout.setGravity(Gravity.CENTER);
        layout.addView(yoText);
        layout.addView(messageText);
        layout.addView(messageLabel);
        layout.addView(doStuff);
        layout.setBackgroundColor(Color.RED);

        setContentView(layout);

        // the button's listener
        doStuff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // call this function when someone clicks the button
                changeText();
            }
        });

        // text's listener
        messageText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND)
                {
                    changeText();
                    handled = true;
                }

                return handled;
            }
        });
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
    }

}