package nl.tue.onlyfarms.view.vendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Product;
import nl.tue.onlyfarms.model.Reservation;
import nl.tue.onlyfarms.view.Base;
import nl.tue.onlyfarms.view.client.RecycleViewAdapterConfirmReservation;
import nl.tue.onlyfarms.viewmodel.ReservationVendorViewModel;

public class FulfillReservationVendor extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ReservationVendorViewModel model;
    private Button reserveButton;
    private Button clearButton;

    private Reservation reservation;
    private Set<Product> productSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirm);

        if (model == null) {
            this.model = new ViewModelProvider(this).get(ReservationVendorViewModel.class);
        }

        // Enabled action bar back to Base activity
        setSupportActionBar(findViewById(R.id.topBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (this.reservation == null) {
            this.reservation = (Reservation) getIntent().getSerializableExtra("reservation");
        }
        if (this.productSet == null) {
            this.productSet = (Set<Product>) getIntent().getSerializableExtra("productSet");
        }
        mAdapter = new RecycleViewAdapterFulfillReservation(this.reservation, this.productSet, FulfillReservationVendor.this);
        recyclerView.setAdapter(mAdapter);

        reserveButton = findViewById(R.id.reserve);
        clearButton = findViewById(R.id.clear);

        reserveButton.setText("Fulfill");
        clearButton.setText("Refuse");

        reserveButton.setOnClickListener(v -> {
            model.fulfillReservation(reservation).addOnCompleteListener(task -> {
               if (task.isSuccessful()) {
                   Toast.makeText(FulfillReservationVendor.this, "Reservation fulfilled", Toast.LENGTH_SHORT).show();
               } else {
                   Toast.makeText(FulfillReservationVendor.this, "Reservation not fulfulled", Toast.LENGTH_SHORT).show();
               }
            });
            goHome();
        });
        clearButton.setOnClickListener(v -> {
            goHome();
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // close current activity and return to previous
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goHome() {
        final Intent intent = new Intent(getApplicationContext(), Base.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
