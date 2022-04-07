package nl.tue.onlyfarms.model;


import org.osmdroid.util.GeoPoint;
import java.io.Serializable;

public class Store implements Serializable, Model {
    private String uid;
    private String userUid;
    private String name;
    private String description;
    private String physicalAddress;
    private GeoPoint coordinates;
    private String emailAddress;
    private String phoneNumber;
    private String openingTime;
    private String closingTime;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
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

    public GeoPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = new GeoPoint(coordinates[0], coordinates[1]);
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
}
