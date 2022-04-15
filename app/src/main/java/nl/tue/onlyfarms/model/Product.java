package nl.tue.onlyfarms.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Product class representing a product in our system
 * This class describes what fields a product should have,
 * as well as getters and setters for each field.
 */
@IgnoreExtraProperties
public class Product extends AbstractNamedModel {
    /**
     * A product has:
     *   - a unique id, used to uniquely identify the product
     *   - a store uid, which is used to identify the store it belongs to
     *   - a price field, which sets the price
     *   - a quantity field, which sets the quantity of the product for the unit
     *   - a unit field, used to say the unit for the given price
     *   - a list of tags, which are used to categorize the product
     */
    private String storeUid;
    private double price;
    private int quantity;
    private String unit;
    // might need to be changed to a single tag idk
    private List<String> tags;
    // TODO: add images
    private int quantityInCart = 0;

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

    public Product() {}

    public String getStoreUid() {
        return storeUid;
    }

    public void setStoreUid(String storeUid) {
        this.storeUid = storeUid;
    }

    public double getPrice() {
        return price;
    }

    public double setPrice(double price) {
        this.price = price;
        return 0;
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
