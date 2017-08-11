package com.jeomix.android.gpstracker.files;

import android.location.Location;

/**
 * Created by jeomix on 8/7/17.
 */

public class VehicleTrack {
    Location location;

    public VehicleTrack(Location location) {
       setLocation(location);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        tos=System.currentTimeMillis();
        this.location = location;
    }

    public long tos;

    public long getTos() {
        return tos;
    }

    public void setTos(long tos) {
        this.tos = tos;
    }
}
