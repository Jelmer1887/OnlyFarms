package nl.tue.onlyfarms.view.client;

import android.annotation.SuppressLint;
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

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.FragmentHomeClientBinding;
import nl.tue.onlyfarms.view.RecyclerViewAdapterEmpty;
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

    private RecyclerView recyclerView;              // list element where cards appear
    private StoreCardAdapter adapterCards;           // adapter that will be used in the list-card

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
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.near_recyclerView);
        searchView = getView().findViewById(R.id.search);
        FloatingActionButton button = getView().findViewById(R.id.floatingActionButton);
        if (getActivity() == null) {
            throw new NullPointerException("Attempted to launch home-fragment without Activity!");
        }

        Log.d("Home", "retrieving view model...");
        model = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        model.getIsDataReceived().observe(getActivity(), isReceived -> {
            if (isReceived) {
                Log.d(TAG, "observer: all data is received -> replacing adapter!");
                adapterCards = new StoreCardAdapter(getViewLifecycleOwner(), model.getStores());
                model.getStores().observe(getViewLifecycleOwner(), s -> adapterCards.notifyDataSetChanged());
                adapterCards.setClickListener(this);
                recyclerView.swapAdapter(adapterCards,true);
            } else {
                Log.e(TAG, "observer: data is no longer present -> swapping to empty adapter!");
                RecyclerViewAdapterEmpty adapterEmpty = new RecyclerViewAdapterEmpty();
                recyclerView.swapAdapter(adapterEmpty,true);
            }
        });
        if (model.getIsDataReceived() == null) {
            throw new NullPointerException("Somehow the isDataReceived flag was never initialized");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.replaceElement, new MapView())
                        .commitNow();
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        Log.d("Home", "creating UI for storelist...");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (model.getIsDataReceived().getValue() == null) {
            Log.e("Home", "no store or user lifeData available yet -> building list with empty adapter");
            RecyclerViewAdapterEmpty adapterEmpty = new RecyclerViewAdapterEmpty();
            recyclerView.setAdapter(adapterEmpty);
        } else {
            adapterCards = new StoreCardAdapter(getViewLifecycleOwner(), model.getStores());
            model.getStores().observe(getViewLifecycleOwner(), s -> adapterCards.notifyDataSetChanged());
            adapterCards.setClickListener(this);
            recyclerView.setAdapter(adapterCards);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), StoreGeneral.class);
        intent.putExtra("store", adapterCards.getItem(position));
        Log.d("Home", "creating StoreGeneral activity with intent: " + intent);
        startActivity(intent);
    }
}