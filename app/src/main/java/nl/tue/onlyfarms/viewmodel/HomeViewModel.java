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
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private MutableLiveData<Set<Product>> products;
    private MutableLiveData<Set<Product>> productsMatching;

    private final FireBaseService<User> userFireBaseService;
    private final FireBaseService<Store> storeFireBaseService;
    private final FireBaseService<Product> productFireBaseService;
    private final MutableLiveData<Boolean> allDataReceived;

    // 'filters' is used to remove unwanted entries in the dataset, applyTo method is defined for this specific variable.
    private final Map<String, Function<Store, Boolean>> filters = new HashMap<>();
    private final Map<String, Boolean> filterMode = new HashMap<>();

    public final static boolean AND = true;
    public final static boolean OR = false;

    /**
     * Initializes the viewModel:
     * - Creates a monitoring state reporting if the data is received yet
     * */
    public HomeViewModel() {
        this.userFireBaseService = new FireBaseService<>(User.class, "users");
        this.storeFireBaseService = new FireBaseService<>(Store.class, "stores");
        this.productFireBaseService = new FireBaseService<>(Product.class, "products");
        this.allDataReceived = new MutableLiveData<>(false);
        this.user = userFireBaseService.getSingleMatchingField("uid", FirebaseAuth.getInstance().getUid());
        this.products = productFireBaseService.getAllAtReference(); // all products are retrieved to make searching faster
        this.productsMatching = new MutableLiveData<>(new HashSet<>());
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

        this.products.observeForever(productSet -> {
            Log.d(TAG, "product set received: " + productSet);
            if (productSet == null) { return; }
            applyFilters();
        });
    }

    public MutableLiveData<User> getUser() { return this.user; }

    public MutableLiveData<Boolean> getAllDataReceived() { return this.allDataReceived; }

    public MutableLiveData<Set<Store>> getStores() { return this.stores; }

    public void addFilter(String name, Function<Store, Boolean> filter) {
        this.filters.put(name, filter);
        this.filterMode.put(name, AND);
    }
    public void addFilter(String name, Boolean MODE, Function<Store, Boolean> filter) {
        this.filters.put(name, filter);
        this.filterMode.put(name, MODE ? AND : OR);
    }

    public void removeFilter(String name) { this.filters.remove(name); this.filterMode.remove(name); }

    public void applyFilters() {
        if (stores == null) { return; }
        if (stores.getValue() == null) { return; }

        final Set<Store> removals = new HashSet<>();
        final Set<Store> whiteList = new HashSet<>();
        final Set<Store> filtered = new HashSet<>(stores.getValue());

        // queue any store not resulting in AND 'true' to be removed, any OR 'true' will always be kept.
        stores.getValue().forEach(store -> {
            filters.forEach((name, filter) -> {
                assert filterMode.containsKey(name);
                Log.d(TAG, String.format("Applying '%s' to %s", name, store.getName()));

                if (filterMode.get(name) == OR) {
                    Log.d(TAG, "filter is OR filter");
                    if (filter.apply(store)) {
                        whiteList.add(store);
                    }
                } else {
                    if (!filter.apply(store)) {
                        removals.add(store);
                    }
                }
            });
        });

        removals.removeAll(whiteList);

        StringBuilder s = new StringBuilder();
        for (Store st : removals) { s.append(st.getName()).append(" "); }
        Log.d(TAG, s.append("will be removed.").toString());
        filtered.removeAll(removals);
        this.filteredStores.postValue(filtered);
    }

    public MutableLiveData<Set<Store>> getFilteredStores() { return this.filteredStores; }

    public MutableLiveData<Set<Product>> getProducts() { return this.products; }

    public Set<Product> getProductsMatchingName(String name) {
        // return empty set to prevent database slowness from crashing the app when values are null.
        if (this.products == null) { return new HashSet<>(); }
        if (this.products.getValue() == null) {return new HashSet<>(); }

        final Set<Product> productSet = new HashSet<>();
        products.getValue().forEach(product -> {
            if (product.getName().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))) {
                productSet.add(product);
            }
        });

        return productSet;
    }

    public boolean storeHasProductInSet(Store store, Set<Product> productSet) {
        assert store != null && productSet != null;
        return !productSet.stream().filter(
                product -> product.getStoreUid().equals(store.getUid()) // this is the actual filter predicate (product's store == given store)
        ).collect(Collectors.toSet()).isEmpty();
    }
}
