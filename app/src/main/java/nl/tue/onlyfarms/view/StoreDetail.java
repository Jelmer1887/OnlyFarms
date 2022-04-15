package nl.tue.onlyfarms.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Locale;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.view.vendor.AbstractBackActivity;

public class StoreDetail extends AbstractBackActivity {
    private static final String TAG = "StoreDetails";
    private Store store;

    private TextView nameField;
    private TextView descriptionField;
    private TextView addressField;
    private TextView openField;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "(re)creating Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        // check if state important variables were restored (!= null) by savedInstanceState.
        StringBuilder debug = new StringBuilder();
        if (this.store == null) {
            this.store = (Store) getIntent().getExtras().getSerializable("store");
        }

        setContentView(R.layout.activity_store_detail); // set the layout

        // store important fields
        nameField = findViewById(R.id.storeDetail_store_name);
        descriptionField = findViewById(R.id.storeDetail_description_placeholder);
        addressField = findViewById(R.id.storeDetail_address);
        openField = findViewById(R.id.storeDetail_open);

        StringBuilder description = new StringBuilder();

        // set fields according to correct values
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
}