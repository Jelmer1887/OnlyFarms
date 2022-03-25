package nl.tue.onlyfarms.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;

import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityStoreGeneralBinding;
import nl.tue.onlyfarms.model.Product;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.view.client.RecyclerViewAdapterProductList;

public class StoreGeneral extends AppCompatActivity {
    private static final String TAG = "StoreGeneral";

    private Store store;
    private MutableLiveData<Set<Product>> products;

    private RecyclerView productListView;
    private RecyclerViewAdapterProductList adapter;

    //TODO: implement viewmodel here!
    private ActivityStoreGeneralBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "re-creating Activity");
        super.onCreate(savedInstanceState);
        Log.d(TAG, "restored saved instance (if any)");
        binding = ActivityStoreGeneralBinding.inflate(getLayoutInflater());
        //TODO: initialize model here
        if (this.store == null) {
            this.store = (Store) getIntent().getExtras().getSerializable("store");
        }
        setContentView(R.layout.activity_store_general);

        // Enabled action bar back to Base activity
        setSupportActionBar(findViewById(R.id.topBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.see_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StoreDetail.class);
                intent.putExtra("store", store);
                startActivity(intent);
            }
        });
    }
}