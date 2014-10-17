package com.example.Yo2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyActivity extends Activity
{
    // UI elements
    TextView messageLabel;
    EditText yoText;
    EditText nameText;
    Button doStuff;
    LinearLayout layout;

    // other stuff
    MediaPlayer mediaPlayer;
    String username;
    boolean loaded;

    // history
    ArrayList<YoCatch> history;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loaded = false;

        // check network connection
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            // display error
            Toast.makeText(this, "Error loading messages", Toast.LENGTH_SHORT).show();
        } else {
            // load messages
            new DownloadMessages().execute();
        }

        // connect elements to code
        messageLabel = (TextView) findViewById(R.id.label);
        yoText = (EditText) findViewById(R.id.yo);
        nameText = (EditText) findViewById(R.id.name);
        doStuff = (Button) findViewById(R.id.button);
        layout = (LinearLayout) findViewById(R.id.screen);

        // set username
        username = "mki578";

        // text listeners
        nameText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(nameText.getWindowToken(), 0);
                    changeText();
                    return true;
                }
                return false;
            }
        });
        yoText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(yoText.getWindowToken(), 0);
                return true;
            }
        });

        // set up audio
        try {
            AssetFileDescriptor afd = getAssets().openFd("yo.mp3");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // history button
    public void onClick(View view)
    {
        // if not loaded yet
        if (!loaded) {
            // show message
            Toast.makeText(this, "Messages not loaded yet", Toast.LENGTH_SHORT).show();
        } else {
            // passes the list to HistoryActivity and starts it
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("historyList", history);
            startActivity(intent);
        }
    }

    // updates the text of the label
    public void changeText()
    {
        // changes background colour
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        layout.setBackgroundColor(color);

        // updates text
        String msg = yoText.getText() + "\n" + nameText.getText() + "!";
        messageLabel.setText(msg);

        // plays sound
        mediaPlayer.start();

        // upload message
        //new PostMessageAsyncTask().execute();
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadMessages extends AsyncTask<String, Void, Void> {

        InputStream inputStream = null;
        String result = "";

        @Override
        protected Void doInBackground(String... v) {
            String url_select = "http://li671-166.members.linode.com/yo/retrieveAllEntries.php";
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                // Set up HTTP post
                // HttpClient is more then less deprecated. Need to change to URLConnection
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(url_select);
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                // Read content & Log
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingException", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }

            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
            }

            try {
                ArrayList<YoCatch> messages = new ArrayList<YoCatch>();
                JSONArray jArray = new JSONArray(result);
                // loop through all messages
                for(int i = 0; i < 2; i++) {

                    publishProgress((int) ((i / (float) 2) * 100));

                    // get message and turn it into a YoCatch message
                    JSONObject jObject = jArray.getJSONObject(i);
                    YoCatch temp = new YoCatch();
                    temp.setName(jObject.getString("yoDestination"));
                    temp.setMsg(jObject.getString("yoMessage"));
                    temp.setImgURL(jObject.getString("imageURL"));
                    temp.setAudioURL(jObject.getString("imageURL"));

                    // add it to the list
                    messages.add(temp);
                }

                // pass through the completed progress
                publishProgress(100);

                // set the list
                history = messages;

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }
            return null;
        }

        protected void onProgressUpdate(int count) {
            Log.v("ugh", count + "% progress");
        }


        private void publishProgress(int count) {
            onProgressUpdate(count);
        }

        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Loading messages...", Toast.LENGTH_SHORT).show();
        }

        protected void onPostExecute(Void v) {
            loaded = true;
            Toast.makeText(getApplicationContext(), "Messages loaded", Toast.LENGTH_SHORT).show();
        }
    }

    private class PostMessageAsyncTask extends AsyncTask <Void, Void, String> {
        @Override protected String doInBackground(Void... p){
            String response = "";
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://li671-166.members.linode.com/yo/addEntry.php");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>(3);
                params.add(new BasicNameValuePair("yoMessage", yoText.getText().toString()));
                params.add(new BasicNameValuePair("yoDestination", nameText.getText().toString()));
                params.add(new BasicNameValuePair("username", username));
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                HttpResponse execute = client.execute(httpPost);
                InputStream content = execute.getEntity().getContent();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null){
                    response += s;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Log.d("debug", result);
        }
    }
}