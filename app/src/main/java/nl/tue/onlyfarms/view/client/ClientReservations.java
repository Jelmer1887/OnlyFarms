package nl.tue.onlyfarms.view.client;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nl.tue.onlyfarms.R;

public class ClientReservations extends AppCompatActivity implements RecyclerViewAdapterClientReservations.ItemClickListener {

    RecyclerViewAdapterClientReservations adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reservation_client);

        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Store 1");
        animalNames.add("Store 2");
        animalNames.add("Store 3");
        animalNames.add("Store 4");
        animalNames.add("Store 5");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvReservationclients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapterClientReservations(this, animalNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}


