package com.jeomix.android.gpstracker.files.Helper;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by jeomix on 8/5/17.
 */

public class BackEndManager {
static ArrayList<Location> locations=null;

    public static ArrayList<Location> getLocations() {
        return locations;
    }

    public static void setLocations(ArrayList<Location> locations) {
        BackEndManager.locations = locations;
    }
    public static void addLocation(Location location){
        if(locations==null)
            locations=new ArrayList<>();
        locations.add(location);
    }
    public static ArrayList<LatLng> getLatlngs(){
        ArrayList<LatLng> latLngs =null;
        if(locations!=null) {
          latLngs =new ArrayList<>();
            for(Location location : locations)
                latLngs.add(new LatLng(location.getLatitude(),location.getLongitude()));
        }
        return latLngs;
    }
}
