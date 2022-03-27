package nl.tue.onlyfarms.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityStoreGeneralBinding;
import nl.tue.onlyfarms.model.Product;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.view.client.RecyclerViewAdapterProductList;
import nl.tue.onlyfarms.viewmodel.ProductViewModel;

public class StoreGeneral extends AppCompatActivity {
    private static final String TAG = "StoreGeneral";

    private Store store;

    private RecyclerView productListView;
    private RecyclerViewAdapterProductList adapter;

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

        // check if variables were restored (!= null) by savedInstanceState.
        StringBuilder debug = new StringBuilder();
        if (model == null) {
            this.model = new ViewModelProvider(this).get(ProductViewModel.class);
            model.setStore(store);  // setting the store requests the product-data.
            debug.append("\n o model = ").append(model);
        }
        if (this.store == null) {
            this.store = (Store) getIntent().getExtras().getSerializable("store");
            debug.append("\n o store = ").append(store);
        }
        if (this.productListView == null) {
            this.productListView = findViewById(R.id.storeGeneral_recyclerview);
            debug.append("\n o productList = ").append(productListView);
        }

        Log.d(TAG, debug.toString());

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
        Log.d(TAG, "Expecting data to be unavailable ( is actually: "+ model.getAllDataReceived().getValue() +"). Using empty adapter");
        productListView.setAdapter(new RecyclerViewAdapterEmpty());


        // Listeners -------------

        // update listener -> updates recyclerview adapter when data is received
        // required to update productList when data is changed or not received yet.
        model.getAllDataReceived().observe(this, receivedData -> {
            StringBuilder debug1 = new StringBuilder("State of receivedData changed to ");
            if (receivedData) {
                debug1.append(true).append(" -> swapping adapter from ")
                        .append(adapter).append(" to ProductListAdapter");
                adapter = new RecyclerViewAdapterProductList(this, model.getProductData());
                model.getProductData().observe(this, s -> adapter.notifyDataSetChanged());
                productListView.swapAdapter(adapter, true);
            } else {
                debug1.append(false).append(" -> swapping adapter from ")
                        .append(adapter).append(" to emptyAdapter");
                productListView.swapAdapter(new RecyclerViewAdapterEmpty(), true);
            }
            Log.d(TAG, debug1.toString());
        });

        // 'see more' button listener -> go to detailed store page
        findViewById(R.id.storeGeneral_see_more).setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), StoreDetail.class);
            intent.putExtra("store", store);
            startActivity(intent);
        });

        // 'make this reservation' button listener -> create a new product (TODO: replace with actual func)
        findViewById(R.id.storeGeneral_reserve).setOnClickListener(view -> {
            //Create new random product with random quantity
            String[] tags = new String[]{"spicey", "disgusting", "'chicken'"};
            Set<String> chosenTags = new HashSet<>();
            chosenTags.add(tags[generator.nextInt(3)]);
            chosenTags.add(tags[generator.nextInt(3)]);
            double price = generator.nextDouble() % 50.00;
            Log.d(TAG, "price is " + price);
            price = ((double) ((int) (price * 100)) ) / 100;
            Log.d(TAG, "price is now " + price);
            Product product = new Product(
                    UUID.randomUUID().toString(),
                    store.getUid(),     // associates store with product
                    String.valueOf(new Random().nextInt() % 200),   // this should be replaced with actual data
                    String.valueOf(new Random().nextLong()),        // this should be replaced with actual data
                    price,                                          // this should be replaced with actual data
                    new ArrayList<>(chosenTags)                     // 2 or 1 tag chosen from the random list above
            );

            model.UploadProduct(product).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "product added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "failed to create product! :(", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}