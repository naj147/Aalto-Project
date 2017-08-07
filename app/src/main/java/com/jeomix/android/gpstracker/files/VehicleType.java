package com.jeomix.android.gpstracker.files;

/**
 * Created by jeomix on 8/5/17.
 */

/**
 * These Types are for Testing purposes only and to show the functionalities of the system, they are extendable
 */
public enum VehicleType {
    motorCycle(0),
    car(1),
    truck(2),
    bus(3);
    private final int type;
    VehicleType(int type)
    {
        this.type = type;
    }
    public int getType() {
        return type;
    }
}
