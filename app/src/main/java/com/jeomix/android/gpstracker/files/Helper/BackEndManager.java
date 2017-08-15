package com.jeomix.android.gpstracker.files.Helper;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ValueEventListener;
import com.jeomix.android.gpstracker.files.Vehicle;

import java.util.ArrayList;

/**
 * Created by jeomix on 8/5/17.
 */

public class BackEndManager {
private static ArrayList<Location> locations=null;



    //THIS WAS FOR TRACKING MORE THAN ONE VEHICULE WHICH I IGNORED AT THE END CAUSE IT WILL OVERLOAD THE DB HENCE DEPRICATING MY FREE ACCESS TO THE DB

//private static ArrayList<BackEndTracking> backEndTrackings=null;

//    public static ArrayList<BackEndTracking> getBackEndTrackings() {
//        return backEndTrackings;
//    }
//
//    public static void setBackEndTrackings(ArrayList<BackEndTracking> backEndTrackings) {
//        BackEndManager.backEndTrackings = backEndTrackings;
//    }

    //    public static ArrayList<LatLng> getLatlngs(Vehicle v){
//        for(BackEndTracking backEndTracking : backEndTrackings){
//            if(backEndTracking.getVehicle().getId().equals(v.getId())){
//                return getLatlngs(backEndTracking.getLocations());
//            }
//        }
//        return null;
//    }
//    private static ArrayList<LatLng> getLatlngs(ArrayList<Location> locations){
//        ArrayList<LatLng> latLngs =null;
//        if(locations!=null) {
//          latLngs =new ArrayList<>();
//            for(Location location : locations)
//                latLngs.add(new LatLng(location.getLatitude(),location.getLongitude()));
//        }
//        return latLngs;
//    }

//    public static void addBackEndTracking(BackEndTracking backEndTracking){
//        if(backEndTrackings==null)
//            backEndTrackings=new ArrayList<>();
//        backEndTrackings.add(backEndTracking);
//    }
//    public static void addValueListener(ValueEventListener vel){
//        if(backEndTrackings!=null){
//            backEndTrackings.get(backEndTrackings.size()-1).setVel(vel);
//        }
//    }
//public static boolean containsVehicule(Vehicle v){
//    for(BackEndTracking backEndTracking : backEndTrackings){
//        if(v.getId().equals(backEndTracking.getVehicle().getId())){
//            return true;
//        }
//    }
//    return false;
//}
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
