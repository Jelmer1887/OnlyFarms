package nl.tue.onlyfarms.view.vendor;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.view.client.ConfirmReservationClient;
import nl.tue.onlyfarms.view.client.RecyclerViewAdapterClientReservations;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;
import nl.tue.onlyfarms.viewmodel.ReservationVendorViewModel;

public class ReservationsVendor extends Fragment implements RecyclerViewAdapterClientReservations.ItemClickListener {

    private RecyclerViewAdapterVendorReservation adapter;
    private ReservationVendorViewModel model;
    private HomeViewModel homeModel;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservations_vendor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.rvReservationvendors);

        // set up the RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        model = new ViewModelProvider(requireActivity()).get(ReservationVendorViewModel.class);
        homeModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeModel.getAllDataReceived().observe(getViewLifecycleOwner(), isReceived -> {
            if (isReceived == null) { throw new IllegalStateException("allDataReceived changed to null!"); }
            Log.d(TAG, "Update to stores data-state. Data-availability became " + isReceived);
            if (isReceived) {
                model.setStores(homeModel.getStores().getValue());
            }
            makeAdapter();
        });

        model.getAllDataReceived().observe(getViewLifecycleOwner(), isReceived -> {
            if (isReceived == null) { throw new IllegalStateException("allDataReceived changed to null!"); }
            Log.d(TAG, "Update to reservations data-state. Data-availability became " + isReceived);

            makeAdapter();
        });

        model.getUsersReceived().observe(getViewLifecycleOwner(), isReceived -> {
            Log.d(TAG, "Update to users data-state. Data-availability became " + isReceived);
           makeAdapter();
        });
    }

    private void makeAdapter() {
        if (!model.getAllDataReceived().getValue() || !model.getUsersReceived().getValue() || !homeModel.getAllDataReceived().getValue())
            return;
        adapter = new RecyclerViewAdapterVendorReservation(getViewLifecycleOwner(), model.getFilteredReservations(), model.getUsers());
        adapter.setClickListener(this);
        recyclerView.swapAdapter(adapter, true);
        model.getFilteredReservations().observe(getViewLifecycleOwner(), b -> adapter.notifyDataSetChanged());
        homeModel.getStores().observe(getViewLifecycleOwner(), b -> adapter.notifyDataSetChanged());
        homeModel.getProducts().observe(getViewLifecycleOwner(), b -> adapter.notifyDataSetChanged());
        model.getUsersReceived().observe(getViewLifecycleOwner(), b -> adapter.notifyDataSetChanged());
    }

    @Override
    public void onItemClick(View view, int position) {
        if (adapter == null) {throw new NullPointerException("adapter not set! (null)");}
        Intent intent = new Intent(getContext(), FulfillReservationVendor.class);
        intent.putExtra("reservation", adapter.getItem(position));
        intent.putExtra("productSet", (Serializable) homeModel.getProducts().getValue());
        intent.putExtra("button", false);
        Log.d("Home", "creating FulfillReservationVendor activity with intent: " + intent);
        startActivity(intent);
    }
}
