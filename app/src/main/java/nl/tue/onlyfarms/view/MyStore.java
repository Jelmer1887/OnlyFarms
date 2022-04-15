package nl.tue.onlyfarms.view;

import android.location.Address;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import org.osmdroid.bonuspack.location.GeocoderNominatim;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.view.vendor.AbstractBackActivity;
import nl.tue.onlyfarms.viewmodel.vendor.MystoreViewModel;

public class MyStore extends AbstractBackActivity {

    private final String TAG = "MyStore";
    private Store store;
    private ArrayAdapter<CharSequence> adapter;
    private MystoreViewModel model;
    private String toast = "Store Created!";
    private GeocoderNominatim geocoder;

    // input fields
    private EditText storeName;
    private EditText description;
    private EditText address;
    private Spinner fromSpinner;
    private Spinner untilSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystore);

        // necessary for getting location from name
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        geocoder = new GeocoderNominatim("store");

        model = new ViewModelProvider(this).get(MystoreViewModel.class);

        // empty store that will be filled with the correct data
        store = new Store(
                UUID.randomUUID().toString(),
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                "",
                "",
                ""
                );

        // store important fields
        storeName = findViewById(R.id.storeName);
        description = findViewById(R.id.description);
        address = findViewById(R.id.tvStoreAddress);
        fromSpinner = findViewById(R.id.spinner);
        untilSpinner = findViewById(R.id.spinner2);

        adapter = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(adapter);
        untilSpinner.setAdapter(adapter);

        // only executed when editing an existing store
        if (getIntent().hasExtra("store")) {
            toast = "Store Updated!";
            ((TextView)findViewById(R.id.addStore)).setText(R.string.editStore);
            ((MaterialToolbar)findViewById(R.id.topBar)).setTitle(R.string.editStore);
            store = (Store) getIntent().getExtras().getSerializable("store");
            prefillFields();
        }

        findViewById(R.id.confirm_store).setOnClickListener(this::onSubmit);
    }

    /*
     * Gets values from the fields and uploads the store to firebase (create/replace).
     */
    private void onSubmit(View v) {
        store.setName(storeName.getText().toString());
        store.setDescription(description.getText().toString());
        store.setOpeningTime(fromSpinner.getSelectedItem().toString());
        store.setClosingTime(untilSpinner.getSelectedItem().toString());


        // IOException if request cannot be made.
        try {
            List<Address> androidAddress = geocoder.getFromLocationName(address.getText().toString(), 1);
            for ( Address address : androidAddress) {
                store.setLatitude(address.getLatitude());
                store.setLongitude(address.getLongitude());

                String street = address.getThoroughfare();
                street = street == null ? "" : street + " ";
                String number = address.getSubThoroughfare();
                number = number == null ? "" : number;
                String city = address.getLocality();
                String subLoc = address.getSubLocality();
                city = city == null ? subLoc == null ? "" : subLoc + ", " : city + ", ";

                store.setPhysicalAddress(String.format("%s%s%s", city, street, number));
                Log.d(TAG, String.format("%s%s%s", city, street, number));
            }
        } catch (IOException e) {
            Log.d(TAG, "Something went horribly wrong! " + e.getMessage());
        }

        // updates/creates store
        model.updateStores(store);
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        finish();
    }

    /*
     * Sets the fields with the information that is already stored in this.store
     */
    private void prefillFields() {
        storeName.setText(store.getName());
        description.setText(store.getDescription());
        address.setText(store.getPhysicalAddress());
        fromSpinner.setSelection(adapter.getPosition(store.getOpeningTime()));
        untilSpinner.setSelection(adapter.getPosition(store.getClosingTime()));
    }

}
