package nl.tue.onlyfarms.view.client;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.FragmentHomeClientBinding;
import nl.tue.onlyfarms.view.StoreCardAdapter;
import nl.tue.onlyfarms.view.StoreGeneral;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements StoreCardAdapter.ItemClickListener{

    private static final String TAG = "Home";
    private FragmentHomeClientBinding binding;
    private SearchView searchView;
    private FloatingActionButton actionButton;

    private RecyclerView recyclerView;              // list element where cards appear
    private StoreCardAdapter adapter;               // adapter that will be used in the list-card

    private HomeViewModel model;

    public static Home newInstance() {
        return new Home();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeClientBinding.inflate(getLayoutInflater());
        return inflater.inflate(R.layout.fragment_home_client, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() == null) {
            throw new NullPointerException("Attempted to launch homeClient-fragment without Activity!");
        }

        // Variable initialisation
        recyclerView = getView().findViewById(R.id.near_recyclerView);
        searchView = getView().findViewById(R.id.search);
        actionButton = getView().findViewById(R.id.floatingActionButton);

        /* ViewModel retrieval */
        Log.d(TAG, "retrieving viewModel");
        model = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);    // model != null <=>
        Log.d(TAG, "performing health checks on model");                                // constructor called <=>
        assert model.getAllDataReceived() != null;                                          // getAllDataReceived != null &&
        assert model.getAllDataReceived().getValue() != null;                               // getAllDataReceived.getValue() != null &&
        assert model.getUser() != null;                                                     // getUser() != null <=>
                                                                                            // eventually getStores() != null <=>
        /* storeList creation */                                                            // eventually getFilteredStores() != null
        Log.d(TAG, "attempting to create list with data...");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (model.getAllDataReceived().getValue()) {
            Log.d(TAG, "all data available upon fragment initialisation! creating list with data");
            adapter = new StoreCardAdapter(getViewLifecycleOwner(), model.getFilteredStores());
            adapter.setClickListener(this);
        } else {
            Log.e(TAG, "data unavailable upon fragment initialisation! creating empty list");
            adapter = new StoreCardAdapter();
        }
        recyclerView.setAdapter(adapter);

        /* ------ Listeners ------ */

        // data-state listener - changes and notifies adapter when availability/content of data changes.
        model.getAllDataReceived().observe(getViewLifecycleOwner(), isReceived -> {
            if (isReceived == null) { throw new IllegalStateException("allDataReceived changed to null!"); }
            Log.d(TAG, "Update to data-state. Data-availability became " + isReceived);

            adapter = isReceived ? new StoreCardAdapter(getViewLifecycleOwner(), model.getFilteredStores()) : new StoreCardAdapter();
            adapter.setClickListener(this);
            recyclerView.swapAdapter(adapter, true);
            model.getFilteredStores().observe(getViewLifecycleOwner(), b -> adapter.notifyDataSetChanged());
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.replaceElement, new MapView())
                        .commitNow();
            }
        });

        // search bar filter application - follows: https://stackoverflow.com/questions/19588311
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, String.format("search text entered is: '%s'", newText));
                if (newText.length() == 0) {
                    model.removeFilter("searchStoreName");
                    model.removeFilter("searchStoreProduct");
                    model.applyFilters();
                    return false;
                }
                model.addFilter("searchStoreName", store ->
                        store.getName().toLowerCase(Locale.ROOT)
                                .contains(newText.toLowerCase(Locale.ROOT))
                );
                model.addFilter("searchStoreProduct", HomeViewModel.OR , store ->
                        model.storeHasProductInSet(store, model.getProductsMatchingName(newText))
                );
                model.applyFilters();
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
    }



    @Override
    public void onItemClick(View view, int position) {
        if (adapter == null) {throw new NullPointerException("adapter not set! (null)");}
        Intent intent = new Intent(getContext(), StoreGeneral.class);
        intent.putExtra("store", adapter.getItem(position));
        Log.d("Home", "creating StoreGeneral activity with intent: " + intent);
        startActivity(intent);
    }
}