package nl.tue.onlyfarms.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import nl.tue.onlyfarms.model.FireBaseService;
import nl.tue.onlyfarms.model.Product;
import nl.tue.onlyfarms.model.Store;

public class ProductViewModel extends ViewModel {
    private static final String TAG = "ProductViewModel";

    private final FireBaseService<Product> productFireBaseService;
    private final MutableLiveData<Boolean> allDataReceived;

    private final List<Observer<Set<Product>>> activeProductObservers;

    private final MutableLiveData<Store> store;
    private MutableLiveData<Set<Product>> productData;
    private final MutableLiveData<Set<Product>> filteredProductData;

    // 'filters' is used to remove unwanted entries in the dataset, applyTo method is defined for this specific variable.
    private final Map<String, Function<Product, Boolean>> filters = new HashMap<>();

    /**
     * Creates a new instance of this viewModel
     * and creates the dataReceived flag (set to false)
     * */
    public ProductViewModel() {
        this.productFireBaseService =   new FireBaseService<>(Product.class, "products");
        this.activeProductObservers =   new ArrayList<>();
        this.allDataReceived =          new MutableLiveData<>(false);
        this.store =                    new MutableLiveData<>(null);
        this.filteredProductData =      new MutableLiveData<>(null);

        // prevents 'out-of-stock' products from showing up
        this.filters.put("out_of_stock", product -> product.getQuantity() > 0);

        this.store.observeForever(store -> {
            // Old product data should be removed when the store changes to prevent data-leaks
            if (this.productData != null) {
                // observers at product are explicitly removed to prevent those at no-longer
                // relevant products putting those products in the current dataset.
                Log.e(TAG, "store changed! storeObserver is removing all productObservers! -> allDataReceived = false");
                this.activeProductObservers.forEach( observer -> this.productData.removeObserver(observer) );
                this.activeProductObservers.clear();
                this.allDataReceived.postValue(false);
                this.productData = null;
            }

            // store cannot be null when trying to get new product data for a store to show.
            if (store == null) { return; }

            this.productData = productFireBaseService.getAllMatchingField("storeUid", store.getUid());
            Log.d(TAG, "requested product data from service, result will be in " + this.productData);

            // product get added to the dataset as they are fetched from the database.
            Observer<Set<Product>> productListener = (products -> {
                if (products == null) { return; }
                Log.e(TAG, "productData is just received -> allDataReceived = true");
                applyFilters();
                this.allDataReceived.postValue(true);
            });

            this.productData.observeForever(productListener);
            activeProductObservers.add(productListener);
        });
    }

    public void setStore(Store store) {
        this.store.postValue(store);
    }

    public MutableLiveData<Store> getStore() {
        return store;
    }

    public MutableLiveData<Boolean> getAllDataReceived() { return allDataReceived; }

    public MutableLiveData<Set<Product>> getProductData() { return productData; }

    public void addFilter(String name, Function<Product, Boolean> filter) { this.filters.put(name, filter); }

    public void removeFilter(String name) { this.filters.remove(name); }

    public void applyFilters() {
        if (productData == null) {return;}
        if (productData.getValue() == null) {return;}

        final Set<Product> removals = new HashSet<>();
        final Set<Product> filtered = new HashSet<>(productData.getValue());

        // removes values of received data based on filters in 'filters'
        productData.getValue().forEach((product -> {
            filters.forEach((name, filter) -> {
                Log.d(TAG, String.format("Applying '%s' to %s", name, product.getName()));
                if (!filter.apply(product)) { removals.add(product); }
            });
        }));
        StringBuilder s = new StringBuilder();
        for (Product p : removals) { s.append(p.getName()).append(" "); }
        Log.d(TAG, s.append("will be removed.").toString());
        filtered.removeAll(removals);
        this.filteredProductData.postValue(filtered);
    }

    public void applyFilters(String name) {
        if (productData == null) {return;}
        if (productData.getValue() == null) {return;}
        Function<Product, Boolean> filter = filters.get(name);
        if (filter == null) { Log.e(TAG, "filter not found!"); return; }

        final Set<Product> removals = new HashSet<>();
        final Set<Product> filtered = new HashSet<>(productData.getValue());

        // removes values of received data based on filters in 'filters'
        productData.getValue().forEach((product -> {
            Log.d(TAG, String.format("Applying '%s' to %s", name, product.getName()));
            if (!filter.apply(product)) { removals.add(product); }
        }));
        StringBuilder s = new StringBuilder();
        for (Product p : removals) { s.append(p.getName()).append(" "); }
        Log.d(TAG, s.append("will be removed.").toString());
        filtered.removeAll(removals);
        this.filteredProductData.postValue(filtered);
    }

    public MutableLiveData<Set<Product>> getFilteredProductData() { return this.filteredProductData; }

    public Task<Void> UploadProduct(Product p) {
        return productFireBaseService.updateToDatabase(p, p.getUid());
    };
}
