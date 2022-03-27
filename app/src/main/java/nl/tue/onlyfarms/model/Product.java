package nl.tue.onlyfarms.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Product implements Model{
    private String uid;
    private String storeUid;
    private String name;
    private String description;
    private double price;
    private String unit;
    // might need to be changed to a single tag idk
    private List<String> tags;
    // TODO: add images

    public Product() {}

    public Product(String uid, String storeUid, String name, String description, double price, List<String> tags) {
        this.uid = uid;
        this.storeUid = storeUid;
        this.name = name;
        this.description = description;
        this.price = price;
        this.tags = tags;
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
}
