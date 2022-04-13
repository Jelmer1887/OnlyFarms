package nl.tue.onlyfarms.view.vendor;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.FragmentHomeVendorBinding;
import nl.tue.onlyfarms.view.AbstractHome;
import nl.tue.onlyfarms.view.Account;
import nl.tue.onlyfarms.view.MyStore;
import nl.tue.onlyfarms.view.StoreCardAdapter;
import nl.tue.onlyfarms.view.StoreGeneral;
import nl.tue.onlyfarms.view.client.MapView;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;

/**
 * Home fragment for a vendor,
 * it uses the {@link AbstractHome} class as default, and overwrites some methods to go to the
 * correct screens.
 * Showing the correct data is handled by the viewModel {@link HomeViewModel}
 */
public class HomeVendor extends AbstractHome implements StoreCardAdapter.ItemClickListener {
    @Nullable
    @Override
    /* overwrite what layout is used for the fragment */
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_vendor, container, false);
    }


    @Override
    /* the super-class takes care of most functionality, only the actionButton needs to be added. */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        assert getView() != null;
        actionButton = getView().findViewById(R.id.floatingActionButton);
        // listener for action button press to go to the map-view.
        actionButton.setOnClickListener(v -> {
            assert getContext() != null;
            Log.d("HELP", "click found!");
            startActivity(new Intent(getContext(), MyStore.class));
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);  // super checks the pre-condition
        Intent intent = new Intent(getContext(), StoreGeneral.class);
        intent.putExtra("store", adapter.getItem(position));
        intent.putExtra("isClient", true);
        Log.d("HomeVendor", "creating StoreGeneral activity with intent: " + intent);
        startActivity(intent);
    }
}
