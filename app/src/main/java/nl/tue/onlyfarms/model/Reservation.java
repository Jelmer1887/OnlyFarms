package nl.tue.onlyfarms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Reservation implements Model, Serializable {
    private String uid;
    private String storeUid;
    private String userUid;
    // products and quantities with product-uid's as keys
    private HashMap<String, Integer> products;
    private Date date;

    public Reservation() {}

    public Reservation(
            String uid,
            String storeUid,
            String userUid,
            Map<String, Integer> products,
            Date creationDate
    ) {
        this.uid = uid;
        this.storeUid = storeUid;
        this.userUid = userUid;
        this.products = (HashMap<String, Integer>) products;
        this.date = creationDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStoreUid() {
        return storeUid;
    }

    public void setStoreUid(String storeUid) {
        this.storeUid = storeUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public HashMap<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<String, Integer> products) {
        this.products = products;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
