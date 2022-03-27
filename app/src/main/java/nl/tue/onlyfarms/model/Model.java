package nl.tue.onlyfarms.model;
/**
 * Generalisation of all our model
 * required for a class to be uploadable to our database via {@link FireBaseService}
 * */
public interface Model {
    String getUid();
    void setUid(String uid);
}
