package nl.tue.onlyfarms.viewmodel;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import nl.tue.onlyfarms.model.FireBaseService;
import nl.tue.onlyfarms.model.Product;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.model.User;

public class HomeViewModel extends ViewModel {
    /* Private fields go here */
    private static final String TAG = "HomeClientViewModel";

    private MutableLiveData<Set<Store>> stores;
    private final MutableLiveData<Set<Store>> filteredStores;
    private MutableLiveData<User> user;

    private final FireBaseService<User> userFireBaseService;
    private final FireBaseService<Store> storeFireBaseService;
    private final MutableLiveData<Boolean> allDataReceived;

    // 'filters' is used to remove unwanted entries in the dataset, applyTo method is defined for this specific variable.
    private final Map<String, Function<Store, Boolean>> filters = new HashMap<>();

    /**
     * Initializes the viewModel:
     * - Creates a monitoring state reporting if the data is received yet
     * */
    public HomeViewModel() {
        this.userFireBaseService = new FireBaseService<>(User.class, "users");
        this.storeFireBaseService = new FireBaseService<>(Store.class, "stores");
        this.allDataReceived = new MutableLiveData<>(false);
        this.user = userFireBaseService.getSingleMatchingField("uid", FirebaseAuth.getInstance().getUid());
        this.filteredStores = new MutableLiveData<>(null);
        // 'stores' is set only by the queries on the database.

        this.user.observeForever(user -> {
            Log.d(TAG, String.format("user in update is %s", user));
            this.allDataReceived.postValue(false);

            if (user == null) { return; }
            Log.d(TAG, "user in update is not null!");
            if (user.getStatus() == User.Status.CLIENT) {
                Log.d(TAG, "user is a client -> retrieving all stores");
                this.stores = storeFireBaseService.getAllAtReference();
            } else {
                Log.d(TAG, "user is a vendor -> retrieving user's stores");
                this.stores = storeFireBaseService.getAllMatchingField("userUid", user.getUid());
            }
            Log.d(TAG, "requested storeData will be in " + this.stores);

            Observer<Set<Store>> storeListener = (storeSet -> {
                if (storeSet == null) { return; }
                Log.d(TAG, "set of Stores received -> allDataReceived = =true");
                applyFilters();
                this.allDataReceived.postValue(true);
            });
            this.stores.observeForever(storeListener);

        });
    }

    public MutableLiveData<User> getUser() { return this.user; }

    public MutableLiveData<Boolean> getAllDataReceived() { return this.allDataReceived; }

    public MutableLiveData<Set<Store>> getStores() { return this.stores; }

    public void addFilter(String name, Function<Store, Boolean> filter) { this.filters.put(name, filter); }

    public void removeFilter(String name) { this.filters.remove(name); }

    public void applyFilters() {
        if (stores == null) { return; }
        if (stores.getValue() == null) { return; }

        final Set<Store> removals = new HashSet<>();
        final Set<Store> filtered = new HashSet<>(stores.getValue());

        // queue any store not resulting in 'true' to be removed
        stores.getValue().forEach(store -> {
            filters.forEach((name, filter) -> {
                Log.d(TAG, String.format("Applying '%s' to %s", name, store.getName()));
                if (!filter.apply(store)) { removals.add(store); }
            });
        });

        StringBuilder s = new StringBuilder();
        for (Store st : removals) { s.append(st.getName()).append(" "); }
        Log.d(TAG, s.append("will be removed.").toString());
        filtered.removeAll(removals);
        this.filteredStores.postValue(filtered);
    }

    public MutableLiveData<Set<Store>> getFilteredStores() { return this.filteredStores; }
}
