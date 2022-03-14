package nl.tue.onlyfarms.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import kotlin.NotImplementedError;
import nl.tue.onlyfarms.model.FirebaseStoreService;
import nl.tue.onlyfarms.model.FirebaseUserService;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.model.User;

public class MystoreViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<User> subjectUser;
    private MutableLiveData<List<Store>> userStores;
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
        User userData = FirebaseUserService.getUser(uid);       //TODO: This is dumb
        userStores = storeService.getStore(userData.getUid());  //TODO: This is very dumb

        //TODO: check result of getStore
    }

    /* Retrieves the current user data */
    public  LiveData<User> getSubjectUser() {
        return subjectUser; // note: may be null if the user logged-out or got deleted somehow
    }

    /* Retrieves all stores associated with the specified user */
    public LiveData<List<Store>> getStores() {
        return userStores;
    }

    /* Updates the userStores variable to reflect the newest subjectUser's data in the DataBase */
    private void updateStores() {}
}