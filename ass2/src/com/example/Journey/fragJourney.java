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
    private double latitude;
    private double longitude;
    private String comment;
    private String URL;
    private String timestamp;
    private MySQLHelper mySQLHelper;
    private boolean cam;
    private int journID;
    private int pointID;
    private int picID;

    // map stuff
    private GoogleMap map;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.journey, container, false);

        // get sqlhelper from the activity and camera status
        actJourney activity = (actJourney) getActivity();
        mySQLHelper = activity.getMySql();
        cam = activity.isCamera();

        // create a journey, point and photo
        currJourney = new tblJourney();
        // set the ID of the journey to the largest ID in the database plus 1
        journID = mySQLHelper.getLastID() + 1;
        currJourney.setID(journID);

        btnRec = (Button) root.findViewById(R.id.butRecord);
        btnRec.setOnClickListener(this);
        btnCam = (Button) root.findViewById(R.id.butCamera);
        btnCam.setOnClickListener(this);
        btnStop = (Button) root.findViewById(R.id.butStop);
        btnStop.setOnClickListener(this);

        // TODO: create mapview. This code doesn't run on my emulator, and creates a million errors on my phone
        /*mapView = (MapView) root.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        map.animateCamera(cameraUpdate);*/

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

        // if the phone has a camear
        if (cam) {
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else {
            // TODO: open gallery folder to pick images
        }
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
                        // User clicked OK button, so get the comment
                        comment = input.getText().toString();
                        // add the photo to the journey
                        String date = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm:ss").format(new Date());
                        currJourney.addPhoto(new tblPhoto(date, comment, URL, picID));

                        // give the user an alert
                        Toast.makeText(getActivity(), "Image and caption saved!", Toast.LENGTH_SHORT).show();

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // add the photo to the journey
                        String date = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm:ss").format(new Date());
                        currJourney.addPhoto(new tblPhoto(date, "", URL, picID));

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
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        currJourney.setDate(date);

        // add the title to the journey
        currJourney.setTitle(title);

        // save the journey to the database
        mySQLHelper.checkOpen();
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
        // get location data
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        // get the timestamp
        timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        Toast.makeText(getActivity(), "changed", Toast.LENGTH_SHORT);

        // add it to the journey
        pointID = currJourney.nextPointID();
        String comp = pointID + "_" + timestamp;
        currJourney.addPoint(new tblPoint(timestamp, latitude, longitude, pointID, comp));
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
        // get a timestamp
        String ts = new SimpleDateFormat("HH:mm:ss").format(new Date());

        // Create a url
        File mediaFile;
        picID = currJourney.nextPhotoID();
        journID = currJourney.getID();
        URL = mediaStorageDir.getPath() + File.separator + "IMG_" + journID + picID + "_" + ts + ".jpg";
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(URL);
        } else {
            return null;
        }

        // return the file
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
