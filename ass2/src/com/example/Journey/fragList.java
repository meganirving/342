package com.example.Journey;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Megan on 29/9/2014.
 */
public class fragList extends Fragment {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    private MySQLHelper mySql;
    private ArrayList<tblJourney> journeys;
    private tblJourney currJourney;
    private View root;
    private ListView listframe;
    private ViewFlipper flipper;
    private journeyAdapter adapter;
    private TextView text;
    private Button back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.list, container, false);

        // get the db helper
        actJourney activity = (actJourney)getActivity();
        mySql = activity.getMySql();

        // get the frames
        listframe = (ListView) root.findViewById(R.id.list);
        text = (TextView) root.findViewById(R.id.flipText);
        flipper = (ViewFlipper) root.findViewById(R.id.flipGal);
        back = (Button) root.findViewById(R.id.btnBack);
        
        // get all journeys
        mySql.checkOpen();
        journeys = mySql.getAllJourneys();

        // create the adapter
        adapter = new journeyAdapter(journeys, getActivity());

        // attach the adapter to the frame
        listframe.setAdapter(adapter);

        // register for context menu
        registerForContextMenu(listframe);

        // set the regular onclick listener to view a journey
        listframe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // view the selected journey
                // TODO: this selects the wrong journey, somehow?
                currJourney = (tblJourney) parent.getItemAtPosition(position);
                viewJourney(currJourney);
            }
        });

        // set the flipper's flippy functions
        flipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });

        // set the back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear the flipper and reset the views
                clearFlipper();
                flipper.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
                listframe.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // check if there are any photos at all
            if (flipper.getChildCount() != 0) {
                try {
                    // right to left swipe
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right));
                        flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left));
                        flipper.showNext();
                        updateText();
                        return true;
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        // left to right swipe
                        flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                        flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right));
                        flipper.showPrevious();
                        updateText();
                        return true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return false;
        }


    }

    public void updateText() {
        // get the current photo
        int index = flipper.getDisplayedChild();
        tblPhoto photo = currJourney.getPhotos().get(index);
        String caption = "";

        // if the photo has a caption
        if (!photo.getComment().isEmpty()) {
            caption = photo.getComment() + "\n";
        }

        // add the timestamp
        caption = caption + photo.gettimeStamp();
        text.setText(caption);
        text.setVisibility(View.VISIBLE);
    }

    public void clearFlipper() {
        // empty the flipper of all the views
        flipper.removeAllViews();
    }
    public void addToFlipper(tblJourney journey) {
        // TODO: be able to show more than one photo. currently the bitmaps are too large
        if (!journey.getPhotos().isEmpty()) {
            File imgFile = new File(journey.getPhotos().get(0).getimgURL());
            if (imgFile.exists()) {
                // create the imageview and add it to the flipper
                ImageView imageView = new ImageView(getActivity());
                imageView.setImageURI(Uri.fromFile(imgFile));
                flipper.addView(imageView);
            }
        } else {
            // if no photo, show the no photo text
            text.setText("No Photos To Display");
            text.setVisibility(View.VISIBLE);
        }
    }

    // view the passed-in journey
    public void viewJourney(tblJourney journey) {
        // hide the list
        listframe.setVisibility(View.GONE);

        // reset and show the flipper
        clearFlipper();
        addToFlipper(journey);
        flipper.setVisibility(View.VISIBLE);
        updateText();

        // show the back button
        back.setVisibility(View.VISIBLE);
    }

    // delete the passed-in journey
    public void deleteJourney(tblJourney journey) {
        // delete it from the database
        mySql.deleteJourney(journey);

        // delete it from the list and update the adapter
        adapter.remove(journey);
        adapter.notifyDataSetChanged();
    }

    // context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.list) {
            // get the current journey
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            tblJourney currJourney = journeys.get(info.position);
            // set the menu title
            menu.setHeaderTitle(currJourney.getTitle());
            // set menu items
            menu.add(0, v.getId(), 0, "Select");
            menu.add(0, v.getId(), 0, "Delete");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        // get current journey
        tblJourney currJourney = journeys.get(info.position);

        // handle menu choice
        if (item.getTitle().equals("Select")) {
            // view the selected journey
            viewJourney(currJourney);
        } else if (item.getTitle().equals("Delete")) {
            // delete the selected journey
            deleteJourney(currJourney);
        } else {
            // handle error
            return false;
        }
        return true;
    }
}