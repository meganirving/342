package com.example.Journey;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.drm.DrmStore;

/**
 * Created by Megan on 30/9/2014.
 */
public class MyListener implements ActionBar.TabListener {

    Fragment fragment;

    public MyListener(Fragment fragment) {
        this.fragment = fragment;
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.fragContainer, fragment);
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.remove(fragment);
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // nothing done here
    }
}
