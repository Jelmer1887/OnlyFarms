package nl.tue.onlyfarms.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.UUID;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.viewmodel.vendor.MystoreViewModel;

public class MyStore extends AppCompatActivity {

    private final String TAG = "MyStore";
    private Store store;
    private ArrayAdapter<CharSequence> adapter;
    private MystoreViewModel model;
    private String toast = "Store Created!";

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

        model = new ViewModelProvider(this).get(MystoreViewModel.class);
        store = new Store(
                UUID.randomUUID().toString(),
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                "",
                "",
                ""
                );

        storeName = findViewById(R.id.storeName);
        description = findViewById(R.id.description);
        address = findViewById(R.id.address);
        fromSpinner = findViewById(R.id.spinner);
        untilSpinner = findViewById(R.id.spinner2);

        adapter = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(adapter);
        untilSpinner.setAdapter(adapter);

        if (getIntent().hasExtra("store")) {
            toast = "Store Updated!";
            ((TextView)findViewById(R.id.addStore)).setText(R.string.editStore);
            ((MaterialToolbar)findViewById(R.id.topBar)).setTitle(R.string.editStore);
            store = (Store) getIntent().getExtras().getSerializable("store");
            prefillFields();
        }

        findViewById(R.id.confirm_store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });

        // Enabled action bar back to Base activity
        setSupportActionBar(findViewById(R.id.topBar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e(TAG, "getSupportActionBar() returned null after 'findViewById(...)' call! -> skipping 'setDisplayHomeAsUpEnabled(true)'");
        }

    }

    private void onSubmit() {
        store.setName(storeName.getText().toString());
        store.setDescription(description.getText().toString());
        store.setPhysicalAddress(address.getText().toString());
//        store.setOpeningTime(fromSpinner.getSelectedItem().toString());
//        store.setClosingTime(untilSpinner.getSelectedItem().toString());
        model.updateStores(store);
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void prefillFields() {
        storeName.setText(store.getName());
        description.setText(store.getDescription());
        address.setText(store.getPhysicalAddress());
//        fromSpinner.setSelection(adapter.getPosition(store.getOpeningTime()));
//        untilSpinner.setSelection(adapter.getPosition(store.getClosingTime()));
    }

    // for back button
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
