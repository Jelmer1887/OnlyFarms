package nl.tue.onlyfarms.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

/*
* User class representing a user in our system.
* This class descripes what fields a user should have for
* data to be stored, as well as containing
* setters and getters for all fields.
*
* The class extends a general Model, which contains setters and getters for UID's
* This is required to allow this class to be uploaded, retrieved to FireBase.
* */
public class User implements Model {
    /*
    * A user has:
    * - a unique identifier, usually created by FireBaseAuthentication services upon registration
    * - a userName to easily display inside the app
    * - a First name
    * - a Last name
    * - an email address, which has to be properly formatted (checked by FireBase)
    * - a status, a user is either a VENDOR or a CLIENT
    * */

    /*
    * The status of a user, used to identify what kind of subtype a user is
    * The user functionality changes based on whether they are:
    * - a VENDOR, to see their stores, etc...
    * - a CLIENT, to see offers, etc...
    */
    public enum Status {
        VENDOR,
        CLIENT
    }

    // Fields to store relevant user data
    private String uid;             // identifier of the user as string, used to match the authentication database data with the user database info
    private String userName;        // short display name
    private String firstName;       // legal first name
    private String lastName;        // actual last name
    private String emailAddress;    // emailAddress, should match the same field
    private Status status;          // status, as Status defined above, either VENDOR or CLIENT

    /* Obligatory empty constructor for FireBase */
    public User() {}


    /* Actually used constructor inside the app */
    public User(String uid, String userName, String firstName, String lastName, String emailAddress, Status status) {
        this.uid = uid;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.status = status;
    }


    /*--- Setters and Getters ---*/

    // getUid and setUid are defined here again, to overwrite and implement behaviour of Model
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
