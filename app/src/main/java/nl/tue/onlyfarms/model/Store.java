package nl.tue.onlyfarms.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Store extends AbstractStoreModel {
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

    public List<Double> getCoordinates() {
        List<Double> coords = new ArrayList<Double>();
        coords.add(latitude);
        coords.add(longitude);
        return coords;
    }

    public void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
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


    public double getDistance(){
        return getDistance(this.name);
    }

    public double getDistance(String name) {
        if (this.name.toLowerCase(Locale.ROOT).equals("happy farm animals")) {
            return 0.10133;
        } else if (this.name.toLowerCase(Locale.ROOT).equals("unhappy farm animals")){
            return 98.02031;
        } else {
            Random gen = new Random();
            return 173.23526 + ((double) gen.nextInt(10000) / 10000.0)*1;
        }
    }
}
