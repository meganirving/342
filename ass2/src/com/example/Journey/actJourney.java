package com.example.Journey;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class actJourney extends Activity {

    MySQLHelper mySql;

    ActionBar.Tab tab1, tab2, tab3;
    Fragment fJourney = new fragJourney();
    Fragment fList = new fragList();
    Fragment fSlide = new fragSlideshow();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // get action bar
        ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // add tabs
        tab1 = bar.newTab().setIcon(R.drawable.ic_map);
        tab1.setTabListener(new MyListener(fJourney));
        bar.addTab(tab1);

        tab2 = bar.newTab().setIcon(R.drawable.ic_list);
        tab2.setTabListener(new MyListener(fList));
        bar.addTab(tab2);

        tab3 = bar.newTab().setIcon(R.drawable.ic_grid);
        tab3.setTabListener(new MyListener(fSlide));
        bar.addTab(tab3);

        // open the database
        mySql = new MySQLHelper(this);
        mySql.open();
    }

    // when the app stops
    @Override
    public void onStop() {
        super.onStop();
        // close the database
        mySql.closeDB();
    }

    // get the activity's db reference
    public MySQLHelper getMySql() {
        return mySql;
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public boolean getCam() {
        return checkCameraHardware(this);
    }
}
