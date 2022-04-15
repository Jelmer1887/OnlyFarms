package nl.tue.onlyfarms.view.client;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.model.Product;
import nl.tue.onlyfarms.view.AbstractReservationView;
import nl.tue.onlyfarms.viewmodel.ReservationClientViewModel;

public class ConfirmReservationClient extends AbstractReservationView {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ReservationClientViewModel model;
    private Button reserveButton;
    private Button clearButton;
    
    private Set<Product> products;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirm);

        if (model == null) {
            this.model = new ViewModelProvider(this).get(ReservationClientViewModel.class);
        }

        // Enabled action bar back to Base activity
        setSupportActionBar(findViewById(R.id.topBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (this.products == null) {
            this.products = (Set<Product>) getIntent().getExtras().getSerializable("products");
            for (Product product : products) {
                model.updateUnconfirmedReservation(product);
            }
        }

        mAdapter = new RecycleViewAdapterConfirmReservation(this.products, ConfirmReservationClient.this);
        recyclerView.setAdapter(mAdapter);

        reserveButton = findViewById(R.id.reserve);
        clearButton = findViewById(R.id.clear);
        reserveButton.setOnClickListener(v -> {
            model.confirmReservation().addOnCompleteListener(task -> {
               if (task.isSuccessful()) {
                   Toast.makeText(ConfirmReservationClient.this, "Reservation confirmed", Toast.LENGTH_SHORT).show();
               } else {
                   Toast.makeText(ConfirmReservationClient.this, "Reservation failed", Toast.LENGTH_SHORT).show();
               }
            });
            goHome();
        });
        clearButton.setOnClickListener(v -> {
            goHome();
        });
    }
}
