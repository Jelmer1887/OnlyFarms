package nl.tue.onlyfarms.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import kotlin.NotImplementedError;
import nl.tue.onlyfarms.model.FirebaseStoreService;
import nl.tue.onlyfarms.model.FirebaseUserService;
import nl.tue.onlyfarms.model.OurFirebaseDatabase;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.model.User;

public class MystoreViewModel extends ViewModel {
    private static final String Tag = "MystoreViewModel";
    // TODO: Implement the ViewModel
    private MutableLiveData<User> subjectUser;
    private MutableLiveData<Store> userStores;
    private Store currentStore;
    private final FirebaseStoreService storeService = FirebaseStoreService.getInstance();

    /**
     * creates a new viewmodel:
     * <li>Finds the currently logged-in user,</li>
     * <li>Gets their associated data</li>
     * <li>Matches stores in the database with the logged-in user.</li>
     * */
    public MystoreViewModel() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) { throw new IllegalStateException("No user is somehow logged in!"); }
        String uid = user.getUid();

        // retrieve user-data from user-database
        Log.d(Tag, uid);
        MutableLiveData<User> userData = FirebaseUserService.getUser(uid); //TODO: This is dumb
        String test;
        if (userData.getValue() == null) {
            test = "getValue is null";
        } else if (userData.getValue().getUid() == null) {
            test = "getUid is null";
        } else {
            test = "User seems fine?";
        }
        Log.d(Tag, userData.toString());
        userStores = storeService.getStore(uid);  //TODO: This is very dumb
        //TODO: check result of getStore
    }

    /* Retrieves the current user data */
    // note: may be null if the user logged-out or got deleted somehow
    public  LiveData<User> getSubjectUser() { return subjectUser; }

    /* Retrieves all stores associated with the specified user */
    public MutableLiveData<Store> getStores() {
        return userStores;
    }

    /* updates the database with any changes made by the user */
    public void updateStores(Store store) {
        storeService.updateStore(store);
    }
}