package com.jeomix.android.gpstracker.files.Helper;

import android.location.Location;

import com.google.firebase.database.ValueEventListener;
import com.jeomix.android.gpstracker.files.Objects.Vehicle;

import java.util.ArrayList;

/**
 * Created by jeomix on 8/14/17.
 */

public class BackEndTracking {
    Vehicle vehicle;
    ArrayList<Location> locations;
    ValueEventListener vel;

    public BackEndTracking(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public ValueEventListener getVel() {
        return vel;
    }
    public  void addLocation(Location location){
        if(locations==null)
            locations=new ArrayList<>();
        locations.add(location);
    }
    public void setVel(ValueEventListener vel) {
        this.vel = vel;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj==null)
            return false;
        if( getClass()!=obj.getClass())
            return false;
        if(this==obj)
            return true;
        Vehicle v = (Vehicle) obj;
        return this.vehicle.getId().equals(v.getId());

    }
}
