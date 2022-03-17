package nl.tue.onlyfarms.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import nl.tue.onlyfarms.Navigation;
import nl.tue.onlyfarms.R;
import nl.tue.onlyfarms.databinding.ActivityBaseBinding;
import nl.tue.onlyfarms.view.client.Home;

public class Base extends AppCompatActivity {

    private MaterialToolbar topBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Navigation navLogic = Navigation.getInstance();
    private ActivityBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_base);

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
                if (id == R.id.navto_logout) { logout(); return true;}

                replaceFragment(navLogic.toNavigator(id));
                drawerLayout.close();

                return true;
            }
        });

        if (savedInstanceState == null) {
            replaceFragment(Home.newInstance());
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.replaceElement, fragment)
                .commitNow();
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginView.class));
        finish();
    }
}