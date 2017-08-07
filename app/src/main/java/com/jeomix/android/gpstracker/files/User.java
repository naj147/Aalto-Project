package com.jeomix.android.gpstracker.files;

/**
 * Created by jeomix on 8/5/17.
 */

public class User {

    private long id;
    private String password;
    private String email;
    private String token;
    private String phoneNumber;
    Boolean isAdmin;
    tracks online;

    public tracks getOnline() {
        return online;
    }

    public void setOnline(tracks online) {
        this.online = online;
    }
    public User(long id){
        this.setId(id);
    }
    Vehicle vehicle;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
