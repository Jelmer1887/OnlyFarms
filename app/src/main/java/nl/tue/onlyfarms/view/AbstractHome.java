package nl.tue.onlyfarms.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;

/**
 * Abstract home view,
 * This class handles all common logic for showing our home screens.
 * Implementations should overwrite the onCreateView method to specify what
 * layout is used, the onViewCreated to specify where the action button should lead,
 * and the onItemClick method to specify where clicking a card should lead.
 *
 * What data is shown is handled by the {@link HomeViewModel}
 * */
public abstract class AbstractHome extends Fragment implements StoreCardAdapter.ItemClickListener {
    private static final String TAG = "HomeAbstract";

    private SearchView searchView;
    protected FloatingActionButton actionButton;    // this is protected to allow use overwrites in sub-classes

    private RecyclerView recyclerView;  // UI element where the cards of stores appear
    protected StoreCardAdapter adapter;   // adapter that binds the data to the UI cards

    private HomeViewModel model;  // model class required to retrieve and process data

    /* This method should be overwritten by each implementation, to change what layout is used */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /* method responsible for attaching functionality to UI elements and setting / retrieving data */
    /* this should be overwritten to implement the actionButton functionality! */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "superclass method called!");
        if (getActivity() == null) {
            throw new NullPointerException("Attempted to launch home-fragment without Activity!");
        }
        if (getView() == null) {
            throw new NullPointerException("View of home-fragment is null!");
        }

        // post: activity != null -> activity == Base
        // post: getView != null -> findViewById will be called on valid view

        /* -- Variable initialisation -- */
        recyclerView = getView().findViewById(R.id.near_recyclerView);
        searchView = getView().findViewById(R.id.search);
        //actionButton = getView().findViewById(R.id.floatingActionButton);

        /* -- ViewModel retrieval -- */
        Log.d(TAG, "retrieving viewModel");
        model = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);    // model != null <=>
        model.activateDistanceFilter(getContext());

        // when the model is retrieve after going back, it has to be in a valid state
        Log.d(TAG, "performing health checks on model");                         // constructor called <=>
        assert model.getAllDataReceived() != null;                                    // getAllDataReceived != null &&
        assert model.getAllDataReceived().getValue() != null;                         // getAllDataReceived.getValue() != null &&
        assert model.getUser() != null;                                                 // eventually getFilteredStores() != null
        // post: getUser, getAllDataReceived and allDataReceived are set and usable

        /* -- storeList creation -- */
        // Initial setup, defines how the list 'behaves' with the other elements
        Log.d(TAG, "attempting to create list with data...");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /* ------ Listeners ------ */

        // data-state listener - changes and notifies adapter when availability/content of data changes.
        model.getAllDataReceived().observe(getViewLifecycleOwner(), isReceived -> {
            // null check to validate that no viewModel ever sets its data-state flag to null
            if (isReceived == null) {
                throw new IllegalStateException("allDataReceived changed to null!");
            }
            Log.d(TAG, "Update to data-state. Data-availability became " + isReceived);

            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, location -> {
                if (getView() == null) return;

                view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                view.findViewById(R.id.loading).setVisibility(View.GONE);
                view.findViewById(R.id.location).setVisibility(View.GONE);

                // adapter is empty if isReceived == false, else it received the filtered stores in random order.
                adapter = new StoreCardAdapter(getViewLifecycleOwner(), model.getFilteredStores(), location);
                adapter.setClickListener(this);
                recyclerView.swapAdapter(adapter, true);

                // listener to change the list when data is updated.
                // note: getFilteredStores() is only guaranteed to be non-null inside this listener.
                model.getFilteredStores().observe(getViewLifecycleOwner(), b -> adapter.notifyDataSetChanged());
            });
        });

        // The action button listener should be implemented here

        // search bar filter application - follows: https://stackoverflow.com/questions/19588311
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /* Here functionality can be put for when the user hits 'search' */
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            /* this method gets called whenever the user types anything in the bar (inc. removing) */
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, String.format("search text entered is: '%s'", newText));

                // Remove filters when user clears their search
                if (newText.length() == 0) {
                    model.removeFilter("searchStoreName");
                    model.removeFilter("searchStoreProduct");
                    model.applyFilters();
                    return false;
                }
                // -> newText.length() > 0

                // add filter to search for the store-name containing the search-string
                model.addFilter("searchStoreName", store ->
                        store.getName().toLowerCase(Locale.ROOT)    // required to ignore case
                                .contains(newText.toLowerCase(Locale.ROOT)) // required to fuzzy-search
                );

                // add filter to search for product-names of store containing the search-string
                // note: this filter is set to be only additive (will never remove stores from the list, only include them)
                model.addFilter("searchStoreProduct", HomeViewModel.OR , store ->
                        model.storeHasProductInSet(store, model.getProductsMatchingName(newText))
                );

                model.applyFilters();
                return false;
            }
        });

        // Listener to change appearance of search-bar if the usr clicks it.
        searchView.setOnClickListener(v -> searchView.setIconified(false));

        super.onViewCreated(view, savedInstanceState);  // calling super will restore any previous states
    }

    /* Override of method, responsible for changing  */
    /* This method should be overwritten to include the user-state */
    @Override
    public void onItemClick(View view, int position) {
        if (adapter == null) {throw new NullPointerException("adapter not set! (null)");}
    }
}
