package nl.tue.onlyfarms.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Set;

import nl.tue.onlyfarms.model.FirebaseStoreService;
import nl.tue.onlyfarms.model.FirebaseUserService;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.model.User;

public class HomeViewModel extends ViewModel {
    /* Private fields go here */
    private static final String TAG = "HomeViewModel";

    private MutableLiveData<Set<Store>> stores;
    private MutableLiveData<User> user;


    public HomeViewModel() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e(TAG, "attempted to get user, while no user is logged in");
            throw new IllegalStateException("No user logged in!");
        }
        // reached => user != null
        String uid = user.getUid();

        Log.d(TAG, "sending requests to retrieve data...");
        requestUser(uid);
        requestStores(uid);
    }

    /**
     * ask the database to get the data associated with some user.
     * @param uid unique identifier of the user to retrieve.
     * note: there is no guarantee about when the database returns the requested data... as such
     *            it cannot be used in this view model.
     * */
    public void requestUser(String uid){
        Log.d(TAG, "sending request to get data associated with this user to UserService...");
        this.user = FirebaseUserService.getUser(uid);
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
     * */
    public void requestStores(String userUid){
        Log.d(TAG, "sending request to get stores associated with this user to StoreService...");
        this.stores = FirebaseStoreService.getStores(userUid);
    }

    /**
     * Retrieves the store objects from this viewModel.
     * NOTE: The value in user will be null-like until the database has returned
     * the data. Use observers!
     * */
    public MutableLiveData<Set<Store>> getStores() { return this.stores; }
}
