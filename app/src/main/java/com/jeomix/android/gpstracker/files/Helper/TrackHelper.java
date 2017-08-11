package com.jeomix.android.gpstracker.files.Helper;

import android.location.Location;

import com.jeomix.android.gpstracker.files.Tracks;

/**
 * Created by jeomix on 8/11/17.
 */

public class TrackHelper {
    public static Tracks currentTrack;
    public static boolean isItNew(Location l){
        if(currentTrack!=null){
            if(currentTrack.getTracks()==null || currentTrack.getTracks().size()==0){
                return true;
            }
            Location comparable=currentTrack.getLocation(currentTrack.getTracks().size()-1);
            if(comparable!=null){
                if(l.getLatitude()==comparable.getLatitude() && l.getLongitude()==comparable.getLongitude())
                    return false;
            }
        }
        return true;
    }
}
