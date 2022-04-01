package nl.tue.onlyfarms.view.vendor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nl.tue.onlyfarms.R;

public class VendorReservations extends AppCompatActivity {

    MyRecyclerViewAdapterVendorReservation adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reservations_vendor);

        ArrayList<String> clientNames = new ArrayList<>();
        clientNames.add("Isabel Client");
        clientNames.add("Jan Janssen");

        RecyclerView recyclerView = findViewById(R.id.rvReservationvendors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapterVendorReservation(this, clientNames);
        recyclerView.setAdapter(adapter);
    }
}
