package nl.tue.onlyfarms.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.FragmentHomeBinding;

public class HomeView extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private SearchView searchView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = binding.search;
        recyclerView = binding.nearRecyclerView;
        // TODO: recycler logic
        // TODO: search logic

        // Makes sure entire search field is clickable
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}