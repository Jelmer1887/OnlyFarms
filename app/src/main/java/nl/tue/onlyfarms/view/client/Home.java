package nl.tue.onlyfarms.view.client;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Set;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.FragmentHomeClientBinding;
import nl.tue.onlyfarms.model.Store;
import nl.tue.onlyfarms.view.StoreCardAdapter;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    private FragmentHomeClientBinding binding;
    private SearchView searchView;
    private RecyclerView recyclerView;
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

        Log.d("Home", "creating view model...");
        model = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

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
}