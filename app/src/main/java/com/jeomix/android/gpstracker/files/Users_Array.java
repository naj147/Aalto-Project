package com.jeomix.android.gpstracker.files;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jeomix on 8/12/17.
 */

public class Users_Array {
   User user;
   Vehicle vehicle;
   Boolean isOnline;

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public Users_Array(User user) {
        this.user=user;
        this.vehicle=null;
    }

    public Users_Array(Vehicle vehicle){
        this.vehicle=vehicle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
