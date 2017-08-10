package com.jeomix.android.gpstracker.files;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by jeomix on 8/7/17.
 */

public class Tracks {

    private ArrayList<VehicleTrack> tracks;
   public void addTrack(VehicleTrack vt){
       if(tracks==null)
           tracks=new ArrayList<>();
       tracks.add(vt);
   }
   public void addLocaion(Location l){
       if(tracks==null)
           tracks=new ArrayList<>();
       tracks.add(new VehicleTrack(l));
   }
   public Location getLocation(int index){
       if (tracks!=null && tracks.size()>index)
           return tracks.get(index).getLocation();
       return null;
   }
   public VehicleTrack getTrack(int index){
       if (tracks!=null && tracks.size()>index)
           return tracks.get(index);
       return null;
   }

    public ArrayList<VehicleTrack> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<VehicleTrack> tracks) {
        this.tracks = tracks;
    }




}
