package nl.tue.onlyfarms.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import java.io.Serializable;
import java.util.Locale;
import java.util.Random;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityStoreGeneralBinding;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.view.client.ConfirmReservationClient;
import nl.tue.onlyfarms.view.client.RecyclerViewAdapterProductList;
import nl.tue.onlyfarms.view.vendor.AddProduct;
import nl.tue.onlyfarms.viewmodel.ProductViewModel;

public class StoreGeneral extends AppCompatActivity implements RecyclerViewAdapterProductList.ItemClickListener {
    private static final String TAG = "StoreGeneral";

    private Store store;

    private SearchView searchBar;
    private final MutableLiveData<String> searchText = new MutableLiveData<>("");

    private TextView storeNameField;
    private TextView storeAddressField;
    private TextView storeOpeningHoursField;
    private TextView storeDescriptionField;

    private RecyclerView productListView;
    private RecyclerViewAdapterProductList adapter = new RecyclerViewAdapterProductList();

    private ProductViewModel model;
    private ActivityStoreGeneralBinding binding;

    private final Random generator = new Random();

    @SuppressLint({"NotifyDataSetChanged", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "(re)creating Activity");
        super.onCreate(savedInstanceState);
        binding = ActivityStoreGeneralBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_store_general);

        // check if state important variables were restored (!= null) by savedInstanceState.
        StringBuilder debug = new StringBuilder();
        if (model == null) {
            this.model = new ViewModelProvider(this).get(ProductViewModel.class);
            model.setStore(store);  // setting the store requests the product-data.
        }
        if (this.store == null) {
            this.store = (Store) getIntent().getExtras().getSerializable("store");
        }
        if (this.productListView == null) {
            this.productListView = findViewById(R.id.storeGeneral_recyclerview);
        }
        if (this.searchBar == null) {
            this.searchBar = findViewById(R.id.storeGeneral_search);
        }

        /* Search bar related */
        // follows: https://stackoverflow.com/questions/19588311
        searchBar.setIconifiedByDefault(false);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            /* user input of search-balk is available in this method's scope */
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, String.format("search text entered is: '%s'", newText));
                if (newText.length() == 0) {
                    model.removeFilter("searchText");
                    model.applyFilters();
                    return false;
                }
                model.addFilter("searchText", product ->
                        product.getName().toLowerCase(Locale.ROOT)
                                .contains(newText.toLowerCase(Locale.ROOT)));
                model.applyFilters();
                return false;
            }
        });

        // retrieve and set store-fields of the ui to their values.
        this.storeNameField = findViewById(R.id.storeGeneral_store_name);
        this.storeAddressField = findViewById(R.id.storeGeneral_address);
        this.storeOpeningHoursField = findViewById(R.id.storeGeneral_opening_hours);
        this.storeDescriptionField = findViewById(R.id.storeGeneral_description);

        ((MaterialToolbar)findViewById(R.id.topBar)).setTitle(store.getName());
        storeNameField.setText(store.getName());
        storeAddressField.append(" " + store.getPhysicalAddress());
        storeOpeningHoursField.append(String.format(Locale.ROOT, " %s - %s", store.getOpeningTime(), store.getClosingTime()));
        storeDescriptionField.setText(store.getDescription());


        // ensure store is set in model (required to get product data from database)
        if (model.getStore().getValue() == null) {
            Log.d(TAG, "model store was null, updating to " + store);
            model.setStore(store);
        }

        // Enabled action bar back to Base activity
        setSupportActionBar(findViewById(R.id.topBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Product list configuration
        productListView.setHasFixedSize(true);
        productListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // Product list initial creation
        Log.d(TAG, "Expecting data to be unavailable ( which is actually "+ (!model.getAllDataReceived().getValue()) +"). Using empty adapter");
        productListView.setAdapter(adapter);


        // Listeners -------------

        // update listener -> updates recyclerview adapter when data is received
        // required to update productList when data is changed or not received yet.
        model.getAllDataReceived().observe(this, receivedData -> {
            StringBuilder debug1 = new StringBuilder("State of receivedData changed to ");

            if (receivedData) {
                debug1.append(true).append(" -> swapping adapter from ")
                        .append(adapter).append(" to ProductListAdapter");
                adapter.setData(this, model.getFilteredProductData());
                Log.d(TAG, "onCreate: activity becomes listener");
                model.getFilteredProductData().observe(this, s -> adapter.notifyDataSetChanged());
            }

            Log.d(TAG, debug1.toString());
        });

        // 'see more' button listener -> go to detailed store page
        findViewById(R.id.storeGeneral_see_more).setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), StoreDetail.class);
            intent.putExtra("store", store);
            startActivity(intent);
        });

        // 'go to cart' button listener -> goes to reservation confirmation
        findViewById(R.id.storeGeneral_reserve).setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ConfirmReservationClient.class);
            intent.putExtra("products", (Serializable) model.getFilteredProductData().getValue());
            startActivity(intent);
        });

        // Change stuff here if vendor
        if (getIntent().hasExtra("isClient")) {
            // make cards clickable to edit product
            adapter.setClickListener(this);

            // change see more to edit store
            TextView seeMore = findViewById(R.id.storeGeneral_see_more);
            seeMore.setText(R.string.editStore);
            seeMore.setOnClickListener(view -> {
                startActivity(new Intent(this, MyStore.class).putExtra("store", store));
            });

            // change make reservation to add product
            Button makeReservation = findViewById(R.id.storeGeneral_reserve);
            makeReservation.setText(R.string.addProduct);
            makeReservation.setOnClickListener(view -> {
                startActivity(new Intent(this, AddProduct.class).putExtra("storeUid", store.getUid()));
            });
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: We got this far");
        if (adapter == null) {
            throw new NullPointerException("adapter not set! (null)");
        }

        Intent intent = new Intent(this, AddProduct.class);
        intent.putExtra("product", adapter.getItem(position));
        Log.d(TAG, "Going to AddProduct with intent: " + intent);
        startActivity(intent);
    }
}