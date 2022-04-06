package nl.tue.onlyfarms.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

@IgnoreExtraProperties
public class Product implements Model, Serializable {
    private String uid;
    private String storeUid;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String unit;
    // might need to be changed to a single tag idk
    private List<String> tags;
    // TODO: add images
    private int quantityInCart = 0;

    public Product() {}

    public Product(String uid, String storeUid, String name, String description, double price, int quantity, String unit, List<String> tags) {
        this.uid = uid;
        this.storeUid = storeUid;
        this.name = name;
        this.description = description;
        this.price = price;
        this.tags = tags;
        this.quantity = quantity;
        this.unit = unit;
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

    public String getStoreUid() {
        return storeUid;
    }

    public void setStoreUid(String storeUid) {
        this.storeUid = storeUid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() { return this.quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) { this.tags = tags; }

    public int whatIsInCart() { return this.quantityInCart; }

    public void changeBy(int x) {
        this.quantityInCart += x;
    }
}
