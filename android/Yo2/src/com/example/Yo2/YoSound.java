package com.example.Yo2;

import android.media.MediaPlayer;

/**
 * Created by Megan on 17/10/2014.
 */
public class YoSound {

    private String Name;
    private MediaPlayer Track;

    public void setName(String name) { Name = name; }
    public void setTrack(MediaPlayer track) { Track = track; }

    public String getName() { return Name; }
    public MediaPlayer getTrack() { return Track; }
}
