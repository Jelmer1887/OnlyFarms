package nl.tue.onlyfarms.viewmodel;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Set;

import nl.tue.onlyfarms.model.FireBaseService;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.model.User;

public class HomeViewModel extends ViewModel {
    /* Private fields go here */
    private static final String TAG = "HomeViewModel";

    private MutableLiveData<Set<Store>> stores;
    private MutableLiveData<User> user;
    private User.Status type;

    private final static FireBaseService<User> userFireBaseService = new FireBaseService<>(User.class, "users");
    private final static FireBaseService<Store> storeFireBaseService = new FireBaseService<>(Store.class, "stores");
    private final MutableLiveData<Boolean> allDataReceived = new MutableLiveData<>();

    /**
     * Initializes the viewModel:
     * - Creates a monitoring state reporting if the data is received yet
     * */
    public HomeViewModel() {
        allDataReceived.postValue(false);
    }

    public void startForUserType(User.Status type) {
        this.type = type;

        this.user = userFireBaseService.getSingleMatchingField("uid", FirebaseAuth.getInstance().getUid());
        if (type == User.Status.VENDOR) {
            this.stores = storeFireBaseService.getAllMatchingField("userUid", FirebaseAuth.getInstance().getUid());
        } else {
            this.stores = storeFireBaseService.getAllAtReference();
        }
        setReceivedFlagObservers("stores");
        setReceivedFlagObservers("user");
    }

    public MutableLiveData<Boolean> getIsDataReceived() {
        if (allDataReceived.getValue() == null) {
            allDataReceived.postValue(this.user != null && this.stores != null);
        }
        return allDataReceived;
    }

    private void setReceivedFlagObservers(String type) {
        if (type.equals("user")) {
            this.user.observeForever(u ->
                    this.allDataReceived.postValue((u != null) && (this.stores.getValue() != null)));
        }
        else {
            this.stores.observeForever(s ->
                    this.allDataReceived.postValue((this.user.getValue() != null) && (s != null)));
        }
    }

    /**
     * ask the database to get the data associated with some user.
     * @param uid unique identifier of the user to retrieve.
     * note: there is no guarantee about when the database returns the requested data... as such
     *            it cannot be used in this view model.
     * note 2: when this method is called, the object stored in user is discarded and replaced,
     *            be careful with it.
     * */
    public void requestUser(String uid){
        Log.d(TAG, "sending request to get data associated with this user to UserService...");
        this.user = userFireBaseService.getSingleMatchingField("uid", uid);
        setReceivedFlagObservers("user");
        Log.d(TAG, "user object has been replaced!");
    }

    /**
     * Retrieves the user object from this viewModel.
     * NOTE: The value in user will be null until the database has returned
     * the data. Use observers!
     * */
    public MutableLiveData<User> getUser() {
        return this.user;
    }

    /**
     * ask the database to get the stores associated with some user.
     * @param userUid unique identifier of the user to retrieve stores for.
     * note: there is no guarantee about when the database returns the requested data... as such
     *            it cannot be used in this view model.
     * note2: calling this method replaces the object stored in 'stores', be careful!
     * */
    public void requestUserStores(String userUid){
        Log.d(TAG, "sending request to get stores associated with this user to StoreService...");
        this.stores = storeFireBaseService.getAllMatchingField("userUid", userUid);
        Log.d(TAG, "stores object has been replaced!");
        setReceivedFlagObservers("stores");
    }

    public void requestAllStores() {
        Log.d(TAG, "sending request to get all stores to StoreService");
        this.stores = storeFireBaseService.getAllAtReference();
        Log.d(TAG, "stores object has been replaced!");
        setReceivedFlagObservers("stores");
    }

    /**
     * Retrieves the store objects from this viewModel.
     * NOTE: The value in user will be null-like until the database has returned
     * the data. Use observers!
     * */
    public MutableLiveData<Set<Store>> getStores() { return this.stores; }
}
