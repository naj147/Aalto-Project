package com.jeomix.android.gpstracker.files.EventBusClasses;

import com.jeomix.android.gpstracker.files.Objects.Vehicle;

/**
 * Created by jeomix on 8/14/17.
 */

public class LocationEvents {
    //Should it be Tracked on map or untracked
    boolean isTrack;
    //The vehicle to track
    Vehicle vehicle;


    public LocationEvents( Vehicle vehicle, boolean isTrack) {
        this.isTrack = isTrack;
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }


    //The track date (Testing...)
    //long tosToTrack;

    public boolean isTrack() {
        return isTrack;
    }

    public void setTrack(boolean track) {
        isTrack = track;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null)
            return false;
        if( getClass()!=obj.getClass())
            return false;
        if(this==obj)
            return true;
        LocationEvents v = (LocationEvents) obj;
        return this.vehicle.getId().equals(v.getVehicle().getId());

    }
}
