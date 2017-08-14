package com.jeomix.android.gpstracker.files;

/**
 * Created by jeomix on 8/5/17.
 */

public class User {

    private String id;
    private String email;
    private String token;
    private String phoneNumber;
    private int isAdmin;//0 : vehicle , 1: admin waiting approval , 2 : admin, 3 :banned
    public User() {
    }

    public User(String id){
        this.id=id;
    }
    public User(String id, String email){
        this(id);
        this.email=email;
    }

//    public Tracks getOnline() {
//        return online;
//    }
//
//    public void setOnline(Tracks online) {
//        this.online = online;
//    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

//    public Vehicle getVehicle() {
//        return vehicle;
//    }
//
//    public void setVehicle(Vehicle vehicle) {
//        this.vehicle = vehicle;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null)
            return false;
        if( getClass()!=obj.getClass())
            return false;
        if(this==obj)
            return true;
        User u= (User) obj;
        if(!this.id.equals(u.getId()))
            return  false;
        return true;

    }

}
