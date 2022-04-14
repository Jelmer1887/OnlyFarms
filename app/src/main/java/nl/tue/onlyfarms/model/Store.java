package nl.tue.onlyfarms.model;


import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Store extends AbstractNamedModel {
    private String userUid;
    private String physicalAddress;
    private String emailAddress;
    private String phoneNumber;
    private String openingTime;
    private String closingTime;
    private double longitude;
    private double latitude;
    // TODO: add images (could)

    // mandatory empty constructor
    public Store(){};

    //TODO: add other fields
    public Store(String uid, String userUid, String name, String description, String physicalAddress) {
        this.uid = uid;
        this.userUid = userUid;
        this.name = name;
        this.description = description;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public double getDistance(Location user) {
        Location store = new Location(LocationManager.GPS_PROVIDER);
        store.setLatitude(latitude);
        store.setLongitude(longitude);

        return store.distanceTo(user) / 1000.0;
    }
}
