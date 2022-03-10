package nl.tue.onlyfarms.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityHomeBinding;
import nl.tue.onlyfarms.ui.main.MyStore;


public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private RecyclerView recyclerView;
    private SearchView searchView;

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        searchView = binding.search;
        recyclerView = binding.nearRecyclerView;
        logoutButton = binding.logoutTemp;


        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyStore.class));
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new StoreCardAdapter(1234));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}