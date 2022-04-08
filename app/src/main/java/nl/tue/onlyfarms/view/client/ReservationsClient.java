package nl.tue.onlyfarms.view.client;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;
import nl.tue.onlyfarms.viewmodel.ReservationClientViewModel;

public class ReservationsClient extends Fragment implements RecyclerViewAdapterClientReservations.ItemClickListener {

    private RecyclerViewAdapterClientReservations adapter;
    private ReservationClientViewModel model;
    private HomeViewModel homeModel;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservations_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.rvReservationclients);

        model = new ViewModelProvider(requireActivity()).get(ReservationClientViewModel.class);
        model.setUserUid(FirebaseAuth.getInstance().getUid());

        homeModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        // set up the RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        makeAdapter();

        model.getAllDataReceived().observe(getViewLifecycleOwner(), isReceived -> {
            if (isReceived == null) { throw new IllegalStateException("allDataReceived changed to null!"); }
            Log.d(TAG, "Update to reservations data-state. Data-availability became " + isReceived);
            makeAdapter();
        });
        homeModel.getAllDataReceived().observe(getViewLifecycleOwner(), isReceived -> {
            if (isReceived == null) { throw new IllegalStateException("allDataReceived changed to null!"); }
            Log.d(TAG, "Update to stores data-state. Data-availability became " + isReceived);
            makeAdapter();
        });
    }

    private void makeAdapter() {
        if (model.getAllDataReceived().getValue() == null || homeModel.getAllDataReceived().getValue() == null)
            return;
        if (!model.getAllDataReceived().getValue() || !homeModel.getAllDataReceived().getValue()) {
            adapter = new RecyclerViewAdapterClientReservations();
            recyclerView.swapAdapter(adapter, true);
            return;
        }
        Log.d(TAG, "makeAdapter: we have all the data for the adapter. create it.");
        adapter = new RecyclerViewAdapterClientReservations(getViewLifecycleOwner(), model.getFilteredReservations(), homeModel.getStores());
        adapter.setClickListener(this);
        recyclerView.swapAdapter(adapter, true);
        model.getFilteredReservations().observe(getViewLifecycleOwner(), b -> adapter.notifyDataSetChanged());
        homeModel.getStores().observe(getViewLifecycleOwner(), b -> adapter.notifyDataSetChanged());
    }

    @Override
    public void onItemClick(View view, int position) {
        if (adapter == null) {throw new NullPointerException("adapter not set! (null)");}
        Intent intent = new Intent(getContext(), ConfirmReservationClient.class);
        intent.putExtra("reservation", adapter.getItem(position));
        intent.putExtra("store", adapter.getStore(position));
        intent.putExtra("button", false);
        Log.d("Home", "creating ReservationsClient activity with intent: " + intent);
        //startActivity(intent);
    }
}


