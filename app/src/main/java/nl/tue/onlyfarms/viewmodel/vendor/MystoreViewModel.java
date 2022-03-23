package nl.tue.onlyfarms.viewmodel.vendor;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Set;

import nl.tue.onlyfarms.model.FireBaseService;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.model.User;

public class MystoreViewModel extends ViewModel {
    public MutableLiveData<User> userData;
    private static final String Tag = "MystoreViewModel";
    // TODO: Implement the ViewModel
    private MutableLiveData<User> subjectUser;
    private MutableLiveData<Set<Store>> userStores;
    private Store currentStore;

    private FireBaseService<Store> storeFireBaseService;
    private FireBaseService<User> userFireBaseService;

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

        // initialize services
        storeFireBaseService = new FireBaseService<>(Store.class, "stores");
        userFireBaseService = new FireBaseService<>(User.class, "users");

        this.userStores = storeFireBaseService.getResults();
        this.subjectUser = userFireBaseService.getFirstResult();

        Log.i("viewModel","got user:" + subjectUser.getValue());
        //userStores = FirebaseStoreService.getStores(uid);
        //TODO: check result of getStore
    }

    public void requestUserUpdate(LifecycleOwner lifecycleOwner, String uid) {
        userFireBaseService.getFirstMatchingField("uid", uid);
    }

    public void requestStoreUpdate(String uid) {
       storeFireBaseService.getAllMatchingField("userUid", uid);
    }

    /* Retrieves the current user data */
    // note: may be null if the user logged-out or got deleted somehow
    public  MutableLiveData<User> getSubjectUser() { return subjectUser; }

    /* Retrieves all stores associated with the specified user */
    public MutableLiveData<Set<Store>> getStores() {
        return userStores;
    }

    /* updates the database with any changes made by the user */
    public void updateStores(Store store) {
        storeFireBaseService.updateToDatabase(store, store.getUid());
    }
}