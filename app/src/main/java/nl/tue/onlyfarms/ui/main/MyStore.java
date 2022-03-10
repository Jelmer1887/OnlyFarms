package nl.tue.onlyfarms.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.ui.main.fragment_myStore;
import nl.tue.onlyfarms.view.Home;
import nl.tue.onlyfarms.view.LoginView;

public class MyStore extends AppCompatActivity {

    MaterialToolbar topBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_store_activity);

        topBar = findViewById(R.id.topBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationBar);

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navto_logout) {
                    logout();
                    return true;
                } else if (id == R.id.navto_home) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                    return true;
                }

                return false;
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.replaceElement, fragment_myStore.newInstance())
                    .commitNow();
        }
    }

    public void logout(){
        startActivity(new Intent(getApplicationContext(), LoginView.class));
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}