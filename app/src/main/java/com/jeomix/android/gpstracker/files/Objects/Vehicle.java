package com.jeomix.android.gpstracker.files.Objects;

/**
 * Created by jeomix on 8/5/17.
 */

public class Vehicle extends VehicleInterface {

    //License Plate Number
    String lpn;

    //Vehicule Identification Number
    String vin;

    public Vehicle() {
    }

    public Vehicle(VehicleType type, String vin) {
        this.type = type;
        this.vin = vin;
    }

    public String getId() {
        return id;
    }

     public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public String getLpn() {
        return lpn;
    }

    public void setLpn(String lpn) {
        this.lpn = lpn;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
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
        if(this.type!=v.getType())
            return  false;
        return this.vin.equals(v.getVin());

    }

    @Override
    public int compareTo(Object o) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

    if(this.equals(o))
        return  EQUAL;
    Vehicle v = (Vehicle)o;
    if(v!=null ){
      if(this.id!=null && v.id!=null && this.id==v.id)
          return  this.id.compareToIgnoreCase(v.id);
    }
        return AFTER;
    }


}
