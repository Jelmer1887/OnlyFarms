package nl.tue.onlyfarms.model;

import java.io.Serializable;
import java.util.List;

public class AbstractStoreModel implements Model, Serializable {
    protected String uid;
    protected String name;
    protected String description;


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
}
