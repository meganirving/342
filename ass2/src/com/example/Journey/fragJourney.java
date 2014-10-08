package com.example.Journey;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Megan on 29/9/2014.
 */
public class fragJourney extends Fragment implements View.OnClickListener {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    private View root;
    private Button btnRec;
    private Button btnCam;
    private Button btnStop;

    private tblJourney currJourney;
    private tblPoint currPoint;
    private tblPhoto currPhoto;
    private MySQLHelper mySQLHelper;

    // map stuff
    private GoogleMap map;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.journey, container, false);

        // get sqlhelper from the activity
        actJourney activity = (actJourney) getActivity();
        mySQLHelper = activity.getMySql();

        // create a journey, point and photo
        currPoint = new tblPoint();
        currPhoto = new tblPhoto();
        currJourney = new tblJourney();
        // set the ID of the journey to the largest ID in the database plus 1
        currJourney.setID( mySQLHelper.getLastID() +1 );

        btnRec = (Button) root.findViewById(R.id.butRecord);
        btnRec.setOnClickListener(this);
        btnCam = (Button) root.findViewById(R.id.butCamera);
        btnCam.setOnClickListener(this);
        btnStop = (Button) root.findViewById(R.id.butStop);
        btnStop.setOnClickListener(this);

        // create map
        /*mapView = (MapView) root.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());*/

        return root;
    }

    // called when the record button is pressed
    public void RecordButton(View v) {

        // start collecting location data
        actJourney activity = (actJourney) getActivity();
        boolean tracking = activity.startTracking();

        if (tracking) {
            // hide this button
            v.setVisibility(View.GONE);

            // show the other two buttons
            btnCam.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.VISIBLE);

            // feedback
            Toast.makeText(activity, "Recording...", Toast.LENGTH_SHORT).show();
        } else {
            // show error
            Toast.makeText(activity, "Error getting location services", Toast.LENGTH_SHORT).show();
        }
    }

    // called when the camera button is pressed
    public void CameraButton(View v) {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    // when the camera intent returns
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {

                // create an alertdialog for adding an optional comment
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                // add a message and a textbox
                alert.setMessage("Give your photo an optional caption:");
                final EditText input = new EditText(getActivity());
                alert.setView(input);

                // add buttons
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button, so add the comment to the photo
                        final String comment = input.getText().toString();
                        currPhoto.setComment(comment);

                        // add the photo to the journey
                        currJourney.addPhoto(currPhoto);

                        // give the user an alert
                        Toast.makeText(getActivity(), "Image and caption saved!", Toast.LENGTH_SHORT).show();

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // add the photo to the journey
                        currJourney.addPhoto(currPhoto);

                        // give the user an alert
                        Toast.makeText(getActivity(), "Image saved!", Toast.LENGTH_SHORT).show();

                    }
                });

                // Create and show the dialog
                AlertDialog dialog = alert.create();
                dialog.show();
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capturing failed somehow
                Toast.makeText(getActivity(), "Image not saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // called when the stop button is pressed
    public void StopButton(View v) {

        // create an alertdialog for getting the journey name
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        // add a message and a textbox
        alert.setMessage("Give your journey a title:");
        final EditText input = new EditText(getActivity());
        alert.setView(input);

        // add buttons
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button, so call the save function
                final String title = input.getText().toString();
                // if the user entered a title
                if (!title.isEmpty()){
                    SaveJourney(title);
                } else {
                    // otherwise save it with a default title
                    SaveJourney("Untitled Journey");
                }

                // stop tracking location
                actJourney activity = (actJourney) getActivity();
                activity.stopTracking();

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog, do nothing
            }
        });

        // Create and show the dialog
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    // saves the journey to database, resets button visibility, and displays a toast
    public void SaveJourney(String title) {
        // hide the stop and camera buttons
        btnStop.setVisibility(View.GONE);
        btnCam.setVisibility(View.GONE);

        // show the record button
        btnRec.setVisibility(View.VISIBLE);

        // add the date to the journey
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        currJourney.setDate(formattedDate);

        // add the title to the journey
        currJourney.setTitle(title);

        // save the journey to the database
        mySQLHelper.createJourney(currJourney);

        // show toast
        Toast.makeText(getActivity().getApplicationContext(), "Saving...", Toast.LENGTH_SHORT).show();
    }

    // deals with the buttons being clicked
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.butStop:
                StopButton(v);
                break;
            case R.id.butCamera:
                CameraButton(v);
                break;
            case R.id.butRecord:
                RecordButton(v);
                break;
        }
    }

    // gets a new location
    public void updateLocation(Location location) {
        // update the data of the point
        currPoint.setLat(location.getLatitude());
        currPoint.setLong(location.getLongitude());

        // add the timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        currPoint.settimeStamp(timeStamp);

        Toast.makeText(getActivity(), currPoint.getLat() + ", " + currPoint.getLong() + ": " + currPoint.gettimeStamp(), Toast.LENGTH_SHORT).show();

        // add it to the journey
        currJourney.addPoint(currPoint);
    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }
    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Journey");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Journey", "failed to create directory");
                return null;
            }
        }
        // Create a url
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        String url = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(url);
        } else {
            return null;
        }

        currPhoto.settimeStamp(timeStamp);
        currPhoto.setimgURL(url);

        return mediaFile;
    }

    @Override
    public void onResume() {
        // mapView.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        // if the fragment stops, stop tracking!
        actJourney activity = (actJourney) getActivity();
        activity.stopTracking();
        super.onDestroy();
        //mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mapView.onLowMemory();
    }
}
