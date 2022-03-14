package nl.tue.onlyfarms.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityHomeBinding;
import nl.tue.onlyfarms.ui.main.MyStore;


public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private RecyclerView recyclerView;
    private SearchView searchView;

    private Button logoutButton;

    MaterialToolbar topBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        topBar = findViewById(R.id.home_topBar);
        drawerLayout = findViewById(R.id.home_drawerLayout);
        navigationView = findViewById(R.id.home_navigationBar);
        searchView = binding.search;
        recyclerView = binding.nearRecyclerView;

        logoutButton = binding.logoutTemp;

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { drawerLayout.open(); }
        });

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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // TODO: generalize navigation logic to new file
                int id = item.getItemId();
                if (id == R.id.navto_logout) {
                    logout();
                    return true;
                } else if (id == R.id.navto_home) {
                    drawerLayout.close();
                    return true;
                } else if (id == R.id.navto_mystore) {
                    startActivity(new Intent(getApplicationContext(), MyStore.class));
                    finish();
                    return true;
                }

                return false;
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

    public void logout(){
        startActivity(new Intent(getApplicationContext(), LoginView.class));
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}
