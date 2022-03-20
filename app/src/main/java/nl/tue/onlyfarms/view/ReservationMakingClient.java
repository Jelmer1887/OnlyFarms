package nl.tue.onlyfarms.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.tue.onlyfarms.R;

public class ReservationMakingClient extends AppCompatActivity {


    List<Reservation> reservationList = new ArrayList<Reservation>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_makingareservation_client);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecycleViewAdapter(reservationList, ReservationMakingClient.this);
        recyclerView.setAdapter(mAdapter);

    }

    private void fillReservationList(int id) {
        Reservation r0 = new Reservation(0);
        Reservation r1 = new Reservation( 1);
        Reservation r2 = new Reservation( 2);
        Reservation r3 = new Reservation( 3);
        Reservation r4 = new Reservation( 4);
        Reservation r5 = new Reservation( 5);
        Reservation r6 = new Reservation( 6);

        reservationList.addAll(Arrays.asList( new Reservation[] { r0, r1, r2, r3, r4, r5, r6}));
    }
}
