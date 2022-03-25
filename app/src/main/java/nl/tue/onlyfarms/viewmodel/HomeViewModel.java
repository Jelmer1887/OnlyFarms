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
    public String uid;
    private FireBaseService<User> userFireBaseService;
    private FireBaseService<Store> storeFireBaseService;

    /**
     * Initializes the viewModel:
     * - Retrieves the uid of the logged-in user.
     * - requests the user-data of the logged-in user from the database, stored in 'user'
     * - requests the store-data of the logged-in user after the user-data is received.
     * */
    public HomeViewModel() {
        Log.d(TAG, "retrieving user id from firebase-auth...");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "creating services...");
        userFireBaseService = new FireBaseService<>(User.class, "users");
        storeFireBaseService = new FireBaseService<>(Store.class, "stores");

        if (user == null) {
            Log.e(TAG, "attempted to get user, while no user is logged in");
            throw new IllegalStateException("No user logged in!");
        }
        // reached => user != null
        String uid = user.getUid();
        Log.d(TAG, "user id: " + uid);

        // retrieve data from database upon creation:

        this.user = userFireBaseService.getSingleMatchingField("uid", uid);
        this.user.observeForever( u -> {    // when the user-data is retrieved, use it to fetch the correct stores
            if (u == null) { return; }
            this.stores = (u.getStatus() == User.Status.CLIENT) ?
                    storeFireBaseService.getAllAtReference() :
                    storeFireBaseService.getAllMatchingField("userUid", u.getUid());
        });
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
    }

    public void requestAllStores() {
        Log.d(TAG, "sending request to get all stores to StoreService");
        this.stores = storeFireBaseService.getAllAtReference();
        Log.d(TAG, "stores object has been replaced!");
    }

    /**
     * Retrieves the store objects from this viewModel.
     * NOTE: The value in user will be null-like until the database has returned
     * the data. Use observers!
     * */
    public MutableLiveData<Set<Store>> getStores() { return this.stores; }
}
