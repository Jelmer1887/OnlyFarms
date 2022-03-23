package nl.tue.onlyfarms.view.vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.FragmentHomeVendorBinding;
import nl.tue.onlyfarms.view.StoreCardAdapter;
import nl.tue.onlyfarms.viewmodel.HomeViewModel;


public class HomeVendor extends Fragment {

    private static final String TAG = "HomeVendor";

    private FragmentHomeVendorBinding binding;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private HomeViewModel model;

    public static HomeVendor newInstance() { return new HomeVendor(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentHomeVendorBinding.inflate(getLayoutInflater());
        return inflater.inflate(R.layout.fragment_home_vendor, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "create view model");
        model = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        model.requestUserStores(FirebaseAuth.getInstance().getUid());

        //TODO: shouldn't these ID's be different than those of the client Homepage?
        recyclerView = getView().findViewById(R.id.near_recyclerView);
        searchView = getView().findViewById(R.id.search);
        FloatingActionButton button = getView().findViewById(R.id.floatingActionButton);


        // recyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        StoreCardAdapter adapter = new StoreCardAdapter(getViewLifecycleOwner(), model.getStores());
        recyclerView.setAdapter(adapter);

        model.getStores().observe(getViewLifecycleOwner(), stores -> {
            adapter.notifyDataSetChanged();
        });
    }
}
