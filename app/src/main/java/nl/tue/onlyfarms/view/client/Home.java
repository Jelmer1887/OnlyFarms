package nl.tue.onlyfarms.view.client;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.FragmentHomeBinding;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.view.StoreCardAdapter;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    private FragmentHomeBinding binding;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private HomeViewModel model;

    public static Home newInstance() {
        return new Home();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        model.requestAllStores();
        model.getStores().observe(getViewLifecycleOwner(), this::showStores);

        recyclerView = getView().findViewById(R.id.near_recyclerView);
        searchView = getView().findViewById(R.id.search);
        FloatingActionButton button = getView().findViewById(R.id.floatingActionButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.replaceElement, new Map())
                        .commitNow();
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        StoreCardAdapter adapter = new StoreCardAdapter(getViewLifecycleOwner(), model.getStores());
        recyclerView.setAdapter(adapter);

        model.getStores().observe(getViewLifecycleOwner(), stores -> {
            adapter.notifyDataSetChanged();
        });
    }

    private void showStores(Set<Store> stores) {
        if (stores == null) {
            throw new IllegalStateException("stores is changed to null after initialization");
        }
        if (stores.isEmpty()) {
            Toast.makeText(getContext(), "store list changed to empty!", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(getContext(), "found "+stores.size() + " stores", Toast.LENGTH_LONG).show();
    }
}