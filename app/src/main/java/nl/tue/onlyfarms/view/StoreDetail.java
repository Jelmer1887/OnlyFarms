package nl.tue.onlyfarms.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Locale;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityStoreDetailBinding;
import nl.tue.onlyfarms.model.Store;

public class StoreDetail extends AppCompatActivity {
    private static final String TAG = "StoreDetails";
    private Store store;
    private ActivityStoreDetailBinding binding;

    private TextView nameField;
    private TextView descriptionField;
    private TextView addressField;
    private TextView openField;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "(re)creating Activity");
        super.onCreate(savedInstanceState);
        binding = ActivityStoreDetailBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_store_detail);

        // check if state important variables were restored (!= null) by savedInstanceState.
        StringBuilder debug = new StringBuilder();
        if (this.store == null) {
            this.store = (Store) getIntent().getExtras().getSerializable("store");
        }

        setContentView(R.layout.activity_store_detail);

        this.nameField = findViewById(R.id.storeDetail_store_name);
        this.descriptionField = findViewById(R.id.storeDetail_description_placeholder);
        this.addressField = findViewById(R.id.storeDetail_address);
        this.openField = findViewById(R.id.storeDetail_open);

        StringBuilder description = new StringBuilder();

        ((MaterialToolbar)findViewById(R.id.storeDetail_topBar)).setTitle(store.getName());
        nameField.setText(store.getName());
        descriptionField.setText(description.append(store.getDescription()));
        addressField.append(" " + store.getPhysicalAddress());
        openField.append(String.format(Locale.ROOT, " %s - %s", store.getOpeningTime(), store.getClosingTime()));

        // Enabled action bar back to Base activity
        setSupportActionBar(findViewById(R.id.storeDetail_topBar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e(TAG, "getSupportActionBar() returned null after 'findViewById(...)' call! -> skipping 'setDisplayHomeAsUpEnabled(true)'");
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // close current activity and return to previous
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}